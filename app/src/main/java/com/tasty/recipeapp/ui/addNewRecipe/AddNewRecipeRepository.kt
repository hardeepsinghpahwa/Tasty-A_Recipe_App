package com.tasty.recipeapp.ui.addNewRecipe

import com.tasty.recipeapp.api.ApiService
import com.tasty.recipeapp.model.response.IngredientResponse
import io.reactivex.Observable
import javax.inject.Inject

class AddNewRecipeRepository @Inject constructor(private val apiService: ApiService) {

    fun getIngredients(): Observable<IngredientResponse> {
        return apiService.getIngredients("http://www.themealdb.com/api/json/v1/1/list.php?i=list")
    }
}