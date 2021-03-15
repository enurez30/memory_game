package com.sera.memorygame.providers

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.sera.memorygame.utils.Utils
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class ResourcesProvider @Inject constructor(private val context: Context) {

    /**
     *
     */
    fun getString(reference: String): String {
        val resId: Int = context.resources.getIdentifier(reference, "string", context.packageName)
        return context.resources.getString(resId)
    }

    /**
     *
     */
    fun getDrawableByReference(context: Context, reference: String): Drawable? {
        return AppCompatResources.getDrawable(
            context,
            Utils.getImageByReference(mContext = context, reference = reference)
        )
    }

    /**
     *
     */
    fun loadJSONFromAsset(jsonFile: String, objectName: String? = null): JSONObject? {
        val json: String?

        try {

            val reference = if (jsonFile.endsWith(".json")) {
                jsonFile
            } else {
                "$jsonFile.json"
            }

            val `is`: InputStream = Utils.getAssetsInputStream(context, reference)

            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer)
        } catch (ex: IOException) {
            // TODO: need to handle this
            ex.printStackTrace()
            return null
        }
        return if (objectName == null) {
            JSONObject(json).getJSONObject("entity")
        } else {
            JSONObject(json).getJSONObject(objectName)
        }
    }

    /**
     *
     */
    fun getJsonByReference(reference: String): JSONObject? {
        loadJSONFromAsset(jsonFile = "memory_game_themes")?.let {
            val collection = it.getJSONArray("collection")
            repeat(collection.length()) { index ->
                val obj = collection[index] as JSONObject
                if (obj.getString("json_ref") == reference) {
                    return obj
                }
            }
        }
        return null
    }


    /**
     *
     */
    fun getSizeByDirectoryReference(dirRef: String): Int {
        return try {
            val assetsPath = context.filesDir.toString() + "/assets/files/$dirRef/"
            File(assetsPath).list()?.size ?: 0
        } catch (e: Exception) {
            0
        }
    }

    /**
     *
     */
    fun getImagesArrayByDirReference(dirRef: String): ArrayList<String> {
        return try {
            val assetsPath = context.filesDir.toString() + "/assets/files/$dirRef/"
            File(assetsPath).list()?.toList() as ArrayList<String>
        } catch (e: Exception) {
            ArrayList()
        }
    }

    /**
     *
     */
    fun getDrawableFromAssets(dirRef: String, reference: String): Drawable? {
        var drawable: Drawable? = null
        var inputStream: InputStream? = null
        try {
            val assetsPath = context.filesDir.toString() + "/assets/files/$dirRef/"
            Utils.getValidFile(path = assetsPath, reference = reference)?.let { imagePath ->
                inputStream = imagePath.inputStream()
                drawable = Drawable.createFromStream(inputStream, null)
            }
        } catch (e: Exception) {
            drawable = null
        } finally {
            inputStream?.close()
        }

        return drawable
    }
}