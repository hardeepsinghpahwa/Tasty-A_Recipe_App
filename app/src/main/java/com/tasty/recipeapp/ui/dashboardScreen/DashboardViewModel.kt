package com.tasty.recipeapp.ui.dashboardScreen

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipeapp.model.response.Categories
import com.tasty.recipeapp.model.response.CategoriesResponse
import com.tasty.recipeapp.model.response.MealData
import com.tasty.recipeapp.model.response.RecipeResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class DashboardViewModel(
    private val repository: DashboardRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val categories: MutableLiveData<ArrayList<Categories>> =
        MutableLiveData<ArrayList<Categories>>()
    private val recipes: MutableLiveData<ArrayList<MealData>> =
        MutableLiveData<ArrayList<MealData>>()
    val retry = ObservableField(false)
    val loading = ObservableField(false)

    fun getCategories(): LiveData<ArrayList<Categories>> {
        return categories
    }

    fun getRecipes(): LiveData<ArrayList<MealData>> {
        return recipes
    }


    fun getCategoriesData() {
        loading.set(true)
        retry.set(false)
        compositeDisposable.add(
            repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CategoriesResponse>() {
                    override fun onNext(t: CategoriesResponse) {
                        if (!t.categories.isNullOrEmpty()) {
                            categories.postValue(t.categories)
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

    fun getRecipeData(categoryName:String) {
        loading.set(true)
        retry.set(false)
        compositeDisposable.add(
            repository.getRecipes(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<RecipeResponse>() {
                    override fun onNext(t: RecipeResponse) {
                        if (!t.meals.isNullOrEmpty()) {
                            recipes.postValue(t.meals)
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

    override fun onCleared() {
        super.onCleared()
        if (compositeDisposable != null) {
            compositeDisposable.clear()
        }
    }
}