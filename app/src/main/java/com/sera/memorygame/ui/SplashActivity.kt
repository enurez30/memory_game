package com.sera.memorygame.ui

import android.content.Intent
import android.os.Bundle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sera.memorygame.service.AssetWorker

class SplashActivity : BaseActivity() {

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WorkManager.getInstance(this).enqueue(OneTimeWorkRequestBuilder<AssetWorker>().build())
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}