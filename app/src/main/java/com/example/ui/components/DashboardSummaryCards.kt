package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CalendarEvent
import com.example.util.AppLanguage
import com.example.util.DateUtils
import com.example.util.LanguageManager
import com.example.util.Strings
import java.time.LocalDate

@Composable
fun DashboardSummaryCards(
    events: List<CalendarEvent>,
    today: LocalDate,
    language: AppLanguage,
    onTodayCardClicked: () -> Unit,
    onUpcomingCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todayIso = today.toString()
    val totalCount = events.size
    val todayCount = events.count { it.date == todayIso }
    val upcomingCount = events.count {
        val d = DateUtils.parseDate(it.date)
        d != null && (d.isEqual(today) || d.isAfter(today))
    }
    val importantCount = events.count { it.isImportant }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryCard(
                title = Strings.totalEvents(language),
                count = LanguageManager.formatNumber(totalCount, language),
                icon = Icons.Default.CalendarMonth,
                accentColor = Color(0xFF3F51B5),
                containerColor = Color(0xFF3F51B5).copy(alpha = 0.12f),
                modifier = Modifier
                    .weight(1f)
                    .testTag("summary_total_events")
            )

            SummaryCard(
                title = Strings.todayEvents(language),
                count = LanguageManager.formatNumber(todayCount, language),
                icon = Icons.Default.Today,
                accentColor = Color(0xFF00897B),
                containerColor = Color(0xFF00897B).copy(alpha = 0.12f),
                onClick = onTodayCardClicked,
                modifier = Modifier
                    .weight(1f)
                    .testTag("summary_today_events")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryCard(
                title = Strings.upcomingEvents(language),
                count = LanguageManager.formatNumber(upcomingCount, language),
                icon = Icons.Default.Schedule,
                accentColor = Color(0xFFFB8C00),
                containerColor = Color(0xFFFB8C00).copy(alpha = 0.12f),
                onClick = onUpcomingCardClicked,
                modifier = Modifier
                    .weight(1f)
                    .testTag("summary_upcoming_events")
            )

            SummaryCard(
                title = Strings.importantEvents(language),
                count = LanguageManager.formatNumber(importantCount, language),
                icon = Icons.Default.Star,
                accentColor = Color(0xFFE91E63),
                containerColor = Color(0xFFE91E63).copy(alpha = 0.12f),
                modifier = Modifier
                    .weight(1f)
                    .testTag("summary_important_events")
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    count: String,
    icon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = count,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
