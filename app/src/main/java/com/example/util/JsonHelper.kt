package com.example.util

import com.example.data.CalendarEvent
import com.example.data.DateNote
import org.json.JSONArray
import org.json.JSONObject

data class ExportData(
    val events: List<CalendarEvent>,
    val notes: List<DateNote>
)

object JsonHelper {
    fun exportToJson(events: List<CalendarEvent>, notes: List<DateNote>): String {
        val root = JSONObject()
        root.put("version", 1)
        root.put("appName", "BanglaCalendar")
        root.put("exportTime", System.currentTimeMillis())

        val eventsArray = JSONArray()
        for (e in events) {
            val obj = JSONObject()
            obj.put("title", e.title)
            obj.put("date", e.date)
            obj.put("startTime", e.startTime)
            obj.put("endTime", e.endTime)
            obj.put("description", e.description)
            obj.put("colorHex", e.colorHex)
            obj.put("isImportant", e.isImportant)
            eventsArray.put(obj)
        }
        root.put("events", eventsArray)

        val notesArray = JSONArray()
        for (n in notes) {
            val obj = JSONObject()
            obj.put("date", n.date)
            obj.put("note", n.note)
            notesArray.put(obj)
        }
        root.put("notes", notesArray)

        return root.toString(2)
    }

    fun parseFromJson(jsonString: String): ExportData? {
        return try {
            val root = JSONObject(jsonString)
            val parsedEvents = mutableListOf<CalendarEvent>()
            val parsedNotes = mutableListOf<DateNote>()

            if (root.has("events")) {
                val eventsArray = root.getJSONArray("events")
                for (i in 0 until eventsArray.length()) {
                    val obj = eventsArray.getJSONObject(i)
                    parsedEvents.add(
                        CalendarEvent(
                            title = obj.optString("title", "Untitled"),
                            date = obj.optString("date", ""),
                            startTime = obj.optString("startTime", ""),
                            endTime = obj.optString("endTime", ""),
                            description = obj.optString("description", ""),
                            colorHex = obj.optString("colorHex", "#3F51B5"),
                            isImportant = obj.optBoolean("isImportant", false)
                        )
                    )
                }
            }

            if (root.has("notes")) {
                val notesArray = root.getJSONArray("notes")
                for (i in 0 until notesArray.length()) {
                    val obj = notesArray.getJSONObject(i)
                    val date = obj.optString("date", "")
                    val noteText = obj.optString("note", "")
                    if (date.isNotBlank() && noteText.isNotBlank()) {
                        parsedNotes.add(DateNote(date = date, note = noteText))
                    }
                }
            }

            ExportData(events = parsedEvents, notes = parsedNotes)
        } catch (e: Exception) {
            null
        }
    }
}
