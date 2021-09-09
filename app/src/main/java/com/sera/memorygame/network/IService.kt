package com.sera.memorygame.network

import com.sera.memorygame.network.model.TriviaCategoryResponseBody
import com.sera.memorygame.network.model.TriviaResponseBody
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IService {

    /**
     *
     */
    @GET("api_category.php")
    suspend fun getTriviaCategories(
    ): TriviaCategoryResponseBody


    /**
     *
     */
    @GET("api.php")
    suspend fun getTriviaQuestionsFlow(
        @QueryMap options: Map<String, String>
    ): TriviaResponseBody
}