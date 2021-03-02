package com.sera.memorygame.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.model.GameThemeObject
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.utils.Utils
import org.json.JSONObject

class GameThemeViewModel(private val context: Context) : ViewModel() {

    /**
     *
     */
    fun getList(): ArrayList<IObject> {
        return ArrayList<IObject>().apply {
            Utils.loadJSONFromAsset(context = context, jsonFile = "memory_game_themes")?.let {
                val collection = it.getJSONArray("collection")
                repeat(collection.length()) { counter ->
                    val obj = collection[counter] as JSONObject
                    this.add(
                        GameThemeObject(
                            title = obj.optString("title", ""),
                            iconReference = obj.optString("icon", ""),
                            jsonReference = obj.optString("json_ref", "")
                        )
                    )
                }
            }
        }
    }
}