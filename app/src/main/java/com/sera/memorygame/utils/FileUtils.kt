package com.sera.memorygame.utils

import android.content.Context
import android.util.Log
import java.io.File

object FileUtils {

    /**
     * Creates a directory of [dirName]
     */
    fun createDir(context: Context, dirName: String) {
        val path = context.filesDir.toString() + "/" + dirName
        val folder = File(path)
        folder.mkdir()
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

}