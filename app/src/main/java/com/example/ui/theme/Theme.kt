package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VetSathiPrimaryDark,
    secondary = VetSathiSecondaryDark,
    tertiary = VetSathiTertiaryDark,
    background = VetSathiBackgroundDark,
    surface = VetSathiSurfaceDark,
    onPrimary = VetSathiBackgroundDark,
    onSecondary = VetSathiBackgroundDark,
    onBackground = VetSathiSurfaceLight,
    onSurface = VetSathiSurfaceLight,
)

private val LightColorScheme = lightColorScheme(
    primary = VetSathiPrimary,
    secondary = VetSathiSecondary,
    tertiary = VetSathiTertiary,
    background = VetSathiBackgroundLight,
    surface = VetSathiSurfaceLight,
    onPrimary = VetSathiSurfaceLight,
    onSecondary = VetSathiSurfaceLight,
    onBackground = VetSathiBackgroundDark,
    onSurface = VetSathiBackgroundDark,
)

@Composable
fun VetSathiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color support deactivated by default for brand consistency
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
