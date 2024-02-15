package com.tasty.recipeapp.ui.addNewRecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AddNewRecipeViewModelFactory @Inject constructor(
    val firestore: FirebaseFirestore,
    val repository: AddNewRecipeRepository,
    val compositeDisposable: CompositeDisposable
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddNewRecipeViewModel(firestore, repository,compositeDisposable) as T
    }
}