package com.sera.memorygame.ui.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.R
import com.sera.memorygame.databinding.UserDialogLayoutBinding
import com.sera.memorygame.ui.BaseActivity
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.utils.FileUtils
import com.sera.memorygame.utils.observers.PictureLifecycleObserver
import com.sera.memorygame.viewModel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserDialog : BaseDialogFragment() {
    private lateinit var mBinder: UserDialogLayoutBinding
    private lateinit var pictureObserver: PictureLifecycleObserver

    @Inject
    lateinit var userViewModel: UserViewModel

    /**
     *
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as? MainActivity?)?.mainComponent?.inject(dialog = this)
    }

    /**
     *
     */
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            pictureObserver.selectImage(isCamera = true)
        } else {
            (requireActivity() as? BaseActivity?)?.showSnackBar(
                content = requireContext().resources.getString(R.string.error_camera_permission),
                view = mBinder.root,
                duration = Snackbar.LENGTH_LONG
            )
        }
    }

    /**
     *
     */
    companion object {
        fun newInstance() = UserDialog()
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = UserDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return mBinder.root
    }

    /**
     *
     */
    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        addEditTextListeners()
        setUpPictureObserver()
        lifecycleScope.launchWhenCreated {
            addObservers()
        }
    }

    /**
     *
     */
    @SuppressLint("NewApi")
    private fun setUpPictureObserver() {
        PictureLifecycleObserver(context = requireContext(), registry = requireActivity().activityResultRegistry) { result ->
            when {
                result.isSuccess -> {
                    (result.getOrNull() as? Uri?)?.let {
                        userViewModel.getUserInSession().value?.let { userEntity ->
                            FileUtils.deleteFileByUri(pCtx = requireContext(), path = userEntity.avatar)
                            userEntity.avatar = it.toString()
                        }
                        setImage(uri = it)
                    }
                }
                result.isFailure -> {
                    (requireActivity() as? BaseActivity?)?.showSnackBar(content = "Some error", view = mBinder.root)
                }
            }

        }.also {
            lifecycle.addObserver(it)
            pictureObserver = it
        }
    }

    /**
     *
     */
    private fun setImage(uri: Uri) {
        Glide.with(requireContext())
            .load(uri)
            .error(R.drawable.bckg_squires)
            .fallback(R.drawable.bckg_squires)
            .into(mBinder.image)
    }

    /**
     *
     */
    private fun addEditTextListeners() {
        mBinder.textField.setEndIconOnClickListener {
            mBinder.textField.editText?.setText("")
            mBinder.textField.error = null
        }
    }

    /**
     *
     */
    private suspend fun addObservers() {
        userViewModel.getUserInSession().collect {
            println()
            mBinder.textField.editText?.setText(userViewModel.getName())
            it?.avatar?.toUri()?.let { uri ->
                setImage(uri = uri)
            }
        }
    }

    /**
     *
     */
    private fun validate(): Boolean {
        return mBinder.textField.editText?.text.toString().trim().isNotEmpty()
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.cancelBtn -> {
                dismiss()
            }
            R.id.positiveBtn -> {
                if (validate()) {
                    userViewModel.getUserInSession().value?.userName = mBinder.textField.editText?.text.toString().trim()
                    userViewModel.updateUser()
                    dismiss()
                } else {
                    mBinder.textField.error = requireContext().resources.getString(R.string.empty_field)
                }
            }
            R.id.imgFab -> {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }
}