package com.example.firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.firebase.navigation.Navigation
import com.example.firebase.ui.theme.FirebaseLearnTheme
import com.example.firebaseauth.manager.AuthManager
import com.example.firebaseauth.manager.UserRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userRepository = UserRepository()
        val authManager = AuthManager(this, userRepository)

        setContent {
            FirebaseLearnTheme {

                Scaffold {padding->
                    Navigation(
                        modifier = Modifier.padding(paddingValues = padding),
                        authManager = authManager,
                        userRepository = userRepository,
                    )
                }
            }
        }
    }
}
