package com.example.habitstopper.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await


class AuthRepository (
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
){
    fun isLoggedIn(): Boolean = auth.currentUser != null
    fun currentUserEmail(): String? = auth.currentUser?.email

    suspend fun signUp(email: String, password: String, displayName: String) {
        // creates the account
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()

        // then saves the name to Firebase Auth profile
        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(displayName.trim())
            .build()
        result.user?.updateProfile(profileUpdates)?.await()
    }

    suspend fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email.trim(),password).await()
    }

    fun logout(){
        auth.signOut()
    }
}