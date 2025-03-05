package com.example.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.firebase.model.Wallpaper
import com.example.firebase.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow

class WallpaperViewModel(private val repository: WallpaperRepository) : ViewModel() {
    val wallpapers: Flow<PagingData<Wallpaper>> = repository.getWallpapers().cachedIn(viewModelScope)
}

// ✅ ViewModel Factory (since no DI is used)
class WallpaperViewModelFactory(private val repository: WallpaperRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WallpaperViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WallpaperViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}