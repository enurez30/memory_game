package com.sera.memorygame

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MemoryApplication : Application() {

    /**
     *
     */
    companion object{
        lateinit var appContext: Context
            private set
    }


    /**
     *
     */
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}
