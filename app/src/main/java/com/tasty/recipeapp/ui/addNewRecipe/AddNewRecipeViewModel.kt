package com.tasty.recipeapp.ui.addNewRecipe

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tasty.recipeapp.model.response.Ingredient
import com.tasty.recipeapp.model.response.IngredientResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class AddNewRecipeViewModel (
    val firestore: FirebaseFirestore,
    val repository: AddNewRecipeRepository,
    val compositeDisposable: CompositeDisposable
) : ViewModel() {


    var ingredients=MutableLiveData<ArrayList<Ingredient>>()

    var name=ObservableField("")
    var ytLink=ObservableField("")
    var area=ObservableField("")
    var category=ObservableField("")
    val retry = ObservableField(false)
    val loading = ObservableField(false)


    fun getIngredients():LiveData<ArrayList<Ingredient>>{
        return ingredients
    }

    fun addNewRecipe(){

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