package com.example.habitstopper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
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


    //update display name
    suspend fun updateDisplayName(newName: String){
        val user = auth.currentUser ?: return
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()
        user.updateProfile(profileUpdates).await()
        db.collection("users").document(user.uid)
            .update("displayName", newName).await()
    }

    suspend fun updatePassword(newPasssword: String){
        val user = auth.currentUser ?: return
        user.updatePassword(newPasssword).await()
    }

    suspend fun updatePhotoUrl(photoUrl: String){
        val user = auth.currentUser ?: return
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(android.net.Uri.parse(photoUrl))
            .build()
        user.updateProfile(profileUpdates).await()
        db.collection("users").document(user.uid)
            .update("photoUrl", photoUrl).await()

    }

}