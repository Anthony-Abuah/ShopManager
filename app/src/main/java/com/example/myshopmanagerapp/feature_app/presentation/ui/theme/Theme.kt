package com.example.myshopmanagerapp.feature_app.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Blue90,
    onPrimary = Blue30,
    primaryContainer = Blue20,
    onPrimaryContainer = Blue95,
    inversePrimary = Blue40,
    secondary = BlueGrey90,
    onSecondary = BlueGrey30,
    secondaryContainer = BlueGrey20,
    onSecondaryContainer = BlueGrey90,
    tertiary = DarkBlue90,
    onTertiary = DarkBlue30,
    tertiaryContainer = DarkBlue20,
    onTertiaryContainer = DarkBlue80,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Grey10,
    onBackground = Grey90,
    surface = BlueGrey10,
    onSurface = BlueGrey90,
    inverseSurface = Grey90,
    inverseOnSurface = Grey10,
    surfaceVariant = BlueGrey30,
    onSurfaceVariant = BlueGrey95,
    outline = BlueGrey80,
    outlineVariant = Blue50
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Grey99,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    inversePrimary = Blue80,
    secondary = BlueGrey40,
    onSecondary = Grey99,
    secondaryContainer = BlueGrey90,
    onSecondaryContainer = BlueGrey10,
    tertiary = DarkBlue40,
    onTertiary = Grey99,
    tertiaryContainer = DarkBlue90,
    onTertiaryContainer = DarkBlue10,
    error = Red40,
    onError = Red95,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = BlueGrey95,
    onSurface = BlueGrey30,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = BlueGrey80,
    onSurfaceVariant = BlueGrey10,
    outline = Grey40,
    outlineVariant = Blue50

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
fun MyShopManagerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightNavigationBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}