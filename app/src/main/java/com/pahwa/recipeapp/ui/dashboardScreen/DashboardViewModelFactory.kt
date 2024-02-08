package com.pahwa.recipeapp.ui.dashboardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DashboardViewModelFactory @Inject constructor(
    val repository: DashboardRepository,
    val compositeDisposable: CompositeDisposable
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository, compositeDisposable) as T
    }
}