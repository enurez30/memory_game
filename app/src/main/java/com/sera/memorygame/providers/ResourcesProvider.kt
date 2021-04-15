package com.sera.memorygame.providers

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.sera.memorygame.utils.Utils
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList


class ResourcesProvider @Inject constructor(private val context: Context) {

    /**
     *
     */
    fun getString(reference: String): String {
        val resId: Int = context.resources.getIdentifier(reference, "string", context.packageName)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale.getDefault())
        return context.createConfigurationContext(configuration).resources.getString(resId)
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
    fun getResurceFromRaw(fName: String): Int {
        return context.resources.getIdentifier(fName, "raw", context.packageName)
    }

    /**
     *
     */
    fun test() {
        var inputStream: InputStream? = null
        val assetsPath = context.filesDir.toString() + "/assets/files/values/"
        println()
        val file = File(assetsPath + "theme.xml")
        println()
        inputStream = file.inputStream()
        println()
        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val db: DocumentBuilder = dbf.newDocumentBuilder()
        val doc: Document = db.parse(file)
        doc.getDocumentElement().normalize()

        val nodeList: NodeList = doc.getElementsByTagName("resources")

        for (i in 0 until nodeList.getLength()) {
            val node: Node = nodeList.item(i)
            val fstElmnt: Element = node as Element
        }
        val n: NodeList = doc.getElementsByTagName("log")

//        for (j in 0 until count) {
//            val node: Node = n.item(j)
//            val fstElmnt: Element = node as Element
//            phone_no.add(fstElmnt.getAttribute("number"))
//        }
    }
}