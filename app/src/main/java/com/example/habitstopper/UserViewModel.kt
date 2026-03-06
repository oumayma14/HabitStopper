package com.example.habitstopper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
                loadUserProfile()
            }catch (e: Exception){
                updateError = e.message ?: "Failed to update photo"
            }
        }
    }

    fun clearMessage(){
        updateSuccess = null
        updateError = null
    }

    fun syncStreakAndBadges(currentBestStreak: Int, unlockedBadgeName: List<String>){
        viewModelScope.launch {
            try {
                repository.updateStreakAndBadges(currentBestStreak, unlockedBadgeName)
                loadUserProfile()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // delete all habits first
                val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                val habits = db.collection("users").document(uid).collection("habits").get().await()
                habits.documents.forEach { it.reference.delete().await() }
                // delete user document
                db.collection("users").document(uid).delete().await()
                // delete firebase auth account
                com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.delete()?.await()
                onSuccess()
            } catch (e: Exception) {
                updateError = e.message ?: "Failed to delete account"
            }
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateDarkMode(enabled)
                userProfile = userProfile?.copy(darkMode = enabled)
                loadUserProfile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}