@file:Suppress("DEPRECATION")

package com.sera.memorygame.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sera.memorygame.MessageEvent
import com.sera.memorygame.R
import com.sera.memorygame.databinding.ActivityMainBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.service.AssetWorker
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), Handlers {
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

        println("DOWNLOAD: WorkManager start")
        WorkManager.getInstance(this).enqueue(OneTimeWorkRequestBuilder<AssetWorker>().build())
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
            .replace(R.id.container, fragment, fragment::class.java.simpleName)
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

    /**
     *
     */
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    /**
     *
     */
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onEvent(event: MessageEvent) {
        if (event.reciever == this::class.java.simpleName) {
            when (event.key) {
                "test" -> {
                    println("event bus test")
                }
                "assets_service" -> {
                    println(event.message)
                }
            }
        }
    }


}