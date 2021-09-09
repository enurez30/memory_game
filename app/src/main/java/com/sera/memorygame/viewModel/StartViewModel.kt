package com.sera.memorygame.viewModel

import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.database.repository.HistoryRepository
import com.sera.memorygame.database.repository.TriviaRepository
import com.sera.memorygame.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class StartViewModel @Inject constructor(private val historyRepository: HistoryRepository, private val triviaRepository: TriviaRepository) : ViewModel() {

//    /**
//     *
//     */
//    val hQuiz: MutableLiveData<HistoryEntity?> by lazy {
//        MutableLiveData(null)
//    }
//
//    /**
//     *
//     */
//    val hTrivia: MutableLiveData<HistoryEntity?> by lazy {
//        MutableLiveData(null)
//    }


    /**
     *
     */
    val hQuizState: MutableStateFlow<HistoryEntity?> by lazy {
        MutableStateFlow(null)
    }

    /**
     *
     */
    val hTriviaState: MutableStateFlow<HistoryEntity?> by lazy {
        MutableStateFlow(null)
    }


//    /**
//     *
//     */
//    fun getMediator(): LiveData<HistoryIndicator.IndicatorResult> {
//        return MediatorLiveData<HistoryIndicator.IndicatorResult>().apply {
//            this.addSource(historyRepository.getHistoryByTypeFlow(type = Constants.HISTORY_QUIZ_GAME_TYPE).asLiveData()) {
//                hQuiz.value = it
//                this.value = HistoryIndicator.IndicatorResult(type = Constants.HISTORY_QUIZ_GAME_TYPE, isVisible = it != null)
//            }
//            this.addSource(historyRepository.getHistoryByTypeFlow(type = Constants.HISTORY_TRIVIA_GAME_TYPE).asLiveData()) {
//                hTrivia.value = it
//                this.value = HistoryIndicator.IndicatorResult(type = Constants.HISTORY_TRIVIA_GAME_TYPE, isVisible = it != null)
//            }
//        }
//    }


    @ExperimentalCoroutinesApi
    fun getData(): Flow<HistoryIndicator.IndicatorResult> {
        val historyFlow = historyRepository.getHistoryByTypeFlow(type = Constants.HISTORY_QUIZ_GAME_TYPE).map {
            hQuizState.value = it
            HistoryIndicator.IndicatorResult(type = Constants.HISTORY_QUIZ_GAME_TYPE, isVisible = it != null)
        }
        val triviaFlow = historyRepository.getHistoryByTypeFlow(type = Constants.HISTORY_TRIVIA_GAME_TYPE).map {
            hTriviaState.value = it
            HistoryIndicator.IndicatorResult(type = Constants.HISTORY_TRIVIA_GAME_TYPE, isVisible = it != null)
        }
        return merge(historyFlow, triviaFlow)
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

sealed class HistoryIndicator {
    class IndicatorResult(val type: String, val isVisible: Boolean) : HistoryIndicator()
}