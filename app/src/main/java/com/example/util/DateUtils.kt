package com.example.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean
)

object DateUtils {
    val DHAKA_ZONE: ZoneId = ZoneId.of("Asia/Dhaka")

    fun getTodayInDhaka(): LocalDate {
        return LocalDate.now(DHAKA_ZONE)
    }

    fun getNowInDhaka(): ZonedDateTime {
        return ZonedDateTime.now(DHAKA_ZONE)
    }

    fun parseDate(dateStr: String): LocalDate? {
        return try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            null
        }
    }

    fun formatDateIso(date: LocalDate): String {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    fun formatHeaderMonthYear(yearMonth: YearMonth, language: AppLanguage): String {
        val monthName = LanguageManager.getMonthName(yearMonth.monthValue, language)
        val yearFormatted = LanguageManager.formatNumber(yearMonth.year, language)
        return "$monthName $yearFormatted"
    }

    fun formatFullDate(date: LocalDate, language: AppLanguage): String {
        val dayNumber = LanguageManager.formatNumber(date.dayOfMonth, language)
        val monthName = LanguageManager.getMonthName(date.monthValue, language)
        val yearNumber = LanguageManager.formatNumber(date.year, language)

        val dayOfWeekIndex = if (date.dayOfWeek == DayOfWeek.SUNDAY) 0 else date.dayOfWeek.value
        val weekday = LanguageManager.getWeekdayName(dayOfWeekIndex, language)

        return if (language == AppLanguage.BANGLA) {
            "$weekday, $dayNumber $monthName $yearNumber"
        } else {
            "$weekday, $monthName $dayNumber, $yearNumber"
        }
    }

    fun getRelativeBadge(dateStr: String, language: AppLanguage): String {
        val target = parseDate(dateStr) ?: return dateStr
        val today = getTodayInDhaka()

        return when {
            target.isEqual(today) -> Strings.today(language)
            target.isEqual(today.plusDays(1)) -> Strings.tomorrow(language)
            target.isAfter(today) && target.isBefore(today.plusDays(8)) -> Strings.nextWeek(language)
            else -> {
                val day = LanguageManager.formatNumber(target.dayOfMonth, language)
                val month = LanguageManager.getMonthName(target.monthValue, language)
                "$day $month"
            }
        }
    }

    /**
     * Generates a 35 or 42 item grid for Sunday-first month view.
     */
    fun buildMonthDays(yearMonth: YearMonth, selectedDate: LocalDate): List<CalendarDay> {
        val today = getTodayInDhaka()
        val firstDayOfMonth = yearMonth.atDay(1)
        val daysInMonth = yearMonth.lengthOfMonth()

        // Sunday is 0, Monday is 1, ..., Saturday is 6
        val firstDayOffset = if (firstDayOfMonth.dayOfWeek == DayOfWeek.SUNDAY) 0 else firstDayOfMonth.dayOfWeek.value

        val prevMonth = yearMonth.minusMonths(1)
        val daysInPrevMonth = prevMonth.lengthOfMonth()

        val days = mutableListOf<CalendarDay>()

        // Previous month filler days
        for (i in (firstDayOffset - 1) downTo 0) {
            val date = prevMonth.atDay(daysInPrevMonth - i)
            days.add(
                CalendarDay(
                    date = date,
                    isCurrentMonth = false,
                    isToday = date.isEqual(today),
                    isSelected = date.isEqual(selectedDate)
                )
            )
        }

        // Current month days
        for (day in 1..daysInMonth) {
            val date = yearMonth.atDay(day)
            days.add(
                CalendarDay(
                    date = date,
                    isCurrentMonth = true,
                    isToday = date.isEqual(today),
                    isSelected = date.isEqual(selectedDate)
                )
            )
        }

        // Next month filler days (fill up to multiple of 7, min 35 or 42)
        val totalCells = if (days.size > 35) 42 else 35
        val nextMonth = yearMonth.plusMonths(1)
        var nextMonthDay = 1
        while (days.size < totalCells) {
            val date = nextMonth.atDay(nextMonthDay)
            days.add(
                CalendarDay(
                    date = date,
                    isCurrentMonth = false,
                    isToday = date.isEqual(today),
                    isSelected = date.isEqual(selectedDate)
                )
            )
            nextMonthDay++
        }

        return days
    }
}
