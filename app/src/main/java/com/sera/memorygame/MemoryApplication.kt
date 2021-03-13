package com.sera.memorygame

import android.app.Application
import android.content.Context
import com.sera.memorygame.di.AppComponent
import com.sera.memorygame.di.DaggerAppComponent

open class MemoryApplication : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }

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
