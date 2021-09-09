package com.sera.memorygame.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.R
import com.sera.memorygame.service.AssetWorker
import com.sera.memorygame.utils.status_callback.NetworkStatus
import com.sera.memorygame.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var userViewModel: UserViewModel

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val flowA = flow { emit(addObserver()) }
            val flowB = flow { emit(userViewModel.checkUserExistance()) }
            merge(flowA, flowB).collect()
        }

    }

    /**
     *
     */
    override fun onStart() {
        super.onStart()
        WorkManager.getInstance(this).enqueue(OneTimeWorkRequestBuilder<AssetWorker>().build())
    }

    /**
     *
     */
    private suspend fun addObserver() {
        workerState.collect {
            when (it) {
                NetworkStatus.START.status -> {
                    this.showSnackBar(getString(R.string.check_for_updates), duration = Snackbar.LENGTH_INDEFINITE)
                }
                NetworkStatus.DOWNLOAD.status -> {
                    this.showSnackBar(getString(R.string.downloading_assets), duration = Snackbar.LENGTH_INDEFINITE)
                }
                NetworkStatus.ERROR.status -> {
                    this.showSnackBar(getString(R.string.error), duration = Snackbar.LENGTH_INDEFINITE)
                    moveNext()
                }
                NetworkStatus.NO_INTERNET_CONNECTION.status -> {
                    this.showSnackBar(getString(R.string.no_internet_connection), duration = Snackbar.LENGTH_INDEFINITE)
                    moveNext()
                }
                NetworkStatus.FINISH.status -> {
                    moveNext()
                }
            }
        }

    }

    /**
     *
     */
    private fun moveNext() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}