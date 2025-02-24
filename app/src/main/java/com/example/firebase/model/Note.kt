package com.example.firebase.model

data class Note(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()  // ✅ Fix: Add timestamp
)
