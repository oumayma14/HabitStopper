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

    var updateSuccess by mutableStateOf<String?>(null)
        private set

    var updateError by mutableStateOf<String?>(null)
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

    fun updateDisplayName(newName: String){
        viewModelScope.launch {
            try {
                repository.updateDisplayName(newName)
                updateSuccess = "Name updated successfully"
                loadUserProfile()
            } catch (e: Exception){
                updateError = e.message ?: "Failed to update name"
            }
        }
    }

    fun updatePassword(newPassword: String){
        viewModelScope.launch {
            try {
                repository.updatePassword(newPassword)
                updateSuccess = "Password updated successfully!"
            } catch (e: Exception){
                updateError = e.message ?: "Failed to update password"
            }
        }
    }

    fun updatePhotoUrl(photoUrl: String){
        viewModelScope.launch {
            try {
                repository.updatePhotoUrl(photoUrl)
                updateSuccess = "Photo updated successfully!"
            }catch (e: Exception){
                updateError = e.message ?: "Failed to update photo"
            }
        }
    }

    fun uploadProfilePhoto(imageUri: android.net.Uri) {
        viewModelScope.launch {
            try {
                isLoading = true
                repository.uploadProfilePhoto(imageUri)
                updateSuccess = "Photo updated!"
                loadUserProfile()
            } catch (e: Exception) {
                updateError = e.message ?: "Failed to upload photo"
            } finally {
                isLoading = false
            }
        }
    }


    fun clearMessage(){
        updateSuccess = null
        updateError = null
    }

}