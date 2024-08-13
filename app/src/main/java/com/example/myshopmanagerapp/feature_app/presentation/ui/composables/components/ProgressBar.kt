package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ProgressBar(
    openDialog: Boolean,
    title: String?,
    toastText: String?,
    getProgress: () -> Float,
    closeDialog: () -> Unit,
) {
    if (openDialog) {
        val context = LocalContext.current
        Dialog(
            /*
            shape = MaterialTheme.shapes.medium,
            contentColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            text = {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surface,
                    progress = {
                        getProgress()
                    })
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
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Start
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
            dismissButton = {}
            */
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true
            ),
            onDismissRequest = {
                if (toastText != null) {
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                }
                closeDialog()
            },
            content = {
                Column (modifier = Modifier
                    .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
                    .height(100.dp)
                    .padding(LocalSpacing.current.small),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ){
                    if (title != null) {
                        Box(
                            modifier = Modifier.padding(LocalSpacing.current.small),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

                    Row {
                        Box(modifier = Modifier.fillMaxWidth().padding(LocalSpacing.current.small),
                            contentAlignment = Alignment.CenterEnd
                        ){
                            val percentageValue = getProgress().times(100.0)
                            Text(text = percentageValue.toString().plus("%"),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onBackground,
                            trackColor = MaterialTheme.colorScheme.surface,
                            progress = {
                                getProgress()
                            }
                        )
                    }
                }
            },
        )
    }

}


