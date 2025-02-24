package com.example.firebase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SignupScreen(authManager: AuthManager, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    // ✅ Agar user logged in hai, to Home Screen par redirect
    /*LaunchedEffect(Unit) {
        if (authManager.isUserLoggedIn()) {
            navController.navigate("home") {
                popUpTo("signup") { inclusive = true } // Back button se wapas na aaye
            }
        }
    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SignUp Page",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 30.sp
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(Modifier.padding(4.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.padding(4.dp))
        Button(
            onClick = {
                authManager.signUpUser(email, password) { success, msg ->
                    message = msg ?: ""
                    if (success) {
                        navController.navigate("login") // Navigate to Login Screen
                    }
                }
            }
        ) {
            Text("Sign Up")
        }
        Text(message, color = Color.Red)

        Spacer(Modifier.padding(4.dp))

        Button(onClick = { navController.navigate("login") }) {
            Text("Don't have an account? Login")
        }
    }
}
