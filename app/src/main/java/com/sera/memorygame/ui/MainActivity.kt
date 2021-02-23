package com.sera.memorygame.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sera.memorygame.R
import com.sera.memorygame.databinding.ActivityMainBinding
import com.sera.memorygame.interfaces.Handlers

class MainActivity : AppCompatActivity(), Handlers {
    private lateinit var mBinder: ActivityMainBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinder.handlers = this
//        supportFragmentManager.beginTransaction()
//            .add(R.id.container, MemoryFragment.newInstance(), MemoryFragment::class.java.canonicalName)
//            .addToBackStack("fragment")
//            .commit()
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
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment::class.java.canonicalName)
            .addToBackStack("fragment")
            .commit()
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