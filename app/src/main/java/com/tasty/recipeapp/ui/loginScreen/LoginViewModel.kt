package com.tasty.recipeapp.ui.loginScreen

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.tasty.recipeapp.model.response.UserDetails
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    val compositeDisposable: CompositeDisposable,
    private val credentialManager: CredentialManager,
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private var _loggedIn = MutableLiveData<Boolean>()

    private var successMessage = MutableLiveData<String>()

    private var failureMessage = MutableLiveData<String>()

    val loading = ObservableField(false)

    @Inject
    lateinit var oneTapClient: SignInClient

    fun getSuccessMessage(): LiveData<String> {
        return successMessage
    }

    fun getFailureMessage(): LiveData<String> {
        return failureMessage
    }

    fun checkUserLogin() {
        val loggedIn = loginRepository.checkForUserLogin()
        _loggedIn.postValue(loggedIn)
    }

    fun getLoggedIn(): LiveData<Boolean> {
        return _loggedIn
    }


    fun signIn(applicationContext: Context) {
        loading.set(true)
        googleSign(applicationContext)
    }

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("606144302985-lchur7a48tagvol8j8asqsv3o10jitnv.apps.googleusercontent.com")
        .setAutoSelectEnabled(false)
        .build()

    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()


    private fun googleSign(applicationContext: Context) {
        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(applicationContext, request)
                val credential = result.credential
                if (credential is GoogleIdTokenCredential) {
                    val googleDisplayName = credential.displayName
                    val googleProfilePictureUri = credential.profilePictureUri

                    val googleCredential =
                        GoogleAuthProvider.getCredential(credential.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(googleCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (firebaseAuth.currentUser != null) {

                                    val obj = UserDetails()

                                    obj.name = googleDisplayName
                                    obj.profilePic = googleProfilePictureUri.toString()

                                    val ref =
                                        firestore.collection("users")
                                            .document(firebaseAuth.currentUser!!.uid)

                                    ref.set(obj).addOnSuccessListener {
                                        _loggedIn.postValue(true)
                                        //successMessage.postValue("Logged In Successfully ")
                                    }.addOnFailureListener {
                                        loading.set(false)
                                        failureMessage.postValue("Failed to login. Please try again.")
                                    }
                                } else {
                                    failureMessage.postValue("No User Logged In")
                                    loading.set(false)
                                }
                            } else {
                                failureMessage.postValue("No User Logged In")
                                loading.set(false)
                            }
                        }
                } else {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                    val googleDisplayName = googleIdTokenCredential.displayName
                    val googleProfilePictureUri = googleIdTokenCredential.profilePictureUri

                    val googleCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(googleCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (firebaseAuth.currentUser != null) {

                                    val obj = UserDetails()

                                    obj.name = googleDisplayName
                                    obj.profilePic = googleProfilePictureUri.toString()

                                    val ref =
                                        firestore.collection("users")
                                            .document(firebaseAuth.currentUser!!.uid)

                                    ref.set(obj).addOnSuccessListener {
                                        _loggedIn.postValue(true)
                                        //successMessage.postValue("Logged In Successfully ")
                                    }.addOnFailureListener {
                                        loading.set(false)
                                        failureMessage.postValue("Failed to login. Please try again.")
                                    }
                                } else {
                                    loading.set(false)
                                    failureMessage.postValue("No User Logged In")
                                }
                            } else {
                                loading.set(false)
                                failureMessage.postValue("No User Logged In")
                            }
                        }
                }
            } catch (e: GetCredentialException) {
                loading.set(false)
                failureMessage.postValue("Google Sign-In Failure")
            }
        }
    }
}