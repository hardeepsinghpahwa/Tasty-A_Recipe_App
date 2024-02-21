package com.tasty.recipeapp.ui.account

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountViewModel(
    private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore
) : ViewModel() {

    lateinit var accountUtil: AccountUtil

    val retry = ObservableField(false)
    val loading = ObservableField(false)
    val edit = ObservableField(false)
    val name = ObservableField("")
    val email = ObservableField("")
    val image = MutableLiveData<String>()
    val cuisine = ObservableField("")
    val food = ObservableField("")
    val chef = ObservableField("")
    val restaurant = ObservableField("")

    fun setUtils(accountUtil: AccountUtil){
        this.accountUtil=accountUtil
    }

    fun initAccountDetails() {
        val user = firebaseAuth.currentUser

        if (user != null) {
            name.set(user.displayName)
            email.set(user.email)
            image.postValue(user.photoUrl.toString())
        }
    }

    fun saveDetails() {
        loading.set(true)
        retry.set(false)

        val data = HashMap<String, Any>()
        data["food"] = food.get()!!
        data["chef"] = chef.get()!!
        data["restaurant"] = restaurant.get()!!
        data["cuisine"] = cuisine.get()!!

        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).update(data)
            .addOnSuccessListener {
                edit.set(false)
                getDetails()
            }.addOnFailureListener {
                loading.set(false)
            }
    }

    fun getDetails() {
        loading.set(true)
        retry.set(false)

        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val snapshot = it.data

                Log.d("SNAPPPP",snapshot.toString())

                if (snapshot != null) {
                    if (snapshot["cuisine"] != null) {
                        cuisine.set(snapshot["cuisine"].toString())
                    }

                    if (snapshot["food"] != null) {
                        cuisine.set(snapshot["food"].toString())
                    }

                    if (snapshot["chef"] != null) {
                        cuisine.set(snapshot["chef"].toString())
                    }


                    if (snapshot["restaurant"] != null) {
                        cuisine.set(snapshot["restaurant"].toString())
                    }

                    loading.set(false)
                }

            }.addOnFailureListener {
                retry.set(true)
                loading.set(false)
            }
    }

    fun logout() {
        firebaseAuth.signOut()
        accountUtil.openIntroScreen()
    }

}