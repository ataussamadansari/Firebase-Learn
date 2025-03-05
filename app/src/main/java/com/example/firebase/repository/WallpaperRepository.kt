package com.example.firebase.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.firebase.model.Wallpaper
import com.example.firebase.network.ApiService
import kotlinx.coroutines.flow.Flow

class WallpaperRepository(private val apiService: ApiService) {
    fun getWallpapers(): Flow<PagingData<Wallpaper>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { WallpaperPagingSource(apiService) }
    ).flow
}