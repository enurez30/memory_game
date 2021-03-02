@file:Suppress("DEPRECATION")

package com.sera.memorygame.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.sera.memorygame.R
import com.sera.memorygame.databinding.ActivityMainBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.ui.start.StartFragment

class MainActivity : BaseActivity(), Handlers {
    private lateinit var mBinder: ActivityMainBinding

    /**
     *AssetWorker
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinder.handlers = this
        replaceFragment(fragment = StartFragment.newInstance())
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
        }
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
        TODO("Not yet implemented")
    }

    /**
     *
     */
    override fun onLongClicked(view: View, position: Int?): Boolean {
        TODO("Not yet implemented")
    }





}