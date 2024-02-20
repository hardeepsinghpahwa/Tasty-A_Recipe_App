package com.tasty.recipeapp.ui.addNewRecipe

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tasty.recipeapp.model.response.Ingredient
import com.tasty.recipeapp.model.response.IngredientResponse
import com.tasty.recipeapp.model.response.MealsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Field
import java.util.UUID

class AddNewRecipeViewModel(
    val firestore: FirebaseFirestore,
    val repository: AddNewRecipeRepository,
    val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var ingredients = MutableLiveData<ArrayList<Ingredient>>()

    private var successMessage = MutableLiveData<String>()

    private var failureMessage = MutableLiveData<String>()

    var selectedIngredients = ArrayList<Ingredient>()

    var name = ObservableField("")
    var ytLink = ObservableField("")
    var instructions = ObservableField("")
    var area = ObservableField("")
    var category = ObservableField("")
    var time = ObservableField("")
    val retry = ObservableField(false)
    val loading = ObservableField(false)
    var uri: Uri? = null
    val rating = ObservableInt(0)


    fun getSuccessMessage(): LiveData<String> {
        return successMessage
    }

    fun getFailureMessage(): LiveData<String> {
        return failureMessage
    }

    fun getIngredients(): LiveData<ArrayList<Ingredient>> {
        return ingredients
    }

    private fun uploadPhoto() {
        loading.set(true)
        val riversRef = Firebase.storage.reference.child("images/${uri!!.lastPathSegment}")
        val uploadTask = riversRef.putFile(uri!!)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            riversRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                addNewRecipe(downloadUri.toString())
            } else {
                loading.set(false)
                failureMessage.postValue("Failed to add recipe. Please try again.")
            }
        }.addOnFailureListener {
            loading.set(false)
            failureMessage.postValue("Failed to add recipe. Please try again.")
        }
    }

    fun addRecipeCall() {
        if (uri == null) {
            addNewRecipe("")
        } else {
            uploadPhoto()
        }
    }

    fun addNewRecipe(image: String) {

        retry.set(false)
        loading.set(true)

        var id = UUID.randomUUID().toString()

        var obj = MealsItem()

        obj.strArea = area.get().toString()
        obj.idMeal = id
        obj.addedBy = Firebase.auth.currentUser!!.displayName.toString()
        obj.strMeal = name.get().toString()
        obj.strCategory = category.get().toString()
        obj.strYoutube = ytLink.get().toString()
        obj.strMealThumb = image
        obj.time = time.get()
        obj.rating = rating.get()

        for (i in 0..<selectedIngredients.size) {
            val ingredientField: Field =
                obj::class.java.getDeclaredField("strIngredient${i + 1}")


            ingredientField.isAccessible = true

            ingredientField.set(obj, selectedIngredients[i].strIngredient)

            val measureField: Field =
                obj::class.java.getDeclaredField("strMeasure${i + 1}")


            measureField.isAccessible = true

            measureField.set(obj, selectedIngredients[i].measure)

        }

        val ref = firestore.collection("new_recipes").document(id)

        ref.set(obj).addOnSuccessListener {
            successMessage.postValue("Recipe Added.")
        }.addOnFailureListener {
            loading.set(false)
            failureMessage.postValue("Failed to add recipe. Please try again.")
        }
    }

    fun getIngredientList() {
        loading.set(true)
        retry.set(false)
        compositeDisposable.add(
            repository.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<IngredientResponse>() {
                    override fun onNext(t: IngredientResponse) {
                        if (!t.meals.isNullOrEmpty()) {
                            ingredients.postValue(t.meals)
                        } else {
                            retry.set(true)
                        }
                        loading.set(false)
                    }

                    override fun onError(e: Throwable) {
                        retry.set(true)
                        loading.set(false)
                    }

                    override fun onComplete() {

                    }

                })

        )
    }

}