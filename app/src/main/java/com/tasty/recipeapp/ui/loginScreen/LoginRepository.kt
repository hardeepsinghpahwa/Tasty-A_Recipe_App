package com.tasty.recipeapp.ui.loginScreen

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginRepository @Inject constructor(
    val auth: FirebaseAuth,
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
}