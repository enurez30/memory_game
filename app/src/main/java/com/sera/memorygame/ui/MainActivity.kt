@file:Suppress("DEPRECATION")

package com.sera.memorygame.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.sera.memorygame.R
import com.sera.memorygame.databinding.ActivityMainBinding
import com.sera.memorygame.extentions.toResId
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.utils.Prefs
import com.sera.memorygame.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity(), Handlers {

    @Inject
    lateinit var userViewModel: UserViewModel


    /**
     *
     */
    private val mBinder by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Prefs.getTheme())
        mBinder.handlers = this
        mBinder.lifecycleOwner = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        lifecycleScope.launch {
            addObservers()
        }

    }

    /**
     *
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            println("RESTORE_STATE: ORIENTATION_LANDSCAPE")
        } else {
            println("RESTORE_STATE: ORIENTATION_PORTRATE")
        }
    }

    /**
     *
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isRestore", true)
    }

    /**
     *
     */
    private suspend fun addObservers() {
        userViewModel.getUserInSession().collect {
            println()
            mBinder.navLayout.userName.text = userViewModel.getName()
            it?.avatar?.toUri()?.let { uri ->
                setImage(uri = uri)
            }
        }
    }

    /**
     *
     */
    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .error(R.drawable.bckg_squires)
            .fallback(R.drawable.bckg_squires)
            .into(mBinder.navLayout.userIV)
    }

    /**
     *
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.iconHamburger -> {
                if (mBinder.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mBinder.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    mBinder.drawerLayout.openDrawer(GravityCompat.START)
                }
            }
            R.id.setttingsTV -> {
                findNavController(mBinder.container.id).navigate(R.id.action_global_settingsFragment)
//                addFragment(fragment = SettingsFragment.newInstance())
                closeDrawer()
            }
        }
    }

    /**
     *
     */
    private fun closeDrawer() {
        Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
            mBinder.drawerLayout.closeDrawer(GravityCompat.START)
        }, 230)
    }

    /**
     *
     */
    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
//        with(supportFragmentManager) {
//            if (this.fragments.size <= 2 && this.findFragmentByTag(StartFragment::class.java.simpleName)?.isVisible == true) {
//                // nothing, add exit dialog
//            } else {
////                this.popBackStack()
//                findNavController(R.id.container).popBackStack()
//            }
//        }
        with(findNavController(mBinder.container.id)) {
            if (this.backStack.count() <= 2) {
                // nothing, add exit dialog
            } else {
                this.popBackStack()
            }
        }
    }

    /**
     *
     */
    override fun updateTitle(id: String) {
        mBinder.include.mainTitle.text = getString(id.toResId)
    }

    /**
     *
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {

    }

    /**
     *
     */
    override fun onLongClicked(view: View, position: Int?): Boolean = true


}