package com.sera.memorygame.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.service.AssetWorker
import com.sera.memorygame.utils.NetworkStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            addObserver()
        }
        WorkManager.getInstance(this).enqueue(OneTimeWorkRequestBuilder<AssetWorker>().build())
    }

    /**
     *
     */
    private suspend fun addObserver() {
        workerState.collect {
            when (it) {
                NetworkStatus.START.status -> {
                    this.showSnackBar("Check for updates", duration = Snackbar.LENGTH_INDEFINITE)
                }
                NetworkStatus.DOWNLOAD.status -> {
                    this.showSnackBar("Downloading Assets", duration = Snackbar.LENGTH_INDEFINITE)
                }
                NetworkStatus.ERROR.status -> {
                    this.showSnackBar("Error", duration = Snackbar.LENGTH_INDEFINITE)
                    moveNext()
                }
                NetworkStatus.NO_INTERNET_CONNECTION.status -> {
                    this.showSnackBar("No internet connection", duration = Snackbar.LENGTH_INDEFINITE)
                    moveNext()
                }
                NetworkStatus.FINISH.status -> {
                    moveNext()
                }
            }
        }

    }

    /**
     *
     */
    private fun moveNext() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}