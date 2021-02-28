package com.sera.memorygame.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import org.json.JSONObject
import java.io.File
import java.io.IOException
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
    private fun getImageByReference(mContext: Context, reference: String): Int {
        return mContext.resources.getIdentifier(reference, "drawable", mContext.packageName)
    }

    /**
     *
     */
    fun loadJSONFromAsset(context: Context, jsonFile: String, objectName: String? = null): JSONObject? {
        val json: String?

        try {

            val reference = if (jsonFile.endsWith(".json")) {
                jsonFile
            } else {
                "$jsonFile.json"
            }

            val `is`: InputStream = getAssetsInputStream(context, reference)

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
    private fun getAssetsInputStream(context: Context, fileName: String): InputStream {
//        return if (SnapablyConstants.USE_SERVER_FOR_ASSETS) {
//            if (BuildConfig.BUILD_TYPE == SnapablyConstants.USE_SERVER_ASSETS_BUILD_TYPE) {
//                val assetsPath = context.filesDir.toString() + "/assets/"
//                val file = File(assetsPath + fileName)
//                file.inputStream()
//            } else {
//                context.assets.open(fileName)
//            }
//        } else {
        val assetsPath = context.filesDir.toString() + "/assets/files/json_files/"
        val file = File(assetsPath + fileName)
        return file.inputStream()
//        return context.assets.open(fileName)
//        }
    }

    /**
     *
     */
    fun getDrawableByReference(context: Context, reference: String): Drawable? {
        return AppCompatResources.getDrawable(
            context,
            getImageByReference(mContext = context, reference = reference)
        )
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

    /**
     *
     */
    private fun getValidFile(path: String, reference: String): File? {
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
    fun getSizeByDirectoryReference(context: Context, dirRef: String): Int {
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
    fun getImagesArrayByDirReference(context: Context, dirRef: String): ArrayList<String> {
        return try {
            val assetsPath = context.filesDir.toString() + "/assets/files/$dirRef/"
            File(assetsPath).list()?.toList() as ArrayList<String>
        } catch (e: Exception) {
            ArrayList()
        }
    }
}