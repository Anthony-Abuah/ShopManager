package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString


@Composable
fun ToggleSwitchCard(
    modifier: Modifier = Modifier,
    checkValue: Boolean = false,
    isEnabled: Boolean = false,
    getCheckedValue: (Boolean)-> Unit = {}
) {

    Switch(
        modifier = modifier,
        checked = checkValue,
        onCheckedChange = {
            getCheckedValue(it)
        },
        thumbContent = {
            if (checkValue) {
                Icon(
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                    imageVector = Icons.Default.Check,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledCheckedThumbColor = MaterialTheme.colorScheme.primary,
            disabledCheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            disabledUncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            disabledUncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        enabled = isEnabled
    )
}





