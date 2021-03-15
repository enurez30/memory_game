package com.sera.memorygame.utils

import android.content.Context
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

    /**
     *
     */
    fun deleteFileByUri(pCtx: Context, path: String) {
        try {
            if (path.contains(".")) {
                val f = File(pCtx.filesDir, path)
                f.delete()
            } else {
                val f = File(pCtx.filesDir, "$path.png")
                f.delete()
                val t = File(pCtx.filesDir, "${path}_thumb.png")
                t.delete()
                val u = File(path)
                u.delete()
            }

        } catch (e: Exception) {
            // TODO need to properly handle
            println("Error deleting file")
        }
    }

}