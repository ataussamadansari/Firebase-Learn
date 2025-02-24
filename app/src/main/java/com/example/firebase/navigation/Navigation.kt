package com.example.firebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebase.factory.NotesViewModelFactory
import com.example.firebase.repository.NotesRepository
import com.example.firebase.screens.AddNoteScreen
import com.example.firebase.screens.HomeScreen
import com.example.firebase.screens.LoginScreen
import com.example.firebase.screens.ProfileScreen
import com.example.firebase.screens.SignupScreen
import com.example.firebase.viewmodel.NotesViewModel
import com.example.firebaseauth.manager.AuthManager
import com.example.firebaseauth.manager.UserRepository
import com.example.firebaseauth.viewmodel.AuthViewModel
import com.example.firebaseauth.viewmodel.AuthViewModelFactory

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    authManager: AuthManager,
    userRepository: UserRepository,
    notesRepository: NotesRepository
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authManager, userRepository)
    )

    val noteViewModel: NotesViewModel = viewModel(factory = NotesViewModelFactory(NotesRepository()))


    val startDestination = if (authViewModel.isUserLoggedIn()) "home" else "signup"

    NavHost(navController, startDestination = startDestination) {
        composable("signup") { SignupScreen(authViewModel, navController) }
        composable("login") { LoginScreen(authViewModel, navController) }
        composable("home") { HomeScreen(modifier, authViewModel, noteViewModel, navController) }
        composable("profile") { ProfileScreen(modifier, authViewModel, navController) }
        composable("note") { AddNoteScreen(modifier, noteViewModel, navController) }
    }
}
