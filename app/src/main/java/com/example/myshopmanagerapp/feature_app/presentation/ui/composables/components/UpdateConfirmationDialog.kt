package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.DialogProperties
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun UpdateConfirmationDialog(
    openDialog: Boolean,
    title: String,
    textContent: String,
    unconfirmedUpdatedToastText: String?,
    confirmedUpdatedToastText: String?,
    confirmUpdate: ()-> Unit,
    closeDialog: () -> Unit,
) {
        if (openDialog) {
            val context = LocalContext.current
            AlertDialog(
                shape = MaterialTheme.shapes.medium,
                contentColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = MaterialTheme.colorScheme.surface,
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                usePlatformDefaultWidth = true
                ),
                modifier = Modifier
                    .padding(LocalSpacing.current.noPadding)
                    .fillMaxWidth(),
                onDismissRequest = {
                    if (unconfirmedUpdatedToastText != null){ Toast.makeText(context, unconfirmedUpdatedToastText, Toast.LENGTH_LONG).show() }
                    closeDialog()
                },
                title = {
                    Box(modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                        contentAlignment = Alignment.Center)
                    {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                text = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = textContent,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (confirmedUpdatedToastText != null){ Toast.makeText(context, confirmedUpdatedToastText, Toast.LENGTH_LONG).show() }
                            confirmUpdate()
                            closeDialog()
                        }
                    ){
                        Text(
                            text = "YES",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            Toast.makeText(context, unconfirmedUpdatedToastText, Toast.LENGTH_LONG).show()
                            closeDialog()
                        }
                    ) {
                        Text(
                            text = "NO",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
}