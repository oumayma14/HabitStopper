package com.example.habitstopper.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                repository.signUp(email, password)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Sign up failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                repository.login(email, password)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Login failed"
            } finally {
                isLoading = false
            }
        }
    }
}
