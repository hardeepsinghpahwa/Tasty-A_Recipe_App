package com.tasty.recipeapp.ui.dashboardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DashboardViewModelFactory @Inject constructor(
    val repository: DashboardRepository,
    val compositeDisposable: CompositeDisposable,
    private val firestore: FirebaseFirestore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository, compositeDisposable,firestore) as T
    }
}