package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Indigo & Slate Modern Palette
val IndigoPrimary = Color(0xFF3F51B5)
val IndigoDark = Color(0xFF303F9F)
val IndigoLight = Color(0xFFC5CAE9)

val AccentAmber = Color(0xFFFFB300)
val AccentCoral = Color(0xFFFF5252)
val AccentTeal = Color(0xFF00897B)
val AccentGreen = Color(0xFF43A047)
val AccentPurple = Color(0xFF8E24AA)
val AccentPink = Color(0xFFE91E63)

// Dark Theme Colors
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceVariant = Color(0xFF334155)
val DarkPrimary = Color(0xFF818CF8)
val DarkOnPrimary = Color(0xFF0F172A)
val DarkOnSurface = Color(0xFFF1F5F9)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkBorder = Color(0xFF334155)

// Light Theme Colors
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightPrimary = Color(0xFF3F51B5)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)
val LightBorder = Color(0xFFE2E8F0)

// Event Color Palette (7 preset vivid options for events)
val EventColorPresets = listOf(
    "#3F51B5", // Indigo
    "#E91E63", // Pink
    "#00897B", // Teal
    "#FB8C00", // Orange
    "#8E24AA", // Purple
    "#00ACC1", // Cyan
    "#E53935"  // Red
)

fun parseHexColor(hex: String, fallback: Color = IndigoPrimary): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(0xFF000000 or colorInt)
        } else if (cleanHex.length == 8) {
            Color(colorInt)
        } else {
            fallback
        }
    } catch (e: Exception) {
        fallback
    }
}
