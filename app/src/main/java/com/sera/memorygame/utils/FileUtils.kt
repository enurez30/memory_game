package com.sera.memorygame.utils

import android.content.Context
import android.graphics.Bitmap
import com.sera.memorygame.database.model.IObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    /**
     * Creates a directory of [dirName]
     */
    fun createDir(context: Context, dirName: String) {
        val path = context.filesDir.toString() + "/" + dirName
        val folder = File(path)
        folder.mkdirs()
    }

    /**
     * deletes the files from a specified directory
     */
    fun deleteDirectory(context: Context, dirName: String) {
        val path = context.filesDir.toString() + "/" + dirName
        val dir = File(path)
        if (dir.exists()) {
            dir.deleteRecursively()
        }
    }


    /**
     * @param pCtx
     * @param pName
     */
    fun deleteFile(pCtx: Context, pName: String) {
        try {
            if (pName.contains(".")) {
                val f = File(pCtx.filesDir, pName)
                f.delete()
            } else {
                val f = File(pCtx.filesDir, "$pName.png")
                f.delete()
                val t = File(pCtx.filesDir, "${pName}_thumb.png")
                t.delete()
            }

        } catch (e: Exception) {
            println("Could not delete file: $pName $e")
        }
    }

    /**
     *
     */
    fun saveAndDeleteOldImage(img: Bitmap, context: Context, entity: IObject): String {
        val uniqueName = UUID.randomUUID().toString()

        val imageFile = entity.getAvatarReference()
        deleteFile(context, imageFile)

        saveImage(context, uniqueName, img)
        return uniqueName
    }

    /**
     * @param pName
     * @param pImage
     */
    private fun saveImage(pCtx: Context, pName: String, pImage: Bitmap) {
        try {
            val stream = pCtx.openFileOutput("$pName.png", Context.MODE_PRIVATE)
            pImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

//            if (pGenerateThumbnail) {
//                val ratio: Float = Constants.THUMBNAIL_SIZE / pImage.width
//                val w = Constants.THUMBNAIL_SIZE.toInt()
//                val h = (pImage.height * ratio).toInt()
//                val thumb: Bitmap = Bitmap.createScaledBitmap(pImage, w, h, false)
//                val streamThumb = pCtx.openFileOutput("${pName}_thumb.png", Context.MODE_PRIVATE)
//                thumb.compress(Bitmap.CompressFormat.PNG, 100, streamThumb)
//                streamThumb.close()
//            }
        } catch (e: Exception) {
            println("Could not save image: $pName")
        }
    }

    /**
     *
     */
    fun createImageFile(context: Context): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "IMG_" + timeStamp + "_"
            val storageDir = context.getDir("cards", Context.MODE_PRIVATE)
            File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
            )
        } catch (e: Exception) {
            null
        }
    }
}