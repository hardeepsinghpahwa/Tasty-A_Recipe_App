package com.tasty.recipeapp.api

import com.tasty.recipeapp.model.response.CategoriesResponse
import com.tasty.recipeapp.model.response.IngredientResponse
import com.tasty.recipeapp.model.response.RecipeResponse
import com.tasty.recipeapp.model.response.SearchResponse
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
    fun getIngredients(
        @Url url: String
    ): Observable<IngredientResponse>

    @GET
    fun searchRecipes(
        @Url url: String
    ): Observable<SearchResponse>

    @GET
    fun getRecipeDetails(
        @Url url: String
    ): Observable<SearchResponse>
}