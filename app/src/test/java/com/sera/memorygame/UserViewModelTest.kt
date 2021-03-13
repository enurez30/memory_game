package com.sera.memorygame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sera.memorygame.database.repository.UserRepository
import com.sera.memorygame.viewModel.UserViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class UserViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        repository = Mockito.mock(UserRepository::class.java)
        viewModel = UserViewModel(repo = repository)
    }

    @Test
    fun `Get username`() {
        Mockito.`when`(viewModel.getUserInSession().value ?: "").thenReturn("Username")

        val username = viewModel.getName()

        Assert.assertEquals("Username", username)
    }
}