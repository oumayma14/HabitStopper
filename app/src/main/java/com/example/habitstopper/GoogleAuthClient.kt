package com.example.habitstopper.com.example.habitstopper

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthClient (private val context: Context){
    private val auth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient by lazy{

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("130593368712-dricd0s6jrml5uan12un4e56cr669c5q.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context,gso)

    }

    fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    suspend fun signInWithIntent(intent: Intent): Result<Unit>{
        return  try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(intent).result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    fun signOut(){
        auth.signOut()
        googleSignInClient.signOut()
    }
}