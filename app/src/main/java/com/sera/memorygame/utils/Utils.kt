package com.sera.memorygame.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import java.io.File
import java.io.InputStream


object Utils {

    /**
     *
     */
    fun dpToPx(dp: Int): Int {
        return (Resources.getSystem().displayMetrics.density * dp).toInt()
    }

    /**
     *
     */
    fun pxToDp(resource: Resources, px: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            px,
            resource.displayMetrics
        )
    }

    /**
     *
     */
    fun getImageByReference(mContext: Context, reference: String): Int {
        return mContext.resources.getIdentifier(reference, "drawable", mContext.packageName)
    }

//    /**
//     *
//     */
//    fun getJsonByReference(context: Context,reference: String): JSONObject? {
//        loadJSONFromAsset(context = context, jsonFile = "memory_game_themes")?.let {
//            val collection = it.getJSONArray("collection")
//            repeat(collection.length()) { index ->
//                val obj = collection[index] as JSONObject
//                if (obj.getString("json_ref") == reference) {
//                    return obj
//                }
//            }
//        }
//        return null
//    }

//    /**
//     *
//     */
//    fun loadJSONFromAsset(context: Context, jsonFile: String, objectName: String? = null): JSONObject? {
//        val json: String?
//
//        try {
//
//            val reference = if (jsonFile.endsWith(".json")) {
//                jsonFile
//            } else {
//                "$jsonFile.json"
//            }
//
//            val `is`: InputStream = getAssetsInputStream(context, reference)
//
//            val size = `is`.available()
//            val buffer = ByteArray(size)
//            `is`.read(buffer)
//            `is`.close()
//            json = String(buffer)
//        } catch (ex: IOException) {
//            // TODO: need to handle this
//            ex.printStackTrace()
//            return null
//        }
//        return if (objectName == null) {
//            JSONObject(json).getJSONObject("entity")
//        } else {
//            JSONObject(json).getJSONObject(objectName)
//        }
//    }

    /**
     *
     */
    fun getAssetsInputStream(context: Context, fileName: String): InputStream {
        val assetsPath = context.filesDir.toString() + "/assets/files/json_files/"
        val file = File(assetsPath + fileName)
        return file.inputStream()
    }


    /**
     *
     */
    fun getValidFile(path: String, reference: String): File? {
        if (reference.endsWith(".png") || reference.endsWith(".webp")) {
            return File(path + reference)
        }
        var ref = "$reference.png"
        return if (File(path + ref).exists()) {
            return File(path + ref)
        } else {
            ref = "$reference.webp"
            if (File(path + ref).exists()) {
                return File(path + ref)
            } else {
                null
            }
        }
    }

    /**
     *
     */
    fun getDrawableFromAssets(context: Context, dirRef: String, reference: String): Drawable? {
        var drawable: Drawable? = null
        var inputStream: InputStream? = null
        try {
            val assetsPath = context.filesDir.toString() + "/assets/files/$dirRef/"
            getValidFile(path = assetsPath, reference = reference)?.let { imagePath ->
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