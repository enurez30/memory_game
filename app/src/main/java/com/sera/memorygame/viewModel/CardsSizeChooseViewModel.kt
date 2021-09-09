package com.sera.memorygame.viewModel

import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.providers.ResourcesProvider
import org.json.JSONObject
import javax.inject.Inject

class CardsSizeChooseViewModel @Inject constructor(private val resourcesProvider: ResourcesProvider) : ViewModel() {

    /**
     *
     */
    fun getList(jsonReference: String): ArrayList<IObject> {
//        val json = Utils.loadJSONFromAsset(context = context, jsonReference)!!
        val json = resourcesProvider.getJsonByReference(reference = jsonReference) ?: return ArrayList()
        return ArrayList<IObject>().apply {
            val list = getSizeObjects(json = json)
            if (list.isNotEmpty()) {
                this.addAll(list)
            }
        }
    }


    /**
     *
     */
    private fun getSizeObjects(json: JSONObject): ArrayList<SizeViewObject> {
        return ArrayList<SizeViewObject>().apply {

            val size = with(resourcesProvider) {
                (json.getString("type").getSizeByDirectoryReference) * 2
            }

            resourcesProvider.loadJSONFromAsset("game_size")?.getJSONArray("collection")
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