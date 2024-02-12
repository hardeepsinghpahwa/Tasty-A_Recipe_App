package com.tasty.recipeapp.ui.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(private val repository: LoginRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(this.repository) as T
    }
}