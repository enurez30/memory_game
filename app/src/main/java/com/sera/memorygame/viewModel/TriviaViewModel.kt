package com.sera.memorygame.viewModel

import androidx.lifecycle.*
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.database.repository.HistoryRepository
import com.sera.memorygame.database.repository.TriviaRepository
import com.sera.memorygame.network.IService
import com.sera.memorygame.network.RetrofitClient
import com.sera.memorygame.network.mapping.TriviaNetworkMapper
import com.sera.memorygame.network.model.TriviaCategoryModel
import com.sera.memorygame.network.model.TriviaCategoryResponseBody
import com.sera.memorygame.network.model.TriviaResponseBody
import com.sera.memorygame.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
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
    private var triviaObjects = MutableLiveData<ArrayList<TriviaEntity>?>().apply {
        value = null
    }

    /**
     *
     */
    var getTriviaObjects: MutableLiveData<ArrayList<TriviaEntity>?>
        get() = triviaObjects
        set(value) {
            triviaObjects = value
        }


    /**
     *
     */
    suspend fun getMediator(): LiveData<Pair<String, Any?>> {
        return MediatorLiveData<Pair<String, Any?>>().apply {
            this.addSource(triviaRepository.getTriviaNotInRangeLive(getHistoryObj.value?.ids ?: listOf(""))) {
                triviaObjects.value = it as ArrayList<TriviaEntity>?
                this.value = Pair("trivia", it)
            }
            this.addSource(historyRepository.getHistoryByTypeLive(type = Constants.HISTORY_TRIVIA_GAME_TYPE)) {
                getHistoryObj.value = it
                this.value = Pair("history", it)
            }
        }
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
        ArrayList<TriviaEntity>().apply {
            item.result.map {
                this.add(TriviaNetworkMapper().mapToEntity(domainModel = it))
            }
        }.run {
            viewModelScope.launch {
                triviaRepository.insertMultiple(list = this@run)
                getTriviaObjects.value = this@run
            }
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
    fun getTriviaCategories() {
        val api = RetrofitClient.getRetrofitInstance(baseUrl = Constants.TRIVIA_BASE_URL, timeout = 10L).create(IService::class.java)
        CompositeDisposable().add(
            api.getTriviaCategories()
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    println("Error: $it")
                }
                .subscribe({ categories ->
                    println()
                    parseCategoryReslut(result = categories)
                }, {
                    println("Error: $it")
                })

        )
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
    fun getTriviaQuestions(map: HashMap<String, String>) {
        val api = RetrofitClient.getRetrofitInstance(baseUrl = Constants.TRIVIA_BASE_URL, timeout = 10L).create(IService::class.java)
        CompositeDisposable().add(
            api.getTriviaQuestions(options = map)
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    println("Error: $it")
                }
                .subscribe({ trivia ->
                    println()
                    persistTriviaObjects(item = trivia)
                }, {
                    println("Error: $it")
                })

        )
    }

    /**
     *
     */
    fun normalizeText(value: String): String {
        return value.replace("&quot;", "\"").replace("&#039;", "'")
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
                viewModelScope.launch {
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