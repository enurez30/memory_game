package com.sera.memorygame.ui.trivia

import androidx.lifecycle.*
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.database.model.TriviaResponseBody
import com.sera.memorygame.database.repository.HistoryRepository
import com.sera.memorygame.database.repository.TriviaRepository
import com.sera.memorygame.network.IService
import com.sera.memorygame.network.RetrofitClient
import com.sera.memorygame.network.mapping.TriviaNetworkMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


class TriviaViewModel @Inject constructor(val historyRepository: HistoryRepository, val triviaRepository: TriviaRepository) : ViewModel() {

//    /**
//     *
//     */
//    var history = MutableLiveData<HistoryEntity?>().apply {
//        viewModelScope.launch(Dispatchers.Main) {
//            value = historyRepository.getHistoryEntityByType(Constants.HISTORY_TRIVIA_GAME_TYPE)
//        }
//    }

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
    suspend fun getMediator(): LiveData<ArrayList<TriviaEntity>> {
        return MediatorLiveData<ArrayList<TriviaEntity>>().apply {
            this.addSource(triviaRepository.getAllTriviaObjectsLive()) {
                triviaObjects.value=it
                this.value=it
            }
        }
    }

    /**
     *
     */
    fun getTriviaObjects() {
        val api = RetrofitClient.getRetrofitInstance(baseUrl = "https://opentdb.com/", timeout = 10L).create(IService::class.java)
        CompositeDisposable().add(
            api.getTriviaQuestions(amount = 10)
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { trivia ->
                    println()
                    persistTriviaObjects(item = trivia)
                }

        )

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
}