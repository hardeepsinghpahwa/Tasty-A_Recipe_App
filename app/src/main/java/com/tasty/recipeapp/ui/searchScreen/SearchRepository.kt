package com.tasty.recipeapp.ui.searchScreen

import com.tasty.recipeapp.api.ApiService
import com.tasty.recipeapp.model.response.SearchResponse
import io.reactivex.Observable
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun searchRecipes(query: String): Observable<SearchResponse> {
        return apiService.searchRecipes("http://www.themealdb.com/api/json/v1/1/search.php?s=${query}")
    }

}