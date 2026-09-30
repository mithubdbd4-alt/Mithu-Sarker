package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val date: String, // Format: YYYY-MM-DD
    val startTime: String = "",
    val endTime: String = "",
    val description: String = "",
    val colorHex: String = "#3F51B5",
    val isImportant: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
