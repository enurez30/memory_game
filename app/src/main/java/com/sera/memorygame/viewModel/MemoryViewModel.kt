package com.sera.memorygame.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sera.memorygame.R
import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.utils.Utils
import com.sera.memorygame.view.MemoryCardView
import kotlin.random.Random

class MemoryViewModel(
    private val context: Context,
    private val jsonRef: String,
    private val sizeViewObject: SizeViewObject
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

//    /**
//     *
//     */
//    fun getLinearContainer(): LinearLayout {
//        return LinearLayout(context).apply {
//            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            orientation = LinearLayout.HORIZONTAL
//        }
//    }

    /**
     *
     */
    fun generateMemoryViewObject(
        memoryObjeList: ArrayList<MemoryViewObject>,
        handlers: Handlers
    ): ArrayList<MemoryViewObject> {
        return ArrayList<MemoryViewObject>().apply {
            memoryObjeList.map {
                val card =
                    MemoryCardView(mContext = context, memoryViewObject = it, callback = handlers)
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
//        val withInDp = Utils.pxToDp(resource = context.resources, px = Resources.getSystem().displayMetrics.widthPixels.toFloat())
//        val heightInDp = Utils.pxToDp(resource = context.resources, px = Resources.getSystem().displayMetrics.heightPixels.toFloat())
//        val width = ((containerW / (4)) - (4 * 4)).toFloat()
//        val height = ((containerH / (6)) - (4 * 4)).toFloat()

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
                            frontResource = Utils.getDrawableFromAssets(
                                context = context,
                                reference = imgRef,
                                dirRef = jsonRef
                            ),
                            backResource = R.drawable.bckg_squires,
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

//        val json = Utils.getJsonByReference(context = context, reference = jsonRef) ?: return ArrayList()

        return Utils.getJsonByReference(context = context, reference = jsonRef)?.let { json ->
            val listArray = Utils.getImagesArrayByDirReference(
                context = context,
                dirRef = json.getString("type")
            )
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
}