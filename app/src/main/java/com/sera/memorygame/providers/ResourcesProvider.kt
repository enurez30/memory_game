package com.sera.memorygame.providers

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import com.sera.memorygame.utils.Utils
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.Locale
import javax.inject.Inject


class ResourcesProvider @Inject constructor(private val context: Context) {

    /**
     *
     */
    fun getString(reference: String): String {
        println("TEST_TEST: ${this.javaClass.simpleName} getString() reference = $reference")
        val resId: Int = context.resources.getIdentifier(reference, "string", context.packageName)
        println("TEST_TEST: ${this.javaClass.simpleName} getString() resId = $resId")
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale.getDefault())
        return context.createConfigurationContext(configuration).resources.getString(resId)
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
    val String.getSizeByDirectoryReference: Int
        get() {
            return try {
                val assetsPath = context.filesDir.toString() + "/assets/files/$this/"
                File(assetsPath).list()?.size ?: 0
            } catch (e: Exception) {
                0
            }
        }


    /**
     *
     */
    fun getImagesArrayByDirReference(dirRef: String): List<String> {
        return try {
            val assetsPath = context.filesDir.toString() + "/assets/files/$dirRef/"
            File(assetsPath).list()?.toList() ?: listOf()
        } catch (e: Exception) {
            listOf()
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

    /**
     *
     */
    fun getStreamFromRaw(fName: String): InputStream {
        return context.resources.openRawResource(
            context.resources.getIdentifier(
                fName,
                "raw", context.packageName
            )
        )
    }

    /**
     *
     */
    val String.getResurceFromRaw: Int
        get() {
            println("TEST_TEST: getResurceFromRaw = $this")
            return context.resources.getIdentifier(this, "raw", context.packageName)
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

}