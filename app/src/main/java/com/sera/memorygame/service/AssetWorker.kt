package com.sera.memorygame.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sera.memorygame.Constants
import com.sera.memorygame.MessageEvent
import com.sera.memorygame.ui.dialog.AssetDownloadDialog
import com.sera.memorygame.utils.FileUtils
import com.sera.memorygame.utils.ZipManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.File
import java.io.File.createTempFile


class AssetWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     *
     */
    override fun doWork(): Result {
        scope.launch {
            getConfiguration()
        }
        return Result.success()
    }

    /**
     *
     */
    private suspend fun getConfiguration() {
        coroutineScope {
            val fileRef = Firebase.storage(Constants.BUCKET_ID).reference.child("configuration.json")
            val localFile = createTempFile("tmp", ".json")
            fileRef.getFile(localFile).addOnSuccessListener {
                parseData(localFile = localFile)?.let { json ->
                    localFile.delete()
                }
                scope.launch {
                    getAssets()
                }
            }.addOnFailureListener {
                sendEvent(key = "error", msg = "${it.message}")
                localFile.delete()
            }
        }
    }

    /**
     *
     */
    private suspend fun getAssets() {
        coroutineScope {
            val fileRef = Firebase.storage(Constants.BUCKET_ID).reference.child("files/files.zip")
            val localFile = createTempFile("tmp", ".zip")
            fileRef.getFile(localFile).addOnSuccessListener {
                unzipFiles(tempFile = localFile)
                localFile.delete()
            }.addOnFailureListener {
                sendEvent(key = "error", msg = "${it.message}")
                localFile.delete()
            }
        }
    }

    /**
     * Creates and unzips a zip file contained within the [tempFile] data
     */
    private fun unzipFiles(tempFile: File) {
        try {
            val dirName = "assets"
            val path = context.filesDir.toString() + "/" + dirName
            if (!File(path).exists()) {
                FileUtils.createDir(context, dirName)
            }

            FileUtils.deleteDirectory(context, dirName)
            //create assets folder
            FileUtils.createDir(context, dirName)
            if (tempFile.path.isNotEmpty()) {
                ZipManager.unzip(tempFile.path, context.filesDir.toString() + "/" + dirName)
            }
        } catch (e: Exception) {
            sendEvent(key = "error", msg = "${e.message}")
        }
        sendEvent(key = "finish", msg = "All set!")
    }


    /**
     *
     */
    private fun parseData(localFile: File): JSONObject? {
        return try {
            JSONObject(StringBuilder().apply {
                localFile.reader().readLines().map {
                    this.append(it)
                }
            }.toString())
        } catch (e: Exception) {
            null
        }
    }

    /**
     *
     */
    private fun sendEvent(key: String, msg: String) {
        val event = MessageEvent().apply {
            this.reciever = AssetDownloadDialog::class.java.simpleName
            this.key = key
            this.message = msg
        }
        println("DOWNLOAD: post event= ${event.key}")
        EventBus.getDefault().post(event)
    }

}