package com.example.habitstopper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository{
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun getOrCreateUserProfile(): UserProfile{
        val firebaseUser = auth.currentUser!!
        val docRef = db.collection("users").document(firebaseUser.uid)
        val snapshot = docRef.get().await()

        return if(snapshot.exists()){
            snapshot.toObject(UserProfile::class.java)!!
        }else{
            val newProfile = UserProfile(
                uid = firebaseUser.uid,
                displayName = firebaseUser.displayName?.takeIf { it.isNotBlank() }
                    ?: firebaseUser.email?.substringBefore("@") ?: "User",
                email = firebaseUser.email ?: "",

                photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                joinDate = System.currentTimeMillis(),
                currentStreak = 0
            )
            docRef.set(newProfile).await()
            newProfile
        }
    }
}