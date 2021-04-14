package com.sera.memorygame.viewModel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sera.memorygame.R
import com.sera.memorygame.custom.MemoryCardView
import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.providers.ResourcesProvider
import kotlin.random.Random

class MemoryViewModel(
    private val jsonRef: String,
    private val sizeViewObject: SizeViewObject,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    /**
     *
     */
    private var pair = MutableLiveData<Pair<MemoryCardView?, MemoryCardView?>>().apply {
        value = Pair(null, null)
    }

    /**
     *
     */
    var memoryListValue = MutableLiveData<Int>()

    /**
     *
     */
    private var controlValue = MutableLiveData<Int?>().apply {
        value = null
    }

    /**
     *
     */
    var getControlValue: MutableLiveData<Int?>
        get() = controlValue
        set(value) {
            controlValue = value
        }

    /**
     *
     */
    var updatePair: MutableLiveData<Pair<MemoryCardView?, MemoryCardView?>>
        get() = pair
        set(value) {
            pair = value
        }


    /**
     *
     */
    fun generateMemoryViewObject(context: Context, memoryObjeList: ArrayList<MemoryViewObject>, handlers: Handlers): ArrayList<MemoryViewObject> {
        return ArrayList<MemoryViewObject>().apply {
            memoryObjeList.map {
                val card = MemoryCardView(mContext = context, memoryViewObject = it, callback = handlers)
                it.memoryView = card
                this.add(it)
            }
        }.also {
            it.shuffle()
        }
    }

    /**
     *
     */
    fun generateMemoryObjects(containerW: Int, containerH: Int): ArrayList<MemoryViewObject> {

        val width = ((containerW / (sizeViewObject.xAxis)) - (4 * 4)).toFloat()
        val height = ((containerH / (sizeViewObject.yAxis)) - (4 * 4)).toFloat()


        return ArrayList<MemoryViewObject>().apply {
            generateList().let { list ->

                memoryListValue.value = list.size
                var counter = 0
                val breakPoint = (list.size / 2) - 1
                list.mapIndexed { index, imgRef ->
                    this.add(
                        MemoryViewObject(
                            id = counter,
                            width = width,
                            height = height,
                            frontResource = resourcesProvider.getDrawableFromAssets(
                                reference = imgRef,
                                dirRef = jsonRef
                            ),
                            backResource = getBackCardImgReferenceByJson() ?: kotlin.run {
                                R.drawable.bckg_squires
                            },
                        )
                    )
                    if (index == breakPoint) {
                        counter = 0
                    } else {
                        counter++
                    }
                }
            }
        }
    }

    /**
     *
     */
    private fun generateList(): ArrayList<String> {
        return ArrayList<String>().apply {
            val imgList = getImagesObjects()
            this.addAll(imgList)
            this.addAll(imgList)
        }
    }

    /**
     *
     */
    private fun getImagesObjects(): ArrayList<String> {
        return resourcesProvider.getJsonByReference(reference = jsonRef)?.let { json ->
            val listArray = resourcesProvider.getImagesArrayByDirReference(
                dirRef = json.getString("type")
            )
            listArray.find { it.contains("cartoon") }?.let { element ->
                listArray.remove(element)
            }
            ArrayList<String>().apply {
                repeat(sizeViewObject.size / 2) {
                    // Pick a random between 0 and the total
                    val random = Random.nextInt(listArray.size)
                    val entry = listArray[random]
                    this.add(entry)
                    listArray.remove(entry)
                }
            }
        } ?: kotlin.run {
            ArrayList()
        }
    }

    /**
     *
     */
    private fun getBackCardImgReferenceByJson(): Drawable? {
        return resourcesProvider.getJsonByReference(reference = jsonRef)?.let { json ->
            resourcesProvider.getDrawableFromAssets(dirRef = "card_back", reference = json.optString("back_card"))
        } ?: kotlin.run {
            null
        }
    }
}