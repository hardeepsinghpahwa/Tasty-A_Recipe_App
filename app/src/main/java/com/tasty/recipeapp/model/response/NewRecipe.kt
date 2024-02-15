package com.tasty.recipeapp.model.response

data class NewRecipe(
    val area: String = "",
    val chef: String = "",
    val instructions: String = "",
    val name: String = "",
    val rating: Int = 0,
    val time: String = "",
    val id: String = "",
    val type: String = "",
    val image: String = "",
    val amounts: List<String> = arrayListOf<String>(),
    val ingredients: List<String> = arrayListOf<String>(),
)