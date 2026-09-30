package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_notes")
data class DateNote(
    @PrimaryKey
    val date: String, // Format: YYYY-MM-DD
    val note: String,
    val updatedAt: Long = System.currentTimeMillis()
)
