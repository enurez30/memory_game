package com.sera.memorygame.di

import com.sera.memorygame.ui.SplashActivity
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@ActivityScope
@Subcomponent
interface SplashComponent {

    // Factory to create instances of RegistrationComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: SplashActivity)
}