package com.example.firebase.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase.model.Note
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun NoteItem(
    note: Note,
    onDelete: (Note) -> Unit,
    expandedNoteId: String?,
    onExpand: (String?) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val threshold = 150f
    val maxOffset = 250f

    LaunchedEffect(expandedNoteId) {
        if (expandedNoteId != note.id) {
            offsetX = 0f // Reset swipe position if another note expands
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        if (expandedNoteId != note.id) {
                            onExpand(note.id) // Expand only if it's not already expanded
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetX < -threshold) {
                                offsetX = -threshold // Stop at threshold
                            } else {
                                offsetX = 0f // Reset position
                                onExpand(null) // Close delete button
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newOffset = (offsetX + dragAmount).coerceIn(-maxOffset, 0f)
                            offsetX = newOffset
                        }
                    }
                )
            }
    ) {
        // 🔴 Delete Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                .clickable {
                    onDelete(note)
                    onExpand(null)
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Note",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 📝 Note Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = note.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = note.description, fontSize = 14.sp)
            }
        }
    }
}



/*
package com.example.firebase.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase.model.Note
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun NoteItem(
    note: Note,
    onDelete: (Note) -> Unit,
    expandedNoteId: String?,
    onExpand: (String?) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val threshold = 150f
    val maxOffset = 250f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 4.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        // Agar dusra item expanded hai toh usko collapse karo
                        if (expandedNoteId != note.id) {
                            onExpand(note.id)
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetX < -threshold) {
                                offsetX = -threshold // Swipe 25% tak le jane pe ruk jaye
                            } else {
                                offsetX = 0f // Reset swipe position
                                onExpand(null) // Close delete button
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newOffset = (offsetX + dragAmount).coerceIn(-maxOffset, 0f)
                            offsetX = newOffset
                        }
                    }
                )
            }
    ) {
        // 🔴 Delete Button (Always Clickable)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(end = 16.dp)
                .align(Alignment.Center)
                .background(Color.Red)
                .clickable {
                    println("Deleting note: ${note.id}")
                    onDelete(note)
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "delete note"
            )
        }

        // 📝 Note Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = note.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = note.description, fontSize = 14.sp)
            }
        }
    }
}

*/


/*
@Composable
fun NoteItem(note: Note) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = note.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.description, fontSize = 14.sp)
        }
    }
}
*/
