package com.pahwa.recipeapp.ui.searchScreen

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pahwa.recipeapp.model.response.MealsItem
import com.pahwa.recipeapp.model.response.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val repository: SearchRepository,private val compositeDisposable: CompositeDisposable) : ViewModel() {

    private val searchResults: MutableLiveData<ArrayList<MealsItem>> =
        MutableLiveData<ArrayList<MealsItem>>()
    val retry = ObservableField(false)
    val loading = ObservableField(false)
    val searchText = ObservableField("")
    val searchTitle = ObservableField("Recent Search")

    fun getSearchResults(): LiveData<ArrayList<MealsItem>> {
        return searchResults
    }

    fun searchData() {
        searchRecipes(searchText.get().toString())
    }

    private fun searchRecipes(query: String) {
        retry.set(false)
        loading.set(true)
        compositeDisposable.add(
            repository.searchRecipes(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(t: SearchResponse) {

                        if (!t.meals.isNullOrEmpty()) {

                            searchTitle.set("Search Results (${t.meals.size})")
                            searchResults.postValue(t.meals)

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