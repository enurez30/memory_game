package com.sera.memorygame.di

import dagger.Module

// This module tells AppComponent which are its subcomponents
@Module(subcomponents = [SplashComponent::class, MainComponent::class])
class AppSubcomponents