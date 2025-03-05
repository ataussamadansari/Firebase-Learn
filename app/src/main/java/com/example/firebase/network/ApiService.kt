package com.example.firebase.network

import com.example.firebase.model.Wallpaper
import retrofit2.http.GET

data class WallpaperResponse(val wallpapers: List<Wallpaper>)

interface ApiService {
    @GET("app/api/images")
    suspend fun getWallpapers(): WallpaperResponse
}
