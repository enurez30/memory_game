package com.sera.memorygame.viewModel

import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.model.GameThemeObject
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.di.ActivityScope
import com.sera.memorygame.providers.ResourcesProvider
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ActivityScope
class GameThemeViewModel @Inject constructor ( private val resourcesProvider: ResourcesProvider) : ViewModel() {

    /**
     *
     */
    fun getList(): ArrayList<IObject> {
        return ArrayList<GameThemeObject>().apply {
            resourcesProvider.loadJSONFromAsset(jsonFile = "memory_game_themes")?.let {
                val collection = it.getJSONArray("collection")
                repeat(collection.length()) { counter ->
                    val obj = collection[counter] as JSONObject
                    this.add(
                        GameThemeObject(
                            title = obj.optString("title", ""),
                            category = obj.optString("category", ""),
                            iconReference = obj.optString("icon", ""),
                            jsonReference = obj.optString("json_ref", "")
                        )
                    )
                }
            }
        }.groupByTo(LinkedHashMap<String, MutableList<GameThemeObject>>(), { it.category }, { it }).run {
            ArrayList<IObject>().also { result ->
                this.map {
                    result.addAll(it.value)
                }
            }
        }
    }
}