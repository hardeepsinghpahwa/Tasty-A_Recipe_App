package com.tasty.recipeapp.ui.loginScreen

import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(
    private val repository: LoginRepository,
    val compositeDisposable: CompositeDisposable,
    private val credentialManager: CredentialManager,
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            this.repository,
            this.compositeDisposable,
            this.credentialManager,
            this.firestore,
            this.firebaseAuth
        ) as T
    }
}