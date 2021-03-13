package com.sera.memorygame.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.entity.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainActivityViewModel @Inject constructor (val context: Context) : ViewModel() {

//    /**
//     *
//     */
//    init {
//        fetchUser()
//    }

    /**
     *
     */
    private val sessionUser = MutableStateFlow<UserEntity?>(null)

    /**
     *
     */
    fun print(){
        println("DAGGER_WTF: wohoooo")
    }

    /**
     *
     */
    fun getUserInSession(): StateFlow<UserEntity?> = sessionUser

    /**
     *
     */
    private fun fetchUser() {
//        viewModelScope.launch {
//            repo.getUserInSession()
//                .catch {
//                    println("error")
//                }
//                .collect {
//                    sessionUser.value = it
//                }
//        }
    }

}