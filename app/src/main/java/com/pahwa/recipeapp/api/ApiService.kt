package com.pahwa.recipeapp.api

import com.pahwa.recipeapp.model.response.CategoriesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
    fun getRecipeCategories(
        @Url url: String
    ): Observable<CategoriesResponse>
}