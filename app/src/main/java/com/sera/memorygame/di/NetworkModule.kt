package com.sera.memorygame.di

import com.sera.memorygame.BuildConfig
import com.sera.memorygame.network.IService
import com.sera.memorygame.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideBaseUrl() = Constants.TRIVIA_BASE_URL

    @Provides
    fun provideTimeout() = 10L


    @Singleton
    @Provides
    fun provideOkHttpClient(timeout: Long) = if (BuildConfig.DEBUG) {
        val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
        httpClient.connectTimeout(timeout, TimeUnit.SECONDS)
        httpClient.callTimeout(timeout, TimeUnit.SECONDS)
        httpClient.readTimeout(timeout, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)
        httpClient.build()
    } else {
        val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
        httpClient.connectTimeout(timeout, TimeUnit.SECONDS)
        httpClient.callTimeout(timeout, TimeUnit.SECONDS)
        httpClient.readTimeout(timeout, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout, TimeUnit.SECONDS)
        httpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): IService = retrofit.create(IService::class.java)
    

}