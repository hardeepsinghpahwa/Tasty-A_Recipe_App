package com.tasty.recipeapp.model.response

import com.google.gson.annotations.SerializedName


data class UserDetails(

    @SerializedName("name") var name: String? = null,
    @SerializedName("profile_pic") var profilePic: String? = null,

)