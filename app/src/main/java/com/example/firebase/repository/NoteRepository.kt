package com.example.firebase.repository

import android.util.Log
import com.example.firebase.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await


class NotesRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val notesCollection = db.collection("notes")

    // ✅ 1. Create Note (User ID के साथ)
    suspend fun addNote(title: String, description: String) {
        val userId = auth.currentUser?.uid ?: return
        val newNoteRef = notesCollection.document()
        val newNote = Note(
            id = newNoteRef.id,
            userId = userId,
            title = title,
            description = description,
            timestamp = System.currentTimeMillis()  // ✅ Fix: Ensure timestamp exists
        )
        newNoteRef.set(newNote).await()
    }

    /* suspend fun addNote(title: String, description: String) {
         val userId = auth.currentUser?.uid ?: return  // 🔹 User Login नहीं है तो return कर दो
         val newNoteRef = notesCollection.document()
         val newNote = Note(id = newNoteRef.id, userId = userId, title = title, description = description)
         newNoteRef.set(newNote).await()
     }*/

    // ✅ 2. Get Notes (सिर्फ Current User के Notes लाने के लिए)
    suspend fun getNotes(): List<Note> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        val snapshot = notesCollection.whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
        return snapshot.documents.mapNotNull { doc ->
            val note = doc.toObject(Note::class.java)
            note
        }
    }

    /*suspend fun getNotes(): List<Note> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        return notesCollection.whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
            .documents.mapNotNull { it.toObject(Note::class.java) }
    }*/

    // ✅ 3. Update Note

    // 🔹 Note ko Firestore se laane ka function
    suspend fun getNoteById(noteId: String): Note? {
        return try {
            val snapshot = notesCollection.document(noteId).get().await()
            snapshot.toObject(Note::class.java)?.copy(id = snapshot.id)
        } catch (e: Exception) {
            null
        }
    }

    // 🔹 Update Note with Firestore Merge (so fields don’t get deleted)
    suspend fun updateNote(note: Note) {
        if (note.id.isNotEmpty()) {
            notesCollection.document(note.id)
                .set(note, SetOptions.merge()) // ✅ Merge only updated fields
                .await()
        }
    }
    /*suspend fun updateNote(note: Note) {
        if (note.id.isNotEmpty()) {
            notesCollection.document(note.id).set(note).await()
        }
    }*/

    // ✅ 4. Delete Note
    suspend fun deleteNote(noteId: String) {
        notesCollection.document(noteId).delete().await()
    }
}