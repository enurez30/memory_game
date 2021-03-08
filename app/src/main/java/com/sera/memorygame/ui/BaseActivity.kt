package com.sera.memorygame.ui

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.R
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.utils.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    /**
     *
     */
    private var assetWorkerExecutingStatus = MutableLiveData<Int>().apply {
        value = NetworkStatus.NONE.status
    }

    /**
     *
     */
    var getAssetWorkerExecutingStatus: MutableLiveData<Int>
        get() = assetWorkerExecutingStatus
        set(value) {
            assetWorkerExecutingStatus = value
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
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment, fragment::class.java.simpleName)
            .addToBackStack("fragment")
            .commit()
    }

    /**
     *
     */
    fun showSnackBar(content: String, view: View, duration: Int = Snackbar.LENGTH_SHORT, map: HashMap<String, Any>? = null, callback: (Any) -> Unit) {
        val snack = Snackbar.make(view, content, duration)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(view.context.resources.getColor(R.color.colorDarkGrayBackground, null))
        map?.let {
            val action = map["action_text"] as String
            snack.setAction(action) {

            }
        }
        snack.show()
        callback(snack)
    }

    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onEvent(event: MessageEvent) {
        if (event.reciever == BaseActivity::class.java.simpleName) {
            when (event.key) {
                "test" -> {
                    println("event bus test")
                }
                "network_status" -> {
                    lifecycleScope.launch(Dispatchers.Main) {
                        getAssetWorkerExecutingStatus.value = event.network_status
                    }
                }
            }
        }
    }

}