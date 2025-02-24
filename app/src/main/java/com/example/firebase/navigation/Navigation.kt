package com.example.firebase.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebase.screens.AuthManager
import com.example.firebase.screens.HomeScreen
import com.example.firebase.screens.LoginScreen
import com.example.firebase.screens.SignupScreen


@Composable
fun Navigation(modifier: Modifier = Modifier, activity: Activity) {
    val navController = rememberNavController()
    val authManager = AuthManager(activity)

    val startDestination = if (authManager.isUserLoggedIn()) "home" else "signup"

    NavHost(navController, startDestination = startDestination) {
//    NavHost(navController, startDestination = "signup") {
        composable("signup") { SignupScreen(authManager, navController) }
        composable("login") { LoginScreen(authManager, navController, activity) }
        composable("home") { HomeScreen(authManager, navController) }
    }
}