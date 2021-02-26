package com.sera.memorygame.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sera.memorygame.model.IObject
import com.sera.memorygame.model.SizeViewObject
import com.sera.memorygame.utils.Utils
import org.json.JSONObject

class CardsSizeChooseViewModel(private val context: Context, private val jsonReference: String) :
    ViewModel() {

    /**
     *
     */
    fun getList(): ArrayList<IObject> {
        val json = Utils.loadJSONFromAsset(context = context, jsonReference)!!

        return ArrayList<IObject>().apply {
            val list = getSizeObjects(json = json)
            if (list.isNotEmpty()) {
//                this.add(TitleIconObject(iconName = "", title = json.getString("title")))
                this.addAll(list)
            }

        }
    }

    /**
     *
     */
    private fun getSizeObjects(json: JSONObject): ArrayList<SizeViewObject> {
        return ArrayList<SizeViewObject>().apply {
//            val jsonSize = (json.getJSONArray("images").length() * 2)
            val size = Utils.getSizeByDirectoryReference(
                context = context,
                dirRef = json.getString("type")
            )
            Utils.loadJSONFromAsset(context = context, "game_size")?.getJSONArray("collection")
                ?.let { arr ->
                    repeat(arr.length()) {
                        val obj = arr[it] as JSONObject
                        if (obj.getString("size").toInt() <= size) {
                            // create object
                            this.add(
                                SizeViewObject(
                                    title = "",
                                    xAxis = obj.getString("xAxis").toInt(),
                                    yAxis = obj.getString("yAxis").toInt(),
                                    size = obj.getString("size").toInt()
                                )
                            )
                        }
                    }
                }
        }
    }
}