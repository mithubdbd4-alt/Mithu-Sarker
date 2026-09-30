package com.example

import com.example.data.CalendarEvent
import com.example.data.DateNote
import com.example.util.AppLanguage
import com.example.util.DateUtils
import com.example.util.JsonHelper
import com.example.util.LanguageManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.YearMonth

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {

    @Test
    fun testBengaliDigitsConversion() {
        assertEquals("২০২৬", LanguageManager.toBengaliDigits("2026"))
        assertEquals("১৫", LanguageManager.toBengaliDigits("15"))
        assertEquals("০১২৩৪৫৬৭৮৯", LanguageManager.toBengaliDigits("0123456789"))
    }

    @Test
    fun testLanguageMonthsAndWeekdays() {
        // Bengali month & weekday checks
        assertEquals("মার্চ", LanguageManager.getMonthName(3, AppLanguage.BANGLA))
        assertEquals("March", LanguageManager.getMonthName(3, AppLanguage.ENGLISH))

        assertEquals("রবি", LanguageManager.getWeekdayName(0, AppLanguage.BANGLA))
        assertEquals("Sun", LanguageManager.getWeekdayName(0, AppLanguage.ENGLISH))
        assertEquals("শনি", LanguageManager.getWeekdayName(6, AppLanguage.BANGLA))
        assertEquals("Sat", LanguageManager.getWeekdayName(6, AppLanguage.ENGLISH))
    }

    @Test
    fun testJsonExportAndImport() {
        val events = listOf(
            CalendarEvent(
                id = 1,
                title = "টিম মিটিং",
                date = "2026-03-25",
                startTime = "10:00 AM",
                endTime = "11:00 AM",
                description = "প্রজেক্টের অগ্রগতি আলোচনা",
                colorHex = "#3F51B5",
                isImportant = true
            )
        )
        val notes = listOf(
            DateNote(date = "2026-03-25", note = "গুরুত্বপূর্ণ নোট")
        )

        val json = JsonHelper.exportToJson(events, notes)
        assertTrue(json.contains("টিম মিটিং"))
        assertTrue(json.contains("গুরুত্বপূর্ণ নোট"))

        val parsed = JsonHelper.parseFromJson(json)
        assertNotNull(parsed)
        assertEquals(1, parsed!!.events.size)
        assertEquals("টিম মিটিং", parsed.events[0].title)
        assertEquals(1, parsed.notes.size)
        assertEquals("গুরুত্বপূর্ণ নোট", parsed.notes[0].note)
    }

    @Test
    fun testMonthDaysGrid() {
        val ym = YearMonth.of(2026, 3)
        val selected = LocalDate.of(2026, 3, 25)
        val days = DateUtils.buildMonthDays(ym, selected)

        // Grid should be multiple of 7 (35 or 42 cells)
        assertTrue(days.size == 35 || days.size == 42)
        assertEquals(0, days.size % 7)

        val selectedDay = days.find { it.date == selected }
        assertNotNull(selectedDay)
        assertTrue(selectedDay!!.isSelected)
    }
}
