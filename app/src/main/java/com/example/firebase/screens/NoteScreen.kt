package com.example.firebase.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteId: String? = null,
    notesViewModel: NotesViewModel,
    navController: NavController
) {
    // ✅ FocusRequester to move focus from Title → Description
    val descriptionFocusRequester = remember { FocusRequester() }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var screenName by remember { mutableStateOf("") }

    if (noteId.isNullOrEmpty()) {
        screenName = "Add Note"
    } else {
        screenName = "Edit Note"
    }

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
                title = { Text(screenName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            // 📝 Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { descriptionFocusRequester.requestFocus() } // ✅ Move focus
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 📝 Description Input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
                    .height(200.dp)
                    .focusRequester(descriptionFocusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Save Button
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        isLoading = true
                        if (!noteId.isNullOrEmpty()) {
                            // 🔹 Update existing note
                            Log.d("HomeScreenTAG", "Update existing note")
                            notesViewModel.updateNote(noteId, title, description) { success ->
                                isLoading = false
                                if (success) navController.popBackStack()
                            }
                        } else {
                            // 🔹 Add new note
                            Log.d("HomeScreenTAG", "Add new note")
                            notesViewModel.addNote(title, description) { success ->
                                isLoading = false
                                if (success) navController.popBackStack()
                            }
                        }
                    } else {
                        message = "Please fill all fields"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(
                    if (isLoading) "Saving..." else if (noteId.isNullOrEmpty()) "Save Note" else "Update Note"
                )
            }

            Text(
                text = message
            )
        }
    }
}

