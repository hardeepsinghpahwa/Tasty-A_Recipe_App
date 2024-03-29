package com.tasty.recipeapp.ui.recipeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RecipeDetailsViewModelFactory @Inject constructor(
    val repository: RecipeDetailsRepository,
    val compositeDisposable: CompositeDisposable,
    val firestore: FirebaseFirestore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeDetailsViewModel(repository, compositeDisposable,firestore) as T
    }
}