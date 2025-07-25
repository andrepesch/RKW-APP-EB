package com.example.rkwthringenapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Ein durchdachtes "Professional Dark" Farbschema
private val RkwProfessionalDarkColorScheme = darkColorScheme(
    primary = App_Accent_Orange,
    onPrimary = App_Text_White,
    background = App_Background_Dark,
    onBackground = App_Text_White,
    surface = App_Surface_Gray, // Haupt-Fläche für Inhalte
    onSurface = App_Text_White,
    surfaceContainerLowest = App_Background_Dark,
    outline = App_Text_Gray,
    error = App_Error_Red,
    secondaryContainer = App_Status_Entwurf_Bar, // Farbe für Entwurf-Header
    tertiaryContainer = App_Status_Gesendet_Bar  // Farbe für Gesendet-Header
)

// Freundliches helles Farbschema
private val RkwFriendlyLightColorScheme = lightColorScheme(
    primary = App_Accent_Orange,
    onPrimary = App_Text_White,
    background = App_Background_Light,
    onBackground = App_Text_Black,
    surface = App_Surface_Light,
    onSurface = App_Text_Black,
    surfaceContainerLowest = App_Background_Light,
    outline = App_Text_DarkGray,
    error = App_Error_Red,
    secondaryContainer = App_Status_Entwurf_Bar,
    tertiaryContainer = App_Status_Gesendet_Bar
)

@Composable
fun RKWThüringenAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) RkwProfessionalDarkColorScheme else RkwFriendlyLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}