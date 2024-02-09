package com.pahwa.recipeapp.ui.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val repository: SearchRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository, compositeDisposable) as T
    }
}