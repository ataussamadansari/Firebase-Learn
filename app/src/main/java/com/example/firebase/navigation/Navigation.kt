package com.example.firebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebase.factory.NotesViewModelFactory
import com.example.firebase.repository.NotesRepository
import com.example.firebase.screens.HomeScreen
import com.example.firebase.screens.LoginScreen
import com.example.firebase.screens.NoteScreen
import com.example.firebase.screens.NoteShowScreen
import com.example.firebase.screens.ProfileScreen
import com.example.firebase.screens.SignupScreen
import com.example.firebase.screens.SplashScreen
import com.example.firebase.screens.WallpaperScreen
import com.example.firebase.viewmodel.NotesViewModel
import com.example.firebaseauth.manager.AuthManager
import com.example.firebaseauth.manager.UserRepository
import com.example.firebaseauth.viewmodel.AuthViewModel
import com.example.firebaseauth.viewmodel.AuthViewModelFactory

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    authManager: AuthManager,
    userRepository: UserRepository
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authManager, userRepository)
    )

    val noteViewModel: NotesViewModel =
        viewModel(factory = NotesViewModelFactory(NotesRepository()))

    val startDestination = "splash"
//    val startDestination = if (authViewModel.isUserLoggedIn()) "home" else "login"

    NavHost(navController, startDestination = startDestination) {
        composable("splash") { SplashScreen(authViewModel, navController) }
        composable("signup") { SignupScreen(authViewModel, navController) }
        composable("login") { LoginScreen(authViewModel, navController) }
        composable("home") { HomeScreen(authViewModel, noteViewModel, navController) }
        composable("profile") { ProfileScreen(authViewModel, navController) }
        composable("note") { NoteScreen("", noteViewModel, navController) }
        composable("note/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteScreen(noteId, noteViewModel, navController)
        }
        composable("noteShow/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteShowScreen(noteId, noteViewModel, navController)
        }
        composable("wallpaper") { WallpaperScreen(navController) }
    }
}
