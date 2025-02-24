package com.example.firebase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun HomeScreen(authManager: AuthManager, navController: NavController) {

    val user = authManager.getCurrentUser()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        user?.let {
            Text("Name: ${it.displayName}")
            Text("Email: ${it.email}")
            Text("uid: ${it.uid}")
            it.photoUrl?.let {
                AsyncImage(model = it, contentDescription = "Profile Image")
            } ?: Text("No Profile Image")
        } ?: Text("User not logged in")

        Spacer(Modifier.padding(12.dp))

        Button(onClick = {
            authManager.logoutUser {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }) {
            Text("Logout")
        }
    }
}