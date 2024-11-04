package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Grey5
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun BasicScreenColumnWithBottomBar(
    content: @Composable (ColumnScope.() -> Unit)
) {
    val backgroundColor = if (isSystemInDarkTheme()) Grey5 else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(backgroundColor)
            .padding(bottom = LocalSpacing.current.bottomNavBarSize)
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        content = content
    )
}

@Composable
fun BasicScreenColumnWithoutBottomBar(
    content: @Composable (ColumnScope.() -> Unit)
) {
    //val backgroundColor = if (isSystemInDarkTheme()) Grey5 else Color.White
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.background)
            .padding(LocalSpacing.current.noPadding)
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        content = content
    )
}
