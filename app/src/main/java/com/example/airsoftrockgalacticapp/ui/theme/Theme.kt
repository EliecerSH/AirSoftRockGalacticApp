package com.example.airsoftrockgalacticapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de colores para el modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4ADE80), // Verde táctico
    secondary = Color(0xFF64748B), // Gris azulado
    background = Color(0xFF1E293B), // Fondo oscuro (slate)
    surface = Color(0xFF334155), // Superficie de las tarjetas
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color(0xFFE2E8F0), // Texto claro
    onSurface = Color(0xFFE2E8F0) // Texto claro en tarjetas
)

// Paleta de colores para el modo claro
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4ADE80),
    secondary = Color(0xFFF5F5F5),
    background = Color(0xFFF8FAFC), // Fondo muy claro (casi blanco)
    surface = Color.White, // Superficie de las tarjetas
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF0F172A), // Texto oscuro
    onSurface = Color(0xFF0F172A) // Texto oscuro en tarjetas
)

@Composable
fun AirSoftRockGalacticAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivamos el color dinámico para tener un branding consistente
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

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
