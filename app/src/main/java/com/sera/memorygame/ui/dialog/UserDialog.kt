package com.sera.memorygame.ui.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.R
import com.sera.memorygame.database.repository.UserRepository
import com.sera.memorygame.databinding.UserDialogLayoutBinding
import com.sera.memorygame.factory.UserViewModelFactory
import com.sera.memorygame.ui.BaseActivity
import com.sera.memorygame.utils.PictureLifecycleObserver
import com.sera.memorygame.viewModel.UserViewModel
import kotlinx.coroutines.flow.collect

class UserDialog : BaseDialogFragment() {
    private lateinit var mBinder: UserDialogLayoutBinding
    private lateinit var pictureObserver: PictureLifecycleObserver

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
    private val userViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(context = requireContext(), repo = UserRepository(context = requireContext()))
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
                        val img = when {
                            Build.VERSION.SDK_INT < Build.VERSION_CODES.P -> {
                                @Suppress("DEPRECATION")
                                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                            }
                            else -> {
                                val source = ImageDecoder.createSource(requireActivity().contentResolver, it)
                                ImageDecoder.decodeBitmap(source)
                            }
                        }
                        mBinder.image.setImageBitmap(img)
//                        userViewModel.getUserInSession().value?.let { userEntity ->
//                            val fName = FileUtils.saveAndDeleteOldImage(img, requireContext(), userEntity)
//                            userEntity.avatar = fName
//                        }
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