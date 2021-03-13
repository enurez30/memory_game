package com.sera.memorygame.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Expose SplashComponent factory from the graph
    fun splashComponent(): SplashComponent.Factory
    fun mainComponene(): MainComponent.Factory

//    fun inject(activity: MainActivity)
//    fun inject(fragment: BaseFragment)
//    fun inject(fragment: StartFragment)
}