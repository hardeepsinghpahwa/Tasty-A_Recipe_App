package com.pahwa.recipeapp.ui.dashboardScreen

import com.pahwa.recipeapp.api.ApiService
import com.pahwa.recipeapp.model.response.CategoriesResponse
import com.pahwa.recipeapp.model.response.RecipeResponse
import io.reactivex.Observable
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getCategories(): Observable<CategoriesResponse> {
        return apiService.getRecipeCategories("http://www.themealdb.com/api/json/v1/1/categories.php")
    }

    fun getRecipes(categoryName: String): Observable<RecipeResponse> {
        return apiService.getRecipes("http://www.themealdb.com/api/json/v1/1/filter.php?c=${categoryName}")
    }
}