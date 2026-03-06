package com.example.habitstopper

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun getOrCreateUserProfile(): UserProfile {
        val firebaseUser = auth.currentUser!!
        val docRef = db.collection("users").document(firebaseUser.uid)
        val snapshot = docRef.get().await()

        return if (snapshot.exists()) {
            snapshot.toObject(UserProfile::class.java)!!
        } else {
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

    suspend fun updateDisplayName(newName: String) {
        val user = auth.currentUser ?: return
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()
        user.updateProfile(profileUpdates).await()
        db.collection("users").document(user.uid)
            .update("displayName", newName).await()
    }

    suspend fun updatePassword(newPassword: String) {
        val user = auth.currentUser ?: return
        user.updatePassword(newPassword).await()
    }

    suspend fun updatePhotoUrl(photoUrl: String) {
        val user = auth.currentUser ?: return
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(photoUrl))
            .build()
        user.updateProfile(profileUpdates).await()
        db.collection("users").document(user.uid)
            .update("photoUrl", photoUrl).await()
    }

    suspend fun updateStreakAndBadges(currentBestStreak: Int, badgeNames: List<String>){
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("users").document(uid)
        val snapshot = docRef.get().await()
        val profile = snapshot.toObject(UserProfile::class.java) ?: return

        //only update the best streak if the user achieved a higher streak
        val newBestStreak = maxOf(profile.bestStreakEver, currentBestStreak)

        //merge new badges with existing ones
        val mergedBadges = (profile.unlockedBadges + badgeNames).distinct()

        docRef.update(
            mapOf(
                "bestStreakEver" to newBestStreak,
                "unlockedBadges" to mergedBadges
            )
        ).await()
    }
}