package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonTopBar(
    placeholder: String,
    goBack: () -> Unit,
    getComparisonValue: (String) -> Unit
) {
    TopAppBar (
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ComparisonBar(
                    placeholder = placeholder,
                    onCompare = { getComparisonValue(it) }
                )
            }
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .clickable { goBack() },
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = emptyString,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

