package com.example.util

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.data.CalendarEvent
import com.example.data.DateNote
import java.time.YearMonth

object PrintHelper {
    fun printCalendar(
        context: Context,
        yearMonth: YearMonth,
        events: List<CalendarEvent>,
        notes: List<DateNote>,
        language: AppLanguage
    ) {
        val monthTitle = DateUtils.formatHeaderMonthYear(yearMonth, language)
        val monthEvents = events.filter { it.date.startsWith(yearMonth.toString()) }
            .sortedBy { it.date }

        val htmlContent = buildString {
            append("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <title>$monthTitle - Calendar</title>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 24px; color: #1e293b; }
                        .header { text-align: center; margin-bottom: 24px; border-bottom: 2px solid #3b82f6; padding-bottom: 12px; }
                        h1 { margin: 0; color: #1e3a8a; font-size: 26px; }
                        .subtitle { color: #64748b; font-size: 14px; margin-top: 4px; }
                        table { width: 100%; border-collapse: collapse; margin-top: 16px; }
                        th { background-color: #f1f5f9; color: #334155; text-align: left; padding: 10px 12px; border: 1px solid #cbd5e1; font-size: 13px; }
                        td { padding: 10px 12px; border: 1px solid #cbd5e1; vertical-align: top; font-size: 13px; }
                        tr:nth-child(even) { background-color: #f8fafc; }
                        .badge { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; color: white; margin-right: 6px; }
                        .important { background-color: #ef4444; }
                        .time { color: #475569; font-weight: 600; font-size: 12px; }
                        .notes-section { margin-top: 28px; padding-top: 16px; border-top: 1px solid #e2e8f0; }
                        .note-item { background: #fefce8; border-left: 4px solid #facc15; padding: 8px 12px; margin-bottom: 8px; font-size: 13px; }
                        .footer { margin-top: 30px; text-align: center; font-size: 11px; color: #94a3b8; }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h1>$monthTitle</h1>
                        <div class="subtitle">${Strings.appTitle(language)} • ${Strings.timezoneBadge(language)}</div>
                    </div>
                    
                    <h3>${Strings.totalEvents(language)} (${LanguageManager.formatNumber(monthEvents.size, language)})</h3>
                    <table>
                        <thead>
                            <tr>
                                <th style="width: 15%;">${Strings.date(language)}</th>
                                <th style="width: 20%;">${Strings.startTime(language)} / ${Strings.endTime(language)}</th>
                                <th style="width: 35%;">${Strings.eventTitle(language)}</th>
                                <th style="width: 30%;">${Strings.description(language)}</th>
                            </tr>
                        </thead>
                        <tbody>
            """.trimIndent())

            if (monthEvents.isEmpty()) {
                append("<tr><td colspan='4' style='text-align:center; padding: 20px; color:#64748b;'>${Strings.noEventsForDate(language)}</td></tr>")
            } else {
                for (event in monthEvents) {
                    val formattedDate = DateUtils.parseDate(event.date)?.let {
                        DateUtils.formatFullDate(it, language)
                    } ?: event.date

                    val importantTag = if (event.isImportant) "<span class='badge important'>★ ${Strings.importantEvents(language)}</span>" else ""
                    val timeStr = if (event.startTime.isNotBlank()) "${event.startTime} - ${event.endTime}" else "-"

                    append("""
                        <tr>
                            <td><strong>$formattedDate</strong></td>
                            <td class="time">$timeStr</td>
                            <td>
                                <span class="badge" style="background-color: ${event.colorHex};">●</span>
                                <strong>${event.title}</strong>
                                $importantTag
                            </td>
                            <td>${event.description.ifBlank { "-" }}</td>
                        </tr>
                    """.trimIndent())
                }
            }

            append("""
                        </tbody>
                    </table>
            """.trimIndent())

            val monthNotes = notes.filter { it.date.startsWith(yearMonth.toString()) }
            if (monthNotes.isNotEmpty()) {
                append("""
                    <div class="notes-section">
                        <h3>${Strings.notes(language)}</h3>
                """.trimIndent())
                for (note in monthNotes) {
                    val dateStr = DateUtils.parseDate(note.date)?.let {
                        DateUtils.formatFullDate(it, language)
                    } ?: note.date
                    append("""
                        <div class="note-item">
                            <strong>$dateStr:</strong> ${note.note}
                        </div>
                    """.trimIndent())
                }
                append("</div>")
            }

            append("""
                    <div class="footer">
                        Generated by ${Strings.appTitle(language)} • Asia/Dhaka
                    </div>
                </body>
                </html>
            """.trimIndent())
        }

        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                val printAdapter = webView.createPrintDocumentAdapter("Calendar-$monthTitle")
                printManager?.print("Calendar-$monthTitle", printAdapter, PrintAttributes.Builder().build())
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }
}
