package com.sera.memorygame.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sera.memorygame.Constants
import com.sera.memorygame.MessageEvent
import com.sera.memorygame.ui.MainActivity
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
        sendEvent(msg = "DOWNLOAD: getConfiguration request")
        coroutineScope {
            val fileRef = Firebase.storage(Constants.BUCKET_ID).reference.child("configuration.json")
            val localFile = createTempFile("tmp", ".json")
            fileRef.getFile(localFile).addOnSuccessListener {
                sendEvent(msg = "DOWNLOAD: obtaining request and parsing data")
                parseData(localFile = localFile)?.let { json ->
                    sendEvent(msg = "DOWNLOAD: version= ${json.getString("asset_version")}")
                    localFile.delete()
                }
                scope.launch {
                    getAssets()
                }
            }.addOnFailureListener {
                sendEvent(msg = "DOWNLOAD: error downloading file= ${it.message}")
                localFile.delete()
            }
        }
    }

    /**
     *
     */
    private suspend fun getAssets() {
        sendEvent(msg = "DOWNLOAD: executing assets request")
        coroutineScope {
            val fileRef = Firebase.storage(Constants.BUCKET_ID).reference.child("files/files.zip")
            val localFile = createTempFile("tmp", ".zip")
            fileRef.getFile(localFile).addOnSuccessListener {
                sendEvent(msg = "DOWNLOAD: Start Unzipping")
                unzipFiles(tempFile = localFile)
                localFile.delete()
            }.addOnFailureListener {
                sendEvent(msg = "DOWNLOAD: error downloading file= ${it.message}")
                localFile.delete()
            }
        }
    }

    /**
     * Creates and unzips a zip file contained within the [tempFile] data
     */
    private fun unzipFiles(tempFile: File) {
        try {
            var count = 0
            sendEvent(msg = "DOWNLOAD: max count= ${ZipManager.getMaxCount(file = tempFile)}")
            val dirName = "assets"
            val path = context.filesDir.toString() + "/" + dirName
            if (!File(path).exists()) {
                FileUtils.createDir(context, dirName)
            }

            FileUtils.deleteDirectory(context, dirName)
            //create assets folder
            FileUtils.createDir(context, dirName)
            if (tempFile.path.isNotEmpty()) {
                ZipManager.unzip(tempFile.path, context.filesDir.toString() + "/" + dirName) { result ->
                    if (result.isSuccess) {
                        count += 1
                        sendEvent(msg = "DOWNLOAD file count= $count")
                    }
                }
            }
        } catch (e: Exception) {
            sendEvent(msg = "DOWNLOAD error parsing zip file= ${e.message}")
        }
        //            EventBus.getDefault().post(Pair("test",HashMap<String,Any>()))
        sendEvent(msg = "DOWNLOAD DONE!!!!")
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
    private fun sendEvent(msg: String) {
        val event = MessageEvent().apply {
            this.reciever = MainActivity::class.java.simpleName
            this.key = "assets_service"
            this.message = msg
        }
        EventBus.getDefault().post(event)
    }

}