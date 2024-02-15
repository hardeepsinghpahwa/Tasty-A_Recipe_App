package com.tasty.recipeapp.ui.recipeDetails

import android.content.ContentValues.TAG
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.tasty.recipeapp.model.response.MealsItem
import com.tasty.recipeapp.model.response.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Field


class RecipeDetailsViewModel(
    private val repository: RecipeDetailsRepository,
    private val compositeDisposable: CompositeDisposable,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val recipes: MutableLiveData<ArrayList<MealsItem>> =
        MutableLiveData<ArrayList<MealsItem>>()
    val retry = ObservableField(false)
    val loading = ObservableField(false)

    val image = MutableLiveData<String>()

    val name = ObservableField("")
    val subTitle = ObservableField("")
    val instructions = ObservableField("")
    val ingredients = ObservableField("")

    val ingredientsImgs = MutableLiveData<ArrayList<String>>()


    fun getRecipes(): LiveData<ArrayList<MealsItem>> {
        return recipes
    }


    fun getIngredientImages(): LiveData<ArrayList<String>> {
        return ingredientsImgs
    }

    fun getImage(): LiveData<String> {
        return image
    }

    fun getRecipeDetails(id: String) {
        loading.set(true)
        retry.set(false)
        compositeDisposable.add(
            repository.getRecipeDetails(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(t: SearchResponse) {
                        if (!t.meals.isNullOrEmpty()) {
                            recipes.postValue(t.meals)

                            val recipe = t.meals[0]

                            name.set(recipe.strMeal)
                            subTitle.set(recipe.strArea + " " + recipe.strCategory)
                            image.postValue(recipe.strMealThumb)
                            val instrn: String = recipe.strInstructions.replace("\n", "\n• ")

                            instructions.set("• $instrn")


                            var ing = ""
                            var list = arrayListOf<String>()

                            for (i in 0..20) {


                                val ingField: Field =
                                    recipe::class.java.getDeclaredField("strIngredient${i + 1}")
                                ingField.isAccessible = true

                                val measField: Field =
                                    recipe::class.java.getDeclaredField("strMeasure${i + 1}")
                                measField.isAccessible = true


                                val ingObject: String =
                                    ingField.get(recipe) as String? ?: break

                                if (ingObject.isEmpty()) {
                                    break
                                }

                                val measObject: String =
                                    measField.get(recipe) as String? ?: break


                                list.add(ingObject)
                                ing += "•  $measObject $ingObject\n"

                            }

                            ingredients.set(ing)
                            ingredientsImgs.postValue(list)

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

    fun getRecipeDetailsFromFirebase(id: String) {
        loading.set(true)
        retry.set(false)


        firestore.collection("new_recipes").document(id).get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    var recipe = document.toObject<MealsItem>()

                    if (recipe != null) {
                        name.set(recipe.strMeal)
                        subTitle.set(recipe.strArea + " " + recipe.strCategory)
                        image.postValue(recipe.strMealThumb)

                    }
                    loading.set(false)
                } else {
                    loading.set(false)
                    retry.set(true)
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                loading.set(false)
                retry.set(true)
                Log.d(TAG, "get failed with ", exception)
            }
    }


}