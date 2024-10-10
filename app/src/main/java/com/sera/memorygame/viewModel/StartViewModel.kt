package com.sera.memorygame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.database.repository.HistoryRepository
import com.sera.memorygame.database.repository.TriviaRepository
import com.sera.memorygame.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

interface StartViewModelInterface {
    val indicateorState: StateFlow<HistoryIndicator?>
    fun isIndicatorOn(type: String): Boolean
    suspend fun deleteHistory(he: HistoryEntity)
}

@HiltViewModel
class StartViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val triviaRepository: TriviaRepository,
) : ViewModel(), StartViewModelInterface {

    private val _indicateorState: MutableStateFlow<HistoryIndicator?> = MutableStateFlow(null)
    override val indicateorState = _indicateorState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    override fun isIndicatorOn(type: String): Boolean {
        return when (type) {
            Constants.HISTORY_QUIZ_GAME_TYPE -> {
                (_indicateorState.value as? HistoryIndicator.QuizResult)?.isVisible ?: false
            }
            Constants.HISTORY_TRIVIA_GAME_TYPE -> {
                (_indicateorState.value as? HistoryIndicator.TriviaResult)?.isVisible ?: false
            }
            else -> false
        }
    }


    init {
        viewModelScope.launch {
            getData()
        }
    }
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


//    /**
//     *
//     */
//    val hQuizState: MutableStateFlow<HistoryEntity?> by lazy {
//        MutableStateFlow(null)
//    }
//
//    /**
//     *
//     */
//    val hTriviaState: MutableStateFlow<HistoryEntity?> by lazy {
//        MutableStateFlow(null)
//    }


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


    private suspend fun getData() {
        val historyObject = historyRepository.getHistoryEntityByType(Constants.HISTORY_QUIZ_GAME_TYPE)
//        hQuizState.value = historyObject
//        val historyFlow = historyRepository.getHistoryByTypeFlow(type = Constants.HISTORY_QUIZ_GAME_TYPE).map {
//            hQuizState.value = it
//            HistoryIndicator.IndicatorResult(type = Constants.HISTORY_QUIZ_GAME_TYPE, isVisible = it != null)
//        }

        _indicateorState.update { HistoryIndicator.QuizResult(isVisible = historyObject != null) }
        val triviaFlow = historyRepository.getHistoryEntityByType(type = Constants.HISTORY_TRIVIA_GAME_TYPE)
//        hTriviaState.value = triviaFlow
//        val triviaFlow = historyRepository.getHistoryByTypeFlow(type = Constants.HISTORY_TRIVIA_GAME_TYPE).map {
//            hTriviaState.value = it
//            HistoryIndicator.IndicatorResult(type = Constants.HISTORY_TRIVIA_GAME_TYPE, isVisible = it != null)
//        }
        _indicateorState.update { HistoryIndicator.TriviaResult(isVisible = triviaFlow != null) }
    }

    /**
     *
     */
    override suspend fun deleteHistory(he: HistoryEntity) {
        historyRepository.delete(historyEntity = he)
        when (he.type) {
            Constants.HISTORY_TRIVIA_GAME_TYPE -> {
                triviaRepository.deleteAll()
            }
        }
    }


}

sealed class HistoryIndicator {
    class QuizResult(val isVisible: Boolean) : HistoryIndicator()
    class TriviaResult(val isVisible: Boolean) : HistoryIndicator()
}