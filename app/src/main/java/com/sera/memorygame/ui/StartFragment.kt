package com.sera.memorygame.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sera.memorygame.R
import com.sera.memorygame.databinding.StartFragmentBinding
import com.sera.memorygame.factory.StartFragmentFactory
import com.sera.memorygame.utils.AnimationHelper
import com.sera.memorygame.utils.FileUtils
import com.sera.memorygame.utils.ZipManager
import com.sera.memorygame.viewModel.StartViewModel
import java.io.File

class StartFragment : BaseFragment() {
    private lateinit var mBinder: StartFragmentBinding

    /**
     *
     */
    companion object {
        fun newInstance() = StartFragment()
    }

    /**
     *
     */
    private val viewModel by viewModels<StartViewModel> {
        StartFragmentFactory(context = requireContext())
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.start_fragment,
            container,
            false
        )
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        prepareView()
        Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
            releaseView()
        }, 500)

    }

    /**
     *
     */
    private fun prepareView() {
        mBinder.playBtn.animate().translationY(1000F).setDuration(0L).start()
    }

    /**
     *
     */
    private fun releaseView() {
        AnimationHelper.animateSpringY(targetView = mBinder.playBtn, finalPosition = 0F)
    }

    /**
     *
     */
    override fun delegateHandlerClick(view: View) {
        when (view.id) {
            R.id.playBtn -> {
                val path = requireContext().filesDir.toString() + "/" + "assest"
                //(requireActivity() as MainActivity).replaceFragment(fragment = GameThemeFragment.newInstance())
                println()
//                val storage = Firebase.storage("gs://memorygame-c6487.appspot.com")
//                val storageRef = storage.reference
//                println()
//
////                var islandRef = storageRef.child("files/files.zip")
////                val ONE_MEGABYTE: Long = 1024 * 1024
////                lifecycleScope.launch(Dispatchers.IO) {
////                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
////                        // Data for "images/island.jpg" is returned, use this as needed
////                        println()
////                    }.addOnFailureListener {
////                        // Handle any errors
////                        println()
////                    }
////                }
//
//                val islandRef = storageRef.child("files/files.zip")
//
//                val localFile = File.createTempFile("tmp", "zip")
//
//                islandRef.getFile(localFile).addOnSuccessListener {
//                    println()
//                    unzipFiles(tempFile = localFile)
//                    // Local temp file has been created
//                }.addOnFailureListener {
//                    // Handle any errors
//                    println()
//                }

            }
        }
    }

    /**
     * Creates and unzips a zip file contained within the [response] data
     * Uses [versionNumber] to build the filepath
     */
    private fun unzipFiles(tempFile: File) {
        try {
            val dirName = "assets"
            val path = requireContext().filesDir.toString() + "/" + dirName
            if (!File(path).exists()) {
                FileUtils.createDir(requireContext(), dirName)
            }
            val zipFile = File(path, "files.zip")
            zipFile.writeBytes(tempFile.readBytes())
            val zipFilePath = zipFile.path

            println()

            FileUtils.deleteDirectory(requireContext(), dirName)

            //create assets folder
            FileUtils.createDir(requireContext(), dirName)
            if (zipFilePath.isNotEmpty()) {
                ZipManager.unzip(zipFilePath, requireContext().filesDir.toString() + "/" + dirName)
            }

            tempFile.delete()
            println()
        } catch (e: Exception) {
            println()
        }
    }
}