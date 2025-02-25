package com.example.firebase.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteShowScreen(
    noteId: String? = null,
    notesViewModel: NotesViewModel,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // 🔹 Fetch existing note data if noteId is available
    LaunchedEffect(noteId) {
        noteId?.let {
            val note = notesViewModel.getNoteById(it) // ✅ Fetch note
            note?.let {
                title = it.title
                description = it.description
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("note/${noteId}") }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Update")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = description, fontSize = 14.sp)
        }
    }
    /* Column(
         modifier = modifier
             .fillMaxSize()
     ) {
         // 🔙 Back Button & Title
         Row(
             modifier = Modifier.fillMaxWidth(),
             verticalAlignment = Alignment.CenterVertically
         ) {
             Icon(
                 Icons.Default.ArrowBack,
                 contentDescription = "Back",
                 modifier = Modifier
                     .size(48.dp)
                     .padding(8.dp)
                     .clickable { navController.popBackStack() }
             )
             Spacer(modifier = Modifier.width(8.dp))
             Text(text = "Note", fontSize = 24.sp)
             Spacer(modifier = Modifier.weight(1f))
             Icon(
                 Icons.Default.Edit,
                 tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                 contentDescription = "Edit",
                 modifier = Modifier
                     .size(48.dp)
                     .padding(12.dp)
                     .clickable(
                         onClick = {
                             navController.navigate("note/${noteId}")
                         }
                     )
             )
         }

         Column(
             modifier = Modifier
                 .padding(16.dp)
         ) {
             Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
             Spacer(Modifier.padding(4.dp))
             Text(text = description, fontSize = 14.sp)
         }

     }*/
}