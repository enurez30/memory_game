package com.sera.memorygame.ui.flag_quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sera.memorygame.database.entity.CountryEntity
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.database.model.FlagQuizMainObject
import com.sera.memorygame.database.model.FlagQuizSingleObject
import com.sera.memorygame.database.model.ScoreObject
import com.sera.memorygame.database.repository.CountryRepository
import com.sera.memorygame.database.repository.HistoryRepository
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.ui.flag_quiz.FlagQuizUiState
import com.sera.memorygame.ui.flag_quiz.KeyEnum
import com.sera.memorygame.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val repo: CountryRepository,
    private val historyRepo: HistoryRepository,
    private val resourcesProvider: ResourcesProvider,
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            checkHistory()
        }
    }

    private val _remainingCountries = MutableStateFlow<List<CountryEntity>?>(null)

    private val _flagQuizObject = MutableStateFlow<FlagQuizMainObject?>(null)
    val flagQuizObject = _flagQuizObject.asStateFlow()

    private val _key = MutableStateFlow("")
    val key = _key.asStateFlow()

    private val _correctState = MutableStateFlow(0)
    private val _wrongState = MutableStateFlow(0)
    private val _forceExit = MutableStateFlow(false)

    val uiState = com.sera.memorygame.extentions.combine(
        _key,
        repo.getAllCountries(),
        _flagQuizObject,
        _correctState,
        _wrongState,
        _forceExit
    ) { key, allCountries, quizObj, correct, wrong, forceExit ->
        FlagQuizUiState(
            flagQuizObject = quizObj,
            correctCountries = correct,
            wrongCountries = wrong,
            totalCountries = allCountries.size,
            isGoNext = key.isNotEmpty(),
            forceExit = forceExit
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, FlagQuizUiState())

    /**
     *
     */
    private val allCountries = MutableStateFlow<List<CountryEntity>>(listOf())

    /**
     *
     */
    private suspend fun updateHistoryObject() {
        historyRepo.getHistoryEntityByType(Constants.HISTORY_QUIZ_GAME_TYPE)?.let { historyEntity ->
            val countryId = _flagQuizObject.value?.countryId ?: ""
            val correct = _correctState.value
            val wrong = _wrongState.value

            historyEntity.score = ScoreObject(total = (correct + wrong), correct = correct, wrong = wrong)
            (historyEntity.ids as? ArrayList)?.add(countryId)
            historyRepo.updateHistoryEntity(historyEntity = historyEntity)
        }
    }

    /**
     *
     */
    private suspend fun checkHistory() {
        val historyObject = historyRepo.getHistoryEntityByType(Constants.HISTORY_QUIZ_GAME_TYPE)
        val allCounteris = repo.getAllCountriesOnly()
        if (historyObject == null) {

            HistoryEntity().apply {
                this.type = Constants.HISTORY_QUIZ_GAME_TYPE
            }.run {
                historyRepo.persistHistoryEntity(historyEntity = this)
            }
            if (allCounteris.isEmpty()) {
                populateDB()
            } else {
                _remainingCountries.value = allCounteris
                allCountries.value = allCounteris
            }
        } else {
            populateRemainCountriesFromHistoryObject(historyObject = historyObject)
        }
        getQuizObject().launchIn(viewModelScope)
    }

    /**
     *
     */
    private suspend fun populateRemainCountriesFromHistoryObject(historyObject: HistoryEntity) {
        allCountries.value = repo.getAllCountriesOnly()
        _remainingCountries.value = repo.getCountirsNotInRange(list = historyObject.ids)
        _correctState.value = historyObject.score.correct
        _wrongState.value = historyObject.score.wrong
    }

    /**
     *
     */
    private suspend fun populateDB() {
        resourcesProvider.getStreamFromRaw(fName = Constants.COUNTRY_CODES_FILE_NAME).let { ins ->
            val result = ArrayList<CountryEntity>()
            JSONObject(StringBuilder().apply {
                ins.reader().readLines().map {
                    this.append(it)
                }
            }.toString()).run {
                this.keys().forEach { key ->
                    val name = this[key] as String
                    repo.getEmptyCountryEntity().apply {
                        this.code = key
                        this.imageReference = key
                        this.name = name
                        this.nameReference = key
                    }.run {
                        result.add(this)
                    }
                }
            }
            println("CountryViewModel: DONE (updating flow and DB)")
            repo.insertMultipleContryEntities(arr = result)
        }
    }


    private fun getQuizObject() =
        _remainingCountries
            .filterNotNull()
            .filter { it.isNotEmpty() }
            .onEach {
                println("TEST_TEST: getQuizObject() size = ${it.size}")
                val country = getRandomCountry()
                _flagQuizObject.value = FlagQuizMainObject(
                    countryId = country.id,
                    flagReference = country.imageReference,
                    options = getRandomAnswersByCountry(countryEntity = country)
                )
            }


    /**
     *
     */
    private fun getResult(key: String) {
        _key.value = key
        when (key) {
            KeyEnum.CORRECT.value -> {
                _correctState.update { it + 1 }
            }
            KeyEnum.WRONG.value -> {
                _wrongState.update { it + 1 }
            }
        }
        viewModelScope.launch {
            updateHistoryObject()
        }
    }

    fun nextQuiz() {
        (_remainingCountries.value as ArrayList<CountryEntity>).removeIf { it.id == (_flagQuizObject.value?.countryId ?: "") }
        if ((_remainingCountries.value as ArrayList<CountryEntity>).isNotEmpty()) {
            _key.update { KeyEnum.DEFAULT.value }
            getQuizObject().launchIn(viewModelScope)
        } else {
            _forceExit.update { true }
        }
    }

    fun getCountryName() = _flagQuizObject.value?.options?.firstOrNull { it.isRight }?.flagName ?: ""

    /**
     *
     */
    private fun getRandomCountry(): CountryEntity {
        println("TEST_TEST: _remainingCountries.value.size = ${_remainingCountries.value!!.size}")
        val random = Random.nextInt(_remainingCountries.value!!.size)
        return _remainingCountries.value!![random]
    }

    /**
     *
     */
    private fun getRandomAnswersByCountry(countryEntity: CountryEntity): ArrayList<FlagQuizSingleObject> {

        val list = ArrayList<CountryEntity>().apply {
            allCountries.value.let { this.addAll(it) }
        }

        list.remove(countryEntity)

        return ArrayList<FlagQuizSingleObject>().apply {
            println("TEST_TEST: countryEntity = ${countryEntity.name}")
            println("TEST_TEST: countryEntity = ${countryEntity.imageReference}")
            this.add(
                FlagQuizSingleObject(
                    flagName = resourcesProvider.getString(reference = countryEntity.nameReference),
                    isRight = true,
                    onClick = { onClickAction(item = it) }
                )
            )
            repeat(3) {
                println("TEST_TEST: list.size = ${list.size}")
                val random = Random.nextInt(list.size)
                this.add(
                    FlagQuizSingleObject(
                        flagName = resourcesProvider.getString(reference = list[random].nameReference),
                        onClick = { onClickAction(item = it) }
                    )
                )
            }
        }.also {
            it.shuffle()
        }

    }


    private fun onClickAction(item: FlagQuizSingleObject) {
        val key = if (item.isRight) {
            KeyEnum.CORRECT.value
        } else {
            KeyEnum.WRONG.value
        }

        _flagQuizObject.value =
            FlagQuizMainObject(
                countryId = _flagQuizObject.value?.countryId ?: "",
                flagReference = _flagQuizObject.value?.flagReference ?: "",
                options = _flagQuizObject.value?.options?.map { it.copy(animate = true) } ?: listOf())
        getResult(key = key)
    }
}