package com.tasty.recipeapp.model.response

import com.google.gson.annotations.SerializedName

data class NotificationExtraData(@SerializedName("meal_id")var mealId: String = "", val firebase: String = "")