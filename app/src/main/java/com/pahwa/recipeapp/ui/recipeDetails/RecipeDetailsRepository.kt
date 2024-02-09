package com.pahwa.recipeapp.ui.recipeDetails

import com.pahwa.recipeapp.api.ApiService
import com.pahwa.recipeapp.model.response.SearchResponse
import io.reactivex.Observable
import javax.inject.Inject

class RecipeDetailsRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getRecipeDetails(id:String): Observable<SearchResponse> {
        return apiService.getRecipeDetails("http://www.themealdb.com/api/json/v1/1/lookup.php?i=$id")
    }

}