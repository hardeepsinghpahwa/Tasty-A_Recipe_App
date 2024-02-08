package com.pahwa.recipeapp.model.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse (

    @SerializedName("categories" ) var categories : ArrayList<Categories> = arrayListOf()

)