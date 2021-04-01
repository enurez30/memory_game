package com.sera.memorygame.network.mapping

import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.database.model.TriviaObjectModel
import com.sera.memorygame.interfaces.EntityMapper

class TriviaNetworkMapper : EntityMapper<TriviaEntity, TriviaObjectModel> {

    /**
     *
     */
    override fun mapFromEntity(entity: TriviaEntity): TriviaObjectModel {
        return TriviaObjectModel()
    }

    /**
     *
     */
    override fun mapToEntity(domainModel: TriviaObjectModel): TriviaEntity {
        return TriviaEntity().apply {
            this.category = domainModel.category ?: ""
            this.correctAnswer = domainModel.correctAnswer ?: ""
            this.difficulty = domainModel.difficulty ?: ""
            this.incorrectAnswers = domainModel.answersList ?: ArrayList()
            this.question = domainModel.question ?: ""
            this.type = domainModel.type ?: ""
        }
    }

}