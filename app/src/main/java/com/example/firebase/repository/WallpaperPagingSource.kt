package com.example.firebase.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.firebase.model.Wallpaper
import com.example.firebase.network.ApiService
import retrofit2.HttpException
import java.io.IOException

/*
class WallpaperPagingSource(private val apiService: ApiService) : PagingSource<Int, Wallpaper>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        return try {
            val response = apiService.getWallpapers()
            LoadResult.Page(
                data = response.wallpapers,
                prevKey = null,
                nextKey = null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? = null
}*/

class WallpaperPagingSource(private val apiService: ApiService) : PagingSource<Int, Wallpaper>() {

    private var cachedWallpapers: List<Wallpaper> = emptyList() // Cached data

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            // Agar cache empty hai toh server se fetch kar
            if (cachedWallpapers.isEmpty()) {
                val response = apiService.getWallpapers()
                cachedWallpapers = response.wallpapers.shuffled()
            }

            // Pagination logic: List ka ek chunk return karo
            val startIndex = (page - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, cachedWallpapers.size)

            val data = if (startIndex < cachedWallpapers.size) {
                cachedWallpapers.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (endIndex >= cachedWallpapers.size) null else page + 1
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? = null
}
