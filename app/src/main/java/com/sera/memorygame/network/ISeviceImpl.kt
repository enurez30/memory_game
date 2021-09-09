package com.sera.memorygame.network

import com.sera.memorygame.network.model.TriviaCategoryResponseBody
import javax.inject.Inject

class ISeviceImpl @Inject constructor(private val iService: IService) : IServiceHelper {

    /**
     *
     */
    override suspend fun getTriviaCategories(): TriviaCategoryResponseBody =iService.getTriviaCategories()

    /**
     *
     */
    override suspend fun getTriviaQuestionsFlow(options: Map<String, String>)=iService.getTriviaQuestionsFlow(options = options)

}