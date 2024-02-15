package com.tasty.recipeapp.model.response

data class Ingredient(
    val strDescription: String = "",
    val strIngredient: String = "",
    var measure: String = "",
    val strType: String = "",
    val idIngredient: String = ""
)