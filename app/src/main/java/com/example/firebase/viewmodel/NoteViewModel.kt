package com.example.firebase.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.model.Note
import com.example.firebase.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _notes = MutableStateFlow(emptyList<Note>())
    val notes: StateFlow<List<Note>> = _notes

    init {
        fetchNotes()
    }

    // 🔹 Get Notes for Current User
    fun fetchNotes() {
        viewModelScope.launch {
            val notesList = repository.getNotes()
            _notes.value = notesList
        }
    }

    // 🔹 Add Note (User-Specific)
    fun addNote(title: String, description: String, onComplete: (Boolean) -> Unit) {
        if (title.isNotBlank() && description.isNotBlank()) {
            viewModelScope.launch {
                repository.addNote(title, description)
                fetchNotes()
                onComplete(true)
            }
        } else {
            onComplete(false)
        }
    }

    // 🔹 Get Note by Id
    suspend fun getNoteById(noteId: String): Note? {
        return repository.getNoteById(noteId)
    }

    // 🔹 Update Note
    fun updateNote(noteId: String, title: String, description: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val document = repository.getNoteById(noteId) // 🔹 Note fetch karein
                document?.let { note ->
                    val updatedNote = note.copy(title = title, description = description)
                    repository.updateNote(updatedNote)
                    fetchNotes()
                    onComplete(true)
                } ?: onComplete(false)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    // 🔹 Delete Note
    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
            fetchNotes()
        }
    }
}
