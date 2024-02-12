package com.tasty.recipeapp.ui.loginScreen

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginRepository @Inject constructor(
    val auth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val activity: Activity
) {

    fun checkForUserLogin(): Boolean {
        if (auth.currentUser == null) {
            Log.d("SIGNIN", "not signed in")
            return false
        } else {
            Log.d("SIGNIN", "signed in")
            return true
        }
    }

    fun googleSignIn() {

        Log.d("SIGNIN", "clicked sign in")
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("606144302985-ber74vgkmr4ap58iglr2unq98jidij2k.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(
                activity
            ) { result ->
                try {
                    activity.startIntentSenderForResult(
                        result.pendingIntent.intentSender, 2,
                        null, 0, 0, 0
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("SIGNIN", "Couldn't start One Tap UI: " + e.localizedMessage)
                }
            }
            .addOnFailureListener(activity) { e -> // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Toast.makeText(
                    activity,
                    "Google sign in failed. Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("SIGNIN", e.localizedMessage)
            }
    }


}