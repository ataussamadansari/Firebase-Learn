package com.example.firebase.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.firebase.R
import com.example.firebase.model.Note
import com.example.firebase.viewmodel.NotesViewModel
import com.example.firebaseauth.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    notesViewModel: NotesViewModel,
    navController: NavController,
) {
    val userData by authViewModel.userData.collectAsState()
    val notesList by notesViewModel.notes.collectAsState() // Notes list from ViewModel
    var expandedNoteId by remember { mutableStateOf<String?>(null) }

    val name = userData?.get("name") as? String ?: "..."
    val profilePic = userData?.get("profilePic") as? String

    LaunchedEffect(Unit) {
        authViewModel.getUserData()
        notesViewModel.fetchNotes()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            //toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Welcome,")
                    Text(name, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.weight(1f))

                profilePic?.let {
                    AsyncImage(
                        model = it,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(40.dp)
                            .clickable { navController.navigate("profile") },
                        contentDescription = "User Profile",
                        placeholder = painterResource(R.drawable.ic_account_circle),
                        error = painterResource(R.drawable.ic_account_circle)
                    )
                } ?: Image(
                    painter = painterResource(R.drawable.ic_account_circle),
                    contentDescription = "Default Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("profile") }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Notes List
            LazyColumn {
                items(notesList) { note ->
//                    NoteItem(note, onDelete = {notesViewModel.deleteNote(note.id)})
                    NoteItem(
                        note = note,
                        onDelete = { notesViewModel.deleteNote(note.id) },
                        expandedNoteId = expandedNoteId,
                        onExpand = { id -> expandedNoteId = id }
                    )
                }
            }

        }

        // floating icons
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            shape = RoundedCornerShape(360.dp),
            onClick = {
                navController.navigate("note")
            }
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

