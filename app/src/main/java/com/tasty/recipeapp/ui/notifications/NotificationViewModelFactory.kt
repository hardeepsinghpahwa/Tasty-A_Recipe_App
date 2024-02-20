package com.tasty.recipeapp.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class NotificationViewModelFactory @Inject constructor(val repository: NotificationRepository,val firestore: FirebaseFirestore,val firebaseAuth: FirebaseAuth) :ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationViewModel(repository,firestore,firebaseAuth) as T
    }
}