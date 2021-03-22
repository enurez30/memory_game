package com.sera.memorygame.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sera.memorygame.database.entity.CountryEntity
import com.sera.memorygame.database.model.FlagQuizMainObject
import com.sera.memorygame.database.model.FlagQuizSingleObject
import com.sera.memorygame.database.repository.CountryRepository
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.random.Random

class CountryViewModel @Inject constructor(private val repo: CountryRepository, private val resourcesProvider: ResourcesProvider) : ViewModel() {

    init {
        println("CountryViewModel: init called")
        viewModelScope.launch(Dispatchers.Default) {
            checkCountries()
        }
    }

    private var remainCountriesLive = MutableLiveData<List<CountryEntity>>().apply {
        value = ArrayList()
    }

    var getRemainCountriesLive: MutableLiveData<List<CountryEntity>>
        get() = remainCountriesLive
        set(value) {
            remainCountriesLive = value
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
    fun getAllCountries(): StateFlow<List<CountryEntity>?> = allCountries

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
    private suspend fun checkCountries() {
        repo.getAllCountries().collect {
            println("CountryViewModel: it = ${it.size}")
            if (it.isEmpty()) {
                viewModelScope.launch(Dispatchers.Default) {
                    populateDB()
                }
            } else {
                allCountries.value = it
                viewModelScope.launch {
                    getRemainCountriesLive.value = repo.getAllCountriesOnly()
                }
            }
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