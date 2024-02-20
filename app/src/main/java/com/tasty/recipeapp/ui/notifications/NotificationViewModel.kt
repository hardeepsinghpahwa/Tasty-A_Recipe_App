package com.tasty.recipeapp.ui.notifications

import android.content.ContentValues
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.tasty.recipeapp.model.response.NotificationResponse


class NotificationViewModel(
    val repository: NotificationRepository,
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private var notifications = MutableLiveData<ArrayList<NotificationResponse>>()
    val retry = ObservableField(false)
    val loading = ObservableField(false)


    fun getNotifications(): LiveData<ArrayList<NotificationResponse>> {
        return notifications
    }


    fun getNotificationData() {
        loading.set(true)
        retry.set(false)

        firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("notifications").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (!document.isEmpty) {
                        val temp = ArrayList<NotificationResponse>()
                        for (snapshot in document) {
                            val data = snapshot.toObject<NotificationResponse>()
                            Log.d("NOTIFI_DATA",snapshot.data.toString())
                            temp.add(data
                            )
                        }
                        notifications.postValue(temp)
                    } else {

                    }
                    loading.set(false)
                } else {
                    loading.set(false)
                    retry.set(true)
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                loading.set(false)
                retry.set(true)
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }


}