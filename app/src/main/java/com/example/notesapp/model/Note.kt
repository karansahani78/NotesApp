package com.example.notesapp.model
data class Note(
    val id: Long? = null,
    val title: String,
    val content: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
