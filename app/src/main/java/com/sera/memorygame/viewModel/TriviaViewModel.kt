package com.sera.memorygame.viewModel

import androidx.lifecycle.*
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.database.repository.HistoryRepository
import com.sera.memorygame.database.repository.TriviaRepository
import com.sera.memorygame.extentions.toTriviaEntity
import com.sera.memorygame.network.model.TriviaCategoryModel
import com.sera.memorygame.network.model.TriviaCategoryResponseBody
import com.sera.memorygame.network.model.TriviaResponseBody
import com.sera.memorygame.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


class TriviaViewModel @Inject constructor(private val historyRepository: HistoryRepository, private val triviaRepository: TriviaRepository) : ViewModel() {

    /**
     *
     */
    private var historyObj = MutableLiveData<HistoryEntity?>().apply {
        value = null
    }

    /**
     *
     */
    private var getHistoryObj: MutableLiveData<HistoryEntity?>
        get() = historyObj
        set(value) {
            historyObj = value
        }

    /**
     *
     */
    private var triviaCategories = MutableLiveData<ArrayList<TriviaCategoryModel>?>().apply {
        value = null
    }

    /**
     *
     */
    var getTriviaCategories: MutableLiveData<ArrayList<TriviaCategoryModel>?>
        get() = triviaCategories
        set(value) {
            triviaCategories = value
        }

    /**
     *
     */
    private var triviaObjects = MutableLiveData<List<TriviaEntity>?>().apply {
        value = null
    }

    /**
     *
     */
    var getTriviaObjects: MutableLiveData<List<TriviaEntity>?>
        get() = triviaObjects
        set(value) {
            triviaObjects = value
        }


    /**
     *
     */
    @ExperimentalCoroutinesApi
    fun getData(): Flow<TriviaUiState> {
        val trivia = triviaRepository.getTriviaNotInRangeFlow(list = getHistoryObj.value?.ids ?: listOf("")).map {
            triviaObjects.value = it
            TriviaUiState.Trivia(list = it)
        }
        val history = historyRepository.getHistoryByTypeFlow(type = Constants.HISTORY_TRIVIA_GAME_TYPE).map {
            getHistoryObj.value = it
            TriviaUiState.History(entity = it)
        }
        return merge(trivia, history)
    }

    /**
     *
     */
    fun checkHistory() {
        if (getHistoryObj.value == null) {
            historyRepository.getEmptyHistoryEntity().apply {
                this.type = Constants.HISTORY_TRIVIA_GAME_TYPE
                this.isAlive = true
            }.run {
                viewModelScope.launch {
                    historyRepository.persistHistoryEntity(historyEntity = this@run)
                    getHistoryObj.value = this@run
                }
            }
        }
    }

    /**
     *
     */
    private fun persistTriviaObjects(item: TriviaResponseBody) {
        val result = item.result.toTriviaEntity()
        viewModelScope.launch {
            triviaRepository.insertMultiple(list = result)
            getTriviaObjects.value = result
        }
    }

    /**
     *
     */
    fun getTriviaObject(): TriviaEntity {
        return getTriviaObjects.value?.get(Random.nextInt(getTriviaObjects.value?.size ?: 0)) ?: TriviaEntity()
    }

    /**
     *
     */
    suspend fun deleteObject(obj: TriviaEntity) {
        triviaRepository.deleteSingleObject(triviaEntity = obj)
    }


    /**
     *
     */
    private fun parseCategoryReslut(result: TriviaCategoryResponseBody) {
        val list = result.array as ArrayList<TriviaCategoryModel>
        list.sortBy { it.name }
        list.add(0, TriviaCategoryModel(id = -1, "Any Category"))
        getTriviaCategories.value = list
    }

    /**
     *
     */
    fun buildRequest(amount: Int, category: Int, diff: String): HashMap<String, String> {
        return HashMap<String, String>().apply {
            this["amount"] = amount.toString()
            if (category != -1) {
                this["category"] = category.toString()
            }
            if (!diff.toLowerCase(Locale.getDefault()).contains("any")) {
                this["difficulty"] = diff.toLowerCase(Locale.getDefault())
            }
        }
    }

    /**
     *
     */
    suspend fun getTriviaCategories() {
        flow {
            emit(triviaRepository.getTriviaCategories())
        }.flowOn(Dispatchers.IO)
            .catch { error ->
                println("Error: $error")
            }
            .collect { categories ->
                parseCategoryReslut(result = categories)
            }

    }

    /**
     *
     */
    suspend fun getTriviaQuestions(map: HashMap<String, String>) {

        flow {
            emit(triviaRepository.getTriviaQuestionsFlow(options = map))
        }.flowOn(Dispatchers.IO)
            .catch { error ->
                println("Error: $error")
            }
            .collect { trivia ->
                persistTriviaObjects(item = trivia)
            }

    }

    /**
     *
     */
    fun updateTriviaScore(value: String, id: String) {
        getHistoryObj.value?.score?.let { score ->
            if (value == "correct") {
                score.correct += 1
            } else {
                score.wrong += 1
            }
        }
        getHistoryObj.value?.let {
            (it.ids as? ArrayList<String>)?.add(id)
        }
        viewModelScope.launch {
            getHistoryObj.value?.let { historyRepository.updateHistoryEntity(historyEntity = it) }
        }
    }

    /**
     *
     */
    fun updateHistoryTotal(total: Int) {
        getHistoryObj.value?.score?.let { score ->
            if (score.total == 0) {
                score.total = total
                viewModelScope.launch(Dispatchers.IO) {
                    getHistoryObj.value?.let { historyRepository.updateHistoryEntity(historyEntity = it) }
                }
            }
        }
    }

    /**
     *
     */
    fun resetHistory() {
        getHistoryObj.value?.let { he ->
            viewModelScope.launch {
                historyRepository.delete(historyEntity = he)
                println()
            }
        } ?: kotlin.run {
            viewModelScope.launch {
                historyRepository.getHistoryEntityByType(type = Constants.HISTORY_TRIVIA_GAME_TYPE)?.let {
                    historyRepository.delete(historyEntity = it)
                }
            }
        }
    }
}

sealed class TriviaUiState {
    data class Trivia(val list: List<*>) : TriviaUiState()
    data class History(val entity: HistoryEntity?) : TriviaUiState()
}