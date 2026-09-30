package com.example.util

enum class AppLanguage(val code: String, val displayName: String) {
    BANGLA("bn", "বাংলা"),
    ENGLISH("en", "English")
}

object LanguageManager {
    val BANGLA_MONTHS = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল",
        "মে", "জুন", "জুলাই", "আগস্ট",
        "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )

    val ENGLISH_MONTHS = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    // Sunday to Saturday order
    val BANGLA_WEEKDAYS = listOf(
        "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহস্পতি", "শুক্র", "শনি"
    )

    val ENGLISH_WEEKDAYS = listOf(
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    )

    fun toBengaliDigits(input: String): String {
        val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val sb = StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                sb.append(bengaliDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun toEnglishDigits(input: String): String {
        val bengaliDigits = "০১২৩৪৫৬৭৮৯"
        val sb = StringBuilder()
        for (ch in input) {
            val idx = bengaliDigits.indexOf(ch)
            if (idx != -1) {
                sb.append(idx)
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun formatNumber(number: Int, language: AppLanguage): String {
        val str = number.toString()
        return if (language == AppLanguage.BANGLA) toBengaliDigits(str) else str
    }

    fun getMonthName(monthNumber: Int, language: AppLanguage): String {
        val index = (monthNumber - 1).coerceIn(0, 11)
        return if (language == AppLanguage.BANGLA) BANGLA_MONTHS[index] else ENGLISH_MONTHS[index]
    }

    fun getWeekdayName(dayIndex: Int, language: AppLanguage): String {
        val index = dayIndex.coerceIn(0, 6)
        return if (language == AppLanguage.BANGLA) BANGLA_WEEKDAYS[index] else ENGLISH_WEEKDAYS[index]
    }
}
