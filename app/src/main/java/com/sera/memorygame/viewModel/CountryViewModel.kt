package com.sera.memorygame.viewModel

import androidx.lifecycle.MutableLiveData
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
import com.sera.memorygame.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import kotlin.random.Random

@ExperimentalCoroutinesApi
class CountryViewModel @Inject constructor(private val repo: CountryRepository, private val historyRepo: HistoryRepository, private val resourcesProvider: ResourcesProvider) : ViewModel() {

    init {
        println("CountryViewModel: init called")
        viewModelScope.launch(Dispatchers.Default) {
            merge(
                flow { emit(checkCountries()) },
                flow { emit(checkHistory()) }
            ).collect()
        }
    }

    private var remainCountriesLive = MutableLiveData<List<CountryEntity>>().apply {
        value = ArrayList()
    }

    var getRemainCountriesLive: MutableLiveData<List<CountryEntity>>
        get() = remainCountriesLive
        set(value) {
            println("CountryViewModel: size = ${remainCountriesLive.value?.size}")
            remainCountriesLive = value
        }

    private val historyEntity: MutableLiveData<HistoryEntity?> by lazy {
        MutableLiveData(null)
    }

    /**
     *
     */
    private val allCountries: MutableStateFlow<List<CountryEntity>?> by lazy {
        MutableStateFlow<List<CountryEntity>?>(null)
            .also {
                repo.getAllCountries()
            }
    }

    /**
     *
     */
    private val correctCountries: MutableStateFlow<Int> by lazy {
        MutableStateFlow(0)
    }

    /**
     *
     */
    private val wrongCountries: MutableStateFlow<Int> by lazy {
        MutableStateFlow(0)
    }

    /**
     *
     */
    private fun getAllCountries(): StateFlow<List<CountryEntity>?> = allCountries

    /**
     *
     */
    fun getCorrectCountries(): StateFlow<Int> = correctCountries

    /**
     *
     */
    fun getWrongCountries(): StateFlow<Int> = wrongCountries

    /**
     *
     */
    fun setCorrectValue(newValue: Int) {
        correctCountries.value = newValue
    }

    /**
     *
     */
    fun setWrongCountries(newValue: Int) {
        wrongCountries.value = newValue
    }

    /**
     *
     */
    suspend fun updateHistoryObject(countryId: String) {
        val (_, correct, wrong, total) = getScoreValues()
        historyEntity.value?.score = ScoreObject(total = total, correct = correct, wrong = wrong)
        (historyEntity.value?.ids as? ArrayList)?.add(countryId)
        historyEntity.value?.let { historyRepo.updateHistoryEntity(historyEntity = it) }
    }

    /**
     *
     */
    suspend fun getScoreValues(): List<Int> = withContext(Dispatchers.Main) {

        val totalCountries = getAllCountries().value?.size ?: 0
        val correct = getCorrectCountries().value
        val wrong = getWrongCountries().value
        val total = totalCountries - (totalCountries - (correct + wrong))

        return@withContext listOf(totalCountries, correct, wrong, total)
    }

    /**
     *
     */
    private suspend fun checkCountries() {
        repo.getAllCountries().collect {
            println("CountryViewModel: it = ${it.size}")
            if (it.isEmpty()) {
                viewModelScope.launch(Dispatchers.Default) {
                    populateDB()
                }
            } else {
                allCountries.value = it
//                viewModelScope.launch {
//                    getRemainCountriesLive.value = repo.getAllCountriesOnly()
//                }
            }
        }

    }

    /**
     *
     */
    private suspend fun checkHistory() {
        val historyObject = historyRepo.getHistoryEntityByType(Constants.HISTORY_QUIZ_GAME_TYPE)

        if (historyObject == null) {
            historyRepo.getEmptyHistoryEntity().apply {
                this.type = Constants.HISTORY_QUIZ_GAME_TYPE
            }.run {
//                viewModelScope.launch {
//
//                }
                setHistoryObject(obj = this)
                historyRepo.persistHistoryEntity(historyEntity = this)
            }
//            viewModelScope.launch {
//                getRemainCountriesLive.value = repo.getAllCountriesOnly()
//            }
        } else {
            setHistoryObject(obj = historyObject)
            populateRemainCountriesFromHistoryObject(historyObject = historyObject)
        }
    }

    /**
     *
     */
    private fun setHistoryObject(obj: HistoryEntity) {
        viewModelScope.launch(Dispatchers.Main) {
            historyEntity.value = obj
        }
    }

    /**
     *
     */
    private suspend fun populateRemainCountriesFromHistoryObject(historyObject: HistoryEntity) {
        viewModelScope.launch(Dispatchers.Main) {
            getRemainCountriesLive.value = repo.getCountirsNotInRange(list = historyObject.ids)
            setCorrectValue(newValue = historyObject.score.correct)
            setWrongCountries(newValue = historyObject.score.wrong)
        }
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
            allCountries.value = result
            viewModelScope.launch {
                getRemainCountriesLive.value = repo.getAllCountriesOnly()
            }
            repo.insertMultipleContryEntities(arr = result)
        }
    }

    fun getQuizObject(): FlagQuizMainObject {

        return getRandomCountry()?.let {
            println("FLAG_REFERENCE: ${it.imageReference}")
            FlagQuizMainObject(
                countryId = it.id,
                flagReference = it.imageReference,
                options = getRandomAnswersByCountry(countryEntity = it)
            )
        } ?: kotlin.run {
            FlagQuizMainObject(
                countryId = "",
                flagReference = "cw",
                options = ArrayList<FlagQuizSingleObject>().apply {
                    this.add(
                        FlagQuizSingleObject(
                            flagName = resourcesProvider.getString("cc")
                        )
                    )
                    this.add(
                        FlagQuizSingleObject(
                            flagName = resourcesProvider.getString("cf")
                        )
                    )
                    this.add(
                        FlagQuizSingleObject(
                            flagName = resourcesProvider.getString("cw")
                        )
                    )
                    this.add(
                        FlagQuizSingleObject(
                            flagName = resourcesProvider.getString("cz")
                        )
                    )
                }
            )
        }

    }

    /**
     *
     */
    private fun getRandomCountry(): CountryEntity? {
        val random = Random.nextInt(remainCountriesLive.value?.size ?: 0)
        return remainCountriesLive.value?.get(random)
    }

    /**
     *
     */
    private fun getRandomAnswersByCountry(countryEntity: CountryEntity): ArrayList<FlagQuizSingleObject> {

        val list = ArrayList<CountryEntity>().apply {
            allCountries.value?.let { this.addAll(it) }
        }

        list.remove(countryEntity)

        return ArrayList<FlagQuizSingleObject>().apply {
            this.add(
                FlagQuizSingleObject(
                    flagName = resourcesProvider.getString(reference = countryEntity.nameReference),
                    isRight = true
                )
            )
            repeat(3) {
                val random = Random.nextInt(list.size)
                this.add(
                    FlagQuizSingleObject(
                        flagName = resourcesProvider.getString(reference = list[random].nameReference)
                    )
                )
            }
        }.also {
            it.shuffle()
        }

    }
}