package com.sera.memorygame.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.MemoryApplication
import com.sera.memorygame.R
import com.sera.memorygame.database.repository.UserRepository
import com.sera.memorygame.factory.UserViewModelFactory
import com.sera.memorygame.service.AssetWorker
import com.sera.memorygame.utils.NetworkStatus
import com.sera.memorygame.viewModel.UserViewModel
import kotlinx.coroutines.flow.collect

class SplashActivity : BaseActivity() {

    /**
     *
     */
    private val userViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(context = this, repo = UserRepository(context = this))
    }

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as? MemoryApplication?)?.appComponent?.inject(activity = this)
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            addObserver()
        }
        lifecycleScope.launchWhenStarted {
            userViewModel.checkUserExistance()
        }
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