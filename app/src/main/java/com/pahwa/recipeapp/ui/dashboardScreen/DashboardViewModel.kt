package com.pahwa.recipeapp.ui.dashboardScreen

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pahwa.recipeapp.model.response.Categories
import com.pahwa.recipeapp.model.response.CategoriesResponse
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
    val retry = ObservableField(false)
    val loading = ObservableField(false)

    fun getRepos(): LiveData<ArrayList<Categories>> {
        return categories
    }

    fun getCategories() {
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

    override fun onCleared() {
        super.onCleared()
        if (compositeDisposable != null) {
            compositeDisposable.clear()
        }
    }
}