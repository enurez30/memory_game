package com.sera.memorygame.network

import com.sera.memorygame.network.model.TriviaCategoryResponseBody
import com.sera.memorygame.network.model.TriviaResponseBody
import retrofit2.http.QueryMap

interface IServiceHelper {

    /**
     *
     */
    suspend fun getTriviaCategories(): TriviaCategoryResponseBody


    /**
     *
     */
    suspend fun getTriviaQuestionsFlow(@QueryMap options: Map<String, String>): TriviaResponseBody

}