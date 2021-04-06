package com.sera.memorygame.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import java.io.File
import java.io.InputStream
import kotlin.random.Random


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

    /**
     *
     */
    fun getRandomColors(quantity: Int=5): List<Int> {
        val rnd = Random(256)
        return ArrayList<Int>().apply {
            repeat(quantity) {
                this.add(Color.argb(255, rnd.nextInt(), rnd.nextInt(), rnd.nextInt()))
            }
        }

    }
}