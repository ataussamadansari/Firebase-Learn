package com.example.firebase.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.firebaseauth.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.getUserData()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back Btn",
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 8.dp, bottom = 8.dp)
                    .clickable(
                        onClick = { navController.popBackStack() }
                    )
            )
//            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Profile",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                authViewModel.logout {
                    navController.navigate("login") { popUpTo(0) }
                }
            }) {
                Text("Logout")
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 16.dp)
               .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = userData?.get("profilePic"),
                contentDescription = "User profile picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Text(
                text = userData?.get("name").toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = userData?.get("email").toString(),
                fontSize = 18.sp
            )
        }

    }
}