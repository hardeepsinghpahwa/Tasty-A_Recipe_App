package com.pahwa.recipeapp.api

import com.pahwa.recipeapp.model.response.CategoriesResponse
import com.pahwa.recipeapp.model.response.RecipeResponse
import com.pahwa.recipeapp.model.response.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
    fun getRecipeCategories(
        @Url url: String
    ): Observable<CategoriesResponse>

    @GET
    fun getRecipes(
        @Url url: String
    ): Observable<RecipeResponse>

    @GET
    fun searchRecipes(
        @Url url: String
    ): Observable<SearchResponse>
}