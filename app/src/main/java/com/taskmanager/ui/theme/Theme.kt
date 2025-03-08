package com.taskmanager.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Gray80,
    secondary = BlueGray80,
    tertiary = Navy80
)

private val LightColorScheme = lightColorScheme(
    primary = Gray40,
    secondary = BlueGray40,
    tertiary = Navy40
)

val colors = listOf(
    Color(0xFFB39DDB),
    Color(0xFF80DEEA),
    Color(0xFFF48FB1),
    Color(0xFFFFAB91),
    Color(0xFFFFD54F),
    Color(0xFFA5D6A7),
)

@Composable
fun TaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    customPrimaryColor: Color? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }.let { scheme ->

        if (customPrimaryColor != null) {
            scheme.copy(
                primary = customPrimaryColor,
                primaryContainer = customPrimaryColor.copy(alpha = 0.3f),
                onPrimaryContainer = customPrimaryColor
            )
        } else {
            scheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}