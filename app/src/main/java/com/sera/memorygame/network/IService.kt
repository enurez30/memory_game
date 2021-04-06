package com.sera.memorygame.network

import com.sera.memorygame.network.model.TriviaCategoryResponseBody
import com.sera.memorygame.network.model.TriviaResponseBody
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IService {

    /**
     *
     */
    @GET("api.php")
    fun getTriviaQuestions(
        @QueryMap options: Map<String, String>
    ): Observable<TriviaResponseBody>

    /**
     *
     */
    @GET("api_category.php")
    fun getTriviaCategories(
    ): Observable<TriviaCategoryResponseBody>
}