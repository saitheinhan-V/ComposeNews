package com.my.composenews.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Color.White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ComposeNewsTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    appThemeStatus: AppThemeStatus = AppThemeStatus.Light,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isSupportedDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
    val colorScheme = when (appThemeStatus) {
        AppThemeStatus.Dark -> DarkColorScheme
        AppThemeStatus.Light -> LightColorScheme
        AppThemeStatus.DynamicDark -> if (isSupportedDynamicColor) dynamicDarkColorScheme(context) else DarkColorScheme
        AppThemeStatus.DynamicLight -> if (isSupportedDynamicColor) dynamicLightColorScheme(context) else LightColorScheme
    }
    val isDarkIcon = when (appThemeStatus) {
        AppThemeStatus.Dark -> false
        AppThemeStatus.DynamicDark -> false
        AppThemeStatus.DynamicLight -> true
        AppThemeStatus.Light -> true
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window,true)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkIcon
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                isDarkIcon
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}