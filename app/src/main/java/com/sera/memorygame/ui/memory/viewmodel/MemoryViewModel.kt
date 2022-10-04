package com.sera.memorygame.ui.memory.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sera.memorygame.R
import com.sera.memorygame.custom.MemoryCardView
import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.ui.memory.MemoryCardAnimationState
import com.sera.memorygame.ui.memory.MemoryUiState
import com.sera.memorygame.ui.memory.data.MemoryMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
) : ViewModel() {

    private var clickInProgress: AtomicBoolean = AtomicBoolean(false)

    private val _containerW = MutableStateFlow(0)
    private val _containerH = MutableStateFlow(0)
    private val _matchdPairValue = MutableStateFlow(0)
    private val _sizeViewObject = MutableStateFlow<SizeViewObject?>(null)
    private val _jsonRef = MutableStateFlow("")
    private val _cardsList = MutableStateFlow<List<MemoryViewObject>>(listOf())
    private val _matchPair = MutableStateFlow<Pair<MemoryMatch?, MemoryMatch?>>(Pair(null, null))
    private val _memoryCardAnimationState = MutableStateFlow(MemoryCardAnimationState.ANIMATION_ENDED)
    val memoryCardAnimationState = _memoryCardAnimationState.asStateFlow()

    val uiState = com.sera.memorygame.extentions.combine(
        _sizeViewObject.filterNotNull(),
        _jsonRef.filter { it.isNotEmpty() },
        _containerW.filterNot { it == 0 },
        _containerH.filterNot { it == 0 },
        _cardsList,
        _matchPair,
        _matchdPairValue
    ) { sizeObject, jsonRef, _, _, cards, matchPair, matchValue ->
        MemoryUiState(
            sizeObject = sizeObject,
            jsonRef = jsonRef,
            cards = cards,
            matchPair = matchPair,
            matchedPairsValue = matchValue
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MemoryUiState())

    private val observeDimentions = combine(
        _containerW.filterNot { it == 0 },
        _containerH.filterNot { it == 0 },
    ) { containerWidth, containerHeight ->
        _cardsList.value = generateMemoryObjects(containerH = containerHeight, containerW = containerWidth).also { it.shuffled() }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf<MemoryViewObject>())


    fun updateConteinerDimentions(width: Int, height: Int) {
        _containerW.value = width
        _containerH.value = height
    }

    /**
     *
     */
    private var pair = MutableLiveData<MemoryPair>().apply {
        value = MemoryPair(null, null)
    }

    /**
     *
     */
    fun setValues(sizeViewObject: SizeViewObject, jsonRef: String) {
        _sizeViewObject.value = sizeViewObject
        _jsonRef.value = jsonRef
    }

    /**
     *
     */
    private fun generateMemoryObjects(containerW: Int, containerH: Int): List<MemoryViewObject> {
        val width = ((containerW / (_sizeViewObject.value?.xAxis ?: 2)) - (4 * 4)).toFloat()
        val height = ((containerH / (_sizeViewObject.value?.yAxis ?: 2)) - (4 * 4)).toFloat()


        return mutableListOf<MemoryViewObject>().apply {
            val imgList = getImagesObjects()
            repeat(2) {
                imgList.mapIndexed { index, imgRef ->
                    this.add(
                        MemoryViewObject(
                            id = index,
                            width = width,
                            height = height,
                            imageReference = imgRef,
                            frontResource = resourcesProvider.getDrawableFromAssets(
                                reference = imgRef,
                                dirRef = _jsonRef.value
                            ),
                            backResource = getBackCardImgReferenceByJson(),
                            onClick = { onClickAction(view = it.first, item = it.second) },
                            onAnimationState = { onAnimationState(state = it) }
                        )
                    )
                }.toImmutableList()
            }
        }
    }

    private fun onAnimationState(state: MemoryCardAnimationState) {
        println("TEST_TEST: onAnimationState state = $state")
        _memoryCardAnimationState.update { state }
    }

    private fun onClickAction(view: MemoryCardView, item: MemoryViewObject) {
        if (clickInProgress.get()) {
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            with(_matchPair) {
                if (value.first?.item == null) {
                    update {
                        Pair(MemoryMatch(
                            item = item,
                            memoryCardView = view,
                            isShow = true
                        ), it.second)
                    }
                } else {
                    clickInProgress = AtomicBoolean(true)
                    update {
                        Pair(it.first?.copy(isShow = false),
                            MemoryMatch(
                                item = item,
                                memoryCardView = view,
                                isShow = true
                            )
                        )
                    }
                }

                delay(1500)

                if (value.first?.item != null && value.second?.item != null) {
                    if (value.first?.item?.imageReference == value.second?.item?.imageReference) {
                        // we have match
                        update { Pair(null, null) }
                        _matchdPairValue.update { it + 1 }
                    } else {
                        // we don't have a match
                        update { Pair(it.first?.copy(isHide = true, isShow = false), it.second?.copy(isHide = true, isShow = false)) }
                        update { Pair(null, null) }
                    }
                }
                clickInProgress = AtomicBoolean(false)
            }
        }
    }

    /**
     *
     */
    private fun getImagesObjects(): List<String> {
        return resourcesProvider.getJsonByReference(reference = _jsonRef.value)?.let { json ->
            val listArray = resourcesProvider.getImagesArrayByDirReference(
                dirRef = json.getString("type")
            ).toMutableList()
            listArray.find { it.contains("cartoon") }?.let { element ->
                listArray.remove(element)
            }
            mutableListOf<String>().apply {
                repeat((_sizeViewObject.value?.size ?: 4) / 2) {
                    // Pick a random between 0 and the total
                    val random = Random.nextInt(listArray.size)
                    val entry = listArray[random]
                    this.add(entry)
                    listArray.remove(entry)
                }
            }.toImmutableList()
        } ?: kotlin.run {
            listOf()
        }
    }

    /**
     *
     */
    private fun getBackCardImgReferenceByJson(): Any {
        return try {
            resourcesProvider.getJsonByReference(reference = _jsonRef.value)?.let { json ->
                resourcesProvider.getDrawableFromAssets(dirRef = "card_back", reference = json.optString("back_card"))
            } ?: kotlin.run {
                R.attr.colorSecondary
            }
        } catch (e: Exception) {
            R.drawable.bckg_squires
        }
    }

    sealed class GamePair
    class MemoryPair(val first: MemoryCardView?, val second: MemoryCardView?) : GamePair()

}