package com.sera.memorygame.network

import com.sera.memorygame.database.model.TriviaResponseBody
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IService {

    /**
     *
     */
    @GET("api.php")
    fun getTriviaQuestions(
        @Query("amount") amount: Int
    ): Observable<TriviaResponseBody>

}