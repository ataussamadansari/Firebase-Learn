package com.example.firebase.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.firebase.canvas.WavingText
import com.example.firebase.network.RetrofitClient
import com.example.firebase.repository.WallpaperRepository
import com.example.firebase.viewmodel.WallpaperViewModel
import com.example.firebase.viewmodel.WallpaperViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperScreen(navController: NavController) {
    val repository = remember { WallpaperRepository(RetrofitClient.apiService) }
    val viewModel: WallpaperViewModel = viewModel(factory = WallpaperViewModelFactory(repository))

    val wallpapers = viewModel.wallpapers.collectAsLazyPagingItems()

    /*LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(wallpapers.itemCount) { index ->
            wallpapers[index]?.let { wallpaper ->
                AsyncImage(
                    model = wallpaper.imageUrl,
                    contentDescription = "wallpapers",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)) // 🔹 Rounded corners
                )
            }
        }
    }*/

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Wallpaper")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (wallpapers.itemCount == 0) {
                WavingText()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(wallpapers.itemCount) { index ->
                        wallpapers[index]?.let { wallpaper ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(400.dp)
                            ) {
                                Box {
                                    SubcomposeAsyncImage(
                                        model = wallpaper.imageUrl,
                                        contentDescription = "wallpapers",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        loading = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                CircularProgressIndicator() // ✅ Loading indicator
                                            }
                                        }
                                    )
                                    if (wallpaper.category.equals("premium")) {
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(4.dp)
                                                .background(
                                                    if (!isSystemInDarkTheme()) Color.White else Color.Black,
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Spacer(Modifier.padding(2.dp))
                                            Icon(
                                                Icons.Default.Favorite,
                                                tint = Color(0xFFFF9800),
                                                contentDescription = "",
                                                modifier = Modifier
                                            )
                                            Text(
                                                text = "Pro",
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                            )
                                            Spacer(Modifier.padding(4.dp))
                                        }

                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}