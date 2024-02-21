package com.tasty.recipeapp.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AccountViewModelFactory @Inject constructor(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(firebaseAuth,firestore) as T
    }
}