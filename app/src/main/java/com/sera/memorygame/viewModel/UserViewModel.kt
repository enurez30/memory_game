package com.sera.memorygame.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sera.memorygame.database.entity.UserEntity
import com.sera.memorygame.database.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(private val context: Context, private val repo: UserRepository) : ViewModel() {

    /**
     *
     */
    private val sessionUser: MutableStateFlow<UserEntity?> by lazy {
        MutableStateFlow<UserEntity?>(null)
            .also {
                fetchUser()
            }
    }

    /**
     *
     */
    fun getName(): String {
        return sessionUser.value?.userName ?: "NO NAME"
    }

    /**
     *
     */
    fun getUserInSession(): StateFlow<UserEntity?> = sessionUser

    /**
     *
     */
    fun updateUser() {
        sessionUser.value?.let { user ->
            viewModelScope.launch {
                repo.updateUser(user = user)
            }
        }
    }

    /**
     *
     */
    private fun fetchUser() {
        viewModelScope.launch {
            repo.getUserInSession()
                .catch {
                    println("error")
                }
                .collect {
                    sessionUser.value = it
                }
        }
    }

    /**
     * method to check if there is al least one user in DB, if not - create default
     */
    suspend fun checkUserExistance() {
        repo.getAllUsersFlow()
            .catch {
                println("error getching all users")
            }
            .collect {
                if (it.isEmpty()) {
                    repo.persistUser(user = repo.createUser())
                }
            }
    }
}
