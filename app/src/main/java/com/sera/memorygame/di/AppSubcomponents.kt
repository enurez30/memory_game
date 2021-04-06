package com.sera.memorygame.di

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi

// This module tells AppComponent which are its subcomponents
@ExperimentalCoroutinesApi
@Module(subcomponents = [SplashComponent::class, MainComponent::class])
class AppSubcomponents