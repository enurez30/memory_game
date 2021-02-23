package com.sera.memorygame.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import org.json.JSONObject
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
            val `is`: InputStream = if (jsonFile.endsWith(".json")) {
                getAssetsInputStream(context, jsonFile)
            } else {
                getAssetsInputStream(context, "$jsonFile.json")
            }

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
        return context.assets.open(fileName)
//        }
    }

    /**
     *
     */
    fun getDrawableByReference(context: Context, reference: String): Drawable? {
        return AppCompatResources.getDrawable(context, getImageByReference(mContext = context, reference = reference))
    }
}