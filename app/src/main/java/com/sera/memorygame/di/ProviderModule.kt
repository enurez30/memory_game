package com.sera.memorygame.di

import android.content.Context
import com.sera.memorygame.providers.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ProviderModule {

    @Provides
    fun providesResources(@ApplicationContext appContext: Context): ResourcesProvider {
        return ResourcesProvider(context = appContext)
    }

}