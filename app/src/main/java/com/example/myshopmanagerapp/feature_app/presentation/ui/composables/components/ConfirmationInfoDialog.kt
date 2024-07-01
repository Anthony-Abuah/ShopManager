package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.DialogProperties
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ConfirmationInfoDialog(
    openDialog: Boolean,
    isLoading: Boolean,
    title: String?,
    textContent: String,
    unconfirmedDeletedToastText: String?,
    confirmedDeleteToastText: String?,
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
                    if (unconfirmedDeletedToastText != null) {
                        Toast.makeText(context, unconfirmedDeletedToastText, Toast.LENGTH_LONG).show()
                    }
                    closeDialog()
                },
                title = {
                    if (title != null) {
                        Box(
                            modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                },
                text = {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    else{
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
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (confirmedDeleteToastText != null) {
                                Toast.makeText(context, confirmedDeleteToastText, Toast.LENGTH_LONG).show()
                            }
                            closeDialog()
                        }
                    ){
                        Text(
                            text = "OK",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                dismissButton = {
                    /*
                    TextButton(
                        onClick = {
                            if (unconfirmedDeletedToastText != null){
                                Toast.makeText(context, unconfirmedDeletedToastText, Toast.LENGTH_LONG).show()
                            }
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
                    */
                }
            )
        }
}