package com.example.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class CalendarRepository(private val dao: CalendarDao) {

    val allEvents: Flow<List<CalendarEvent>> = dao.getAllEvents()
    val allNotes: Flow<List<DateNote>> = dao.getAllNotes()

    fun getEventsForDate(date: String): Flow<List<CalendarEvent>> = dao.getEventsForDate(date)

    fun searchEvents(query: String): Flow<List<CalendarEvent>> = dao.searchEvents(query)

    suspend fun getEventById(id: Long): CalendarEvent? = dao.getEventById(id)

    suspend fun insertEvent(event: CalendarEvent): Long = dao.insertEvent(event)

    suspend fun insertEvents(events: List<CalendarEvent>) = dao.insertEvents(events)

    suspend fun updateEvent(event: CalendarEvent) = dao.updateEvent(event)

    suspend fun deleteEvent(event: CalendarEvent) = dao.deleteEvent(event)

    suspend fun deleteEventById(id: Long) = dao.deleteEventById(id)

    suspend fun clearAllEvents() = dao.clearAllEvents()

    fun getNoteForDate(date: String): Flow<DateNote?> = dao.getNoteForDate(date)

    suspend fun saveNote(date: String, note: String) {
        if (note.trim().isEmpty()) {
            dao.deleteNoteForDate(date)
        } else {
            dao.insertOrUpdateNote(DateNote(date = date, note = note.trim()))
        }
    }

    suspend fun deleteNote(date: String) = dao.deleteNoteForDate(date)

    suspend fun insertNotes(notes: List<DateNote>) = dao.insertNotes(notes)

    suspend fun clearAllNotes() = dao.clearAllNotes()

    suspend fun populateSampleDataIfEmpty() {
        val today = LocalDate.now(ZoneId.of("Asia/Dhaka"))
        val todayStr = today.toString()
        val tomorrowStr = today.plusDays(1).toString()
        val inThreeDaysStr = today.plusDays(3).toString()
        val nextWeekStr = today.plusDays(7).toString()

        val sampleEvents = listOf(
            CalendarEvent(
                title = "টিম স্ট্যান্ডআপ মিটিং (Sprint Sync)",
                date = todayStr,
                startTime = "10:00 AM",
                endTime = "10:30 AM",
                description = "দৈনিক প্রজেক্ট অগ্রগতি পর্যালোচনা ও টাস্ক বণ্টন।",
                colorHex = "#3F51B5",
                isImportant = true
            ),
            CalendarEvent(
                title = "ডাক্তারের সাথে ফলো-আপ অ্যাপয়েন্টমেন্ট",
                date = todayStr,
                startTime = "04:30 PM",
                endTime = "05:15 PM",
                description = "রুটিন স্বাস্থ্য পরীক্ষা এবং প্রেসক্রিপশন রিভিউ।",
                colorHex = "#E91E63",
                isImportant = false
            ),
            CalendarEvent(
                title = "ক্লায়েন্ট প্রজেক্ট প্রেজেন্টেশন",
                date = tomorrowStr,
                startTime = "11:00 AM",
                endTime = "12:30 PM",
                description = "নতুন ডিজাইন মকআপ ও ফিচার ডেমোনস্ট্রেশন।",
                colorHex = "#00897B",
                isImportant = true
            ),
            CalendarEvent(
                title = "পরিবারের সাথে ডিনার ও আড্ডা",
                date = inThreeDaysStr,
                startTime = "08:00 PM",
                endTime = "10:00 PM",
                description = "ধানমন্ডি লেকভিউ রেস্তোরাঁয় পারিবারিক পুনর্মিলন।",
                colorHex = "#FB8C00",
                isImportant = false
            ),
            CalendarEvent(
                title = "মাসিক বাজেট ও সঞ্চয় পর্যালোচনা",
                date = nextWeekStr,
                startTime = "03:00 PM",
                endTime = "04:00 PM",
                description = "মাসিক খরচের হিসাব এবং বিদ্যুৎ ও গ্যাস বিল পরিশোধ।",
                colorHex = "#8E24AA",
                isImportant = true
            )
        )

        val sampleNotes = listOf(
            DateNote(
                date = todayStr,
                note = "আজকের লক্ষ্য: ক্যালেন্ডার অ্যাপের সব ফিচার চমৎকারভাবে পরীক্ষা করা ও টিম মেম্বারদের মতামত নেওয়া।"
            ),
            DateNote(
                date = tomorrowStr,
                note = "উপস্থাপন ফাইল ও ল্যাপটপ চার্জার সাথে নিয়ে যেতে হবে।"
            )
        )

        dao.insertEvents(sampleEvents)
        dao.insertNotes(sampleNotes)
    }
}
