package com.tasty.recipeapp.ui.loginScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    var _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> get() = _loggedIn

    @Inject
    lateinit var oneTapClient: SignInClient

    fun googleSignIn(){
        loginRepository.googleSignIn()
    }

    fun checkUserLogin() {
        val loggedIn=loginRepository.checkForUserLogin()
        _loggedIn.postValue(loggedIn)
    }

}