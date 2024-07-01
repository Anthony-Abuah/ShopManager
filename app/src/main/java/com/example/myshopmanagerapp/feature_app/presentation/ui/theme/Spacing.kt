package com.example.myshopmanagerapp.feature_app.presentation.ui.theme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val noElevation: Dp = 0.dp,
    val noPadding: Dp = 0.dp,
    val divider: Dp = 0.5.dp,
    val borderStroke: Dp = 1.dp,
    val textSpace: Dp = 1.dp,
    val extraSmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val default: Dp = 8.dp,
    val smallMedium: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val checkbox: Dp = 16.dp,
    val semiLarge: Dp = 24.dp,
    val miniDatePicker: Dp = 20.dp,
    val large: Dp = 32.dp,
    val dropDownItem: Dp = 36.dp,
    val topBarIcon: Dp = 40.dp,
    val floatingActionButton: Dp = 50.dp,
    val dateChipWidth: Dp = 32.dp,
    val iconHeight: Dp = 100.dp,
    val buttonHeight: Dp = 60.dp,
    val topAppBarSize: Dp = 50.dp,
    val viewOrUpdateRow: Dp = 50.dp,
    val textFieldHeight: Dp = 60.dp,
    val bottomNavBarSize: Dp = 60.dp,
    val extraLarge: Dp = 64.dp,
    val dialogBox: Dp = 150.dp
)


val LocalSpacing = compositionLocalOf { Spacing() }


val spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current