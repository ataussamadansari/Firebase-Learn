package com.example.firebase.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onOpen: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    expandedNoteId: String?,
    onExpand: (String?) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val threshold = 150f
    val maxOffset = 250f
    var isSwiped by remember { mutableStateOf(false) }

    LaunchedEffect(expandedNoteId) {
        if (expandedNoteId != note.id) {
            offsetX = 0f
            isSwiped = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        // 🔴 Delete Button (Always behind Note Card)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
                .background(Color.Red, shape = RoundedCornerShape(9.dp))
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

        // 📝 Note Card (On top of delete button)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            if (expandedNoteId != note.id) {
                                onExpand(note.id)
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX < -threshold) {
                                    offsetX = -threshold
                                    isSwiped = true
                                } else {
                                    offsetX = 0f
                                    onExpand(null)
                                    isSwiped = false
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                offsetX = (offsetX + dragAmount).coerceIn(-maxOffset, 0f)
                            }
                        }
                    )
                }
                .clickable(enabled = !isSwiped) {
                    onOpen(note)
                },
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.description,
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}
