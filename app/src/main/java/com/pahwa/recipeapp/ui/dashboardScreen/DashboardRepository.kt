package com.pahwa.recipeapp.ui.dashboardScreen

import com.pahwa.recipeapp.api.ApiService
import com.pahwa.recipeapp.model.response.CategoriesResponse
import io.reactivex.Observable
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    val apiService: ApiService
) {

    fun getCategories(): Observable<CategoriesResponse> {
        return apiService.getRecipeCategories("http://www.themealdb.com/api/json/v1/1/categories.php")
    }
}