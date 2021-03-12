package com.sera.memorygame.utils

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


class PictureLifecycleObserver(private val context: Context, private val registry: ActivityResultRegistry, private val callback: (Result<Any>) -> Unit) : DefaultLifecycleObserver {
    lateinit var getContent: ActivityResultLauncher<String>
    lateinit var getPicture: ActivityResultLauncher<Uri>
    private lateinit var photoUri: Uri

    /**
     *
     */
    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("image_picking", owner, ActivityResultContracts.GetContent(), {
            if (it == null) {
                println("user cancel image picking")
            } else {
                callback(Result.success(it))
            }
        })

        getPicture = registry.register("taking_picture", owner, ActivityResultContracts.TakePicture(), {
            if (it) {
                callback(Result.success(photoUri))
            } else {
                callback(Result.failure(Throwable("Error")))
            }
        })
    }

    /**
     *
     */
    fun selectImage(isCamera: Boolean) {
        if (isCamera) {
            val photoFile = FileUtils.createImageFile(context = context)
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(context, "com.sera.memorygame.fileprovider", photoFile)
                getPicture.launch(photoUri)
            }

        } else {
            getContent.launch("image/*")
        }
    }
}