package com.example.habitstopper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel(){
    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    fun loadUserProfile(){
        viewModelScope.launch{
            try{

                isLoading = true
                userProfile = repository.getOrCreateUserProfile()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

}