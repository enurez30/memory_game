package com.sera.memorygame.di

import android.content.Context
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.SplashActivity
import dagger.BindsInstance
import dagger.Component

@Component
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }


    fun inject(activity: MainActivity)
    fun inject(activity: SplashActivity)
}