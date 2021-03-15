package com.sera.memorygame.di

import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.dialog.UserDialog
import com.sera.memorygame.ui.settings.SettingsFragment
import com.sera.memorygame.ui.start.StartFragment
import com.sera.memorygame.ui.theme.GameThemeFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {

    // Factory to create instances of RegistrationComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: MainActivity)
    fun inject(fragment: StartFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: GameThemeFragment)
    fun inject(dialog: UserDialog)
}