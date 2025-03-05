package com.example.firebase.model

import com.google.gson.annotations.SerializedName

data class Wallpaper(
    val id: Int,
    @SerializedName("image_url") val imageUrl: String,
    val category: String
)

