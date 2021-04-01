package com.sera.memorygame.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /**
     *
     */
    fun getRetrofitInstance(baseUrl: String, timeout: Long): Retrofit {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .serializeNulls()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(createOkHttpClient(timeout))
            .build()


    }

    /**
     *
     */
    private fun createOkHttpClient(timeout: Long): OkHttpClient {

        val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
        httpClient.connectTimeout(timeout, TimeUnit.SECONDS)
        httpClient.callTimeout(timeout, TimeUnit.SECONDS)
        httpClient.readTimeout(timeout, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)
        return httpClient.build()
    }

}