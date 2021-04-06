package com.sera.memorygame.di

import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.dialog.UserDialog
import com.sera.memorygame.ui.flag_quiz.FlagQuizContainerFragment
import com.sera.memorygame.ui.flag_quiz.FlagQuizFragment
import com.sera.memorygame.ui.settings.SettingsFragment
import com.sera.memorygame.ui.start.StartFragment
import com.sera.memorygame.ui.theme.GameThemeFragment
import com.sera.memorygame.ui.trivia.TriviaChooseFragment
import com.sera.memorygame.ui.trivia.TriviaContainerFragment
import com.sera.memorygame.ui.trivia.TriviaFragment
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
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
    fun inject(fragment: FlagQuizFragment)
    fun inject(fragment: FlagQuizContainerFragment)
    fun inject(fragment: TriviaContainerFragment)
    fun inject(fragment: TriviaChooseFragment)
    fun inject(fragment: TriviaFragment)
    fun inject(dialog: UserDialog)
}