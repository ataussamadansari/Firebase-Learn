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

    /*fun fetchNotes() {
        viewModelScope.launch {
            _notes.value = repository.getNotes()
        }
    }*/

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

    // 🔹 Update Note
    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
            fetchNotes()
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
