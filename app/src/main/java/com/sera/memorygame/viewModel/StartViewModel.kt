package com.sera.memorygame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.database.repository.HistoryRepository
import com.sera.memorygame.database.repository.TriviaRepository
import com.sera.memorygame.utils.Constants
import javax.inject.Inject

class StartViewModel @Inject constructor(private val historyRepository: HistoryRepository,private val triviaRepository: TriviaRepository) : ViewModel() {

    /**
     *
     */
    val hQuiz: MutableLiveData<HistoryEntity?> by lazy {
        MutableLiveData(null)
    }

    /**
     *
     */
    val hTrivia: MutableLiveData<HistoryEntity?> by lazy {
        MutableLiveData(null)
    }

    /**
     *
     */
    fun getMediator(): LiveData<Pair<String, Boolean>> {
        return MediatorLiveData<Pair<String, Boolean>>().apply {
            this.addSource(historyRepository.getHistoryByTypeLive(type = Constants.HISTORY_QUIZ_GAME_TYPE)) {
                hQuiz.value = it
                this.value = Pair(Constants.HISTORY_QUIZ_GAME_TYPE, it != null)
            }
            this.addSource(historyRepository.getHistoryByTypeLive(type = Constants.HISTORY_TRIVIA_GAME_TYPE)) {
                hTrivia.value = it
                this.value = Pair(Constants.HISTORY_TRIVIA_GAME_TYPE, it != null)
            }
        }
    }

    /**
     *
     */
    suspend fun deleteHistory(obj: HistoryEntity) {
        historyRepository.delete(historyEntity = obj)
        when (obj.type) {
            Constants.HISTORY_TRIVIA_GAME_TYPE -> {
                triviaRepository.deleteAll()
            }
        }
    }
}