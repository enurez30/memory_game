@file:Suppress("DEPRECATION")

package com.sera.memorygame.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.sera.memorygame.R
import com.sera.memorygame.database.repository.UserRepository
import com.sera.memorygame.databinding.ActivityMainBinding
import com.sera.memorygame.factory.MainActivityFactory
import com.sera.memorygame.factory.UserViewModelFactory
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.ui.start.StartFragment
import com.sera.memorygame.utils.Prefs
import com.sera.memorygame.viewModel.MainActivityViewModel
import com.sera.memorygame.viewModel.UserViewModel
import kotlinx.coroutines.flow.collect

class MainActivity : BaseActivity(), Handlers {
//    private lateinit var mBinder: ActivityMainBinding

    /**
     *
     */
    private val viewModel by viewModels<MainActivityViewModel> {
        MainActivityFactory(context = this, repo = UserRepository(context = this))
    }

    /**
     *
     */
    private val userViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(context = this, repo = UserRepository(context = this))
    }

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
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
        replaceFragment(fragment = StartFragment.newInstance())

        lifecycleScope.launchWhenCreated {
            addObservers()
        }
    }

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
        }
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
                addFragment(fragment = SettingsFragment.newInstance())
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
    override fun onBackPressed() {
        if (supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] is StartFragment) {
            // nothing, add exit dialog
        } else {
            supportFragmentManager.popBackStack()
        }
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