package com.sera.memorygame.ui

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.R
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.utils.ContextUtils
import com.sera.memorygame.utils.Prefs
import com.sera.memorygame.utils.status_callback.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {

        val localeToSwitchTo = Prefs.getAppLanguage()
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, Locale(localeToSwitchTo))
        super.attachBaseContext(localeUpdatedContext)
    }

    /**
     *
     */
    protected val workerState: MutableStateFlow<Int> by lazy {
        MutableStateFlow(NetworkStatus.NONE.status)
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
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment::class.java.simpleName)
            .addToBackStack("fragment")
            .commit()
    }

    /**
     *
     */
    fun addFragment(fragment: Fragment) {
        with(supportFragmentManager) {
            findFragmentByTag(fragment::class.java.simpleName)?.let {
                beginTransaction().remove(it).commit()
            }
            beginTransaction()
                .add(R.id.container, fragment, fragment::class.java.simpleName)
                .addToBackStack("fragment")
                .commit()
        }


    }

    /**
     *
     */
    fun showSnackBar(content: String, view: View = window.decorView.rootView, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(view, content, duration)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(view.context.resources.getColor(R.color.colorDarkGrayBackground, null))
            .show()
    }

    /**
     *
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    fun onEvent(event: MessageEvent) {
        if (event.reciever == BaseActivity::class.java.simpleName) {
            when (event.key) {
                "test" -> {
                    println("event bus test")
                }
                "network_status" -> {
                    lifecycleScope.launch(Dispatchers.Main) {
                        workerState.value = event.network_status
                    }
                }
            }
        }
    }

}