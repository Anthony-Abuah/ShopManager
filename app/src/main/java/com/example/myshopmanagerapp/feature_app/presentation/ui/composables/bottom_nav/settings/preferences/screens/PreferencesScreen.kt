package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.preferences.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.preferences.PreferencesContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SettingsScreenTopBar


@Composable
fun PreferencesScreen(
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = { SettingsScreenTopBar(topBarTitleText = "Preferences", navigateBack = navigateBack) },
        //bottomBar = { BottomBar(navHostController) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PreferencesContent()
        }
    }

}

