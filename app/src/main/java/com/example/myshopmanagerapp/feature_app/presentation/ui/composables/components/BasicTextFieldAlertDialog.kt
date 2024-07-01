package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun BasicTextFieldAlertDialog(
    openDialog: Boolean,
    title: String,
    textContent: String,
    placeholder: String,
    label: String,
    icon: Int,
    keyboardType: KeyboardType,
    unconfirmedUpdatedToastText: String?,
    confirmedUpdatedToastText: String?,
    getValue: (String)-> Unit,
    closeDialog: () -> Unit,
) {
    var value by remember {
        mutableStateOf(emptyString)
    }
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
                    Toast.makeText(context, unconfirmedUpdatedToastText, Toast.LENGTH_LONG).show()
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
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
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
                        BasicTextFieldWithTrailingIconError(
                            value = value,
                            onValueChange = {
                                value = it
                            },
                            readOnly = false,
                            isError = false,
                            placeholder = placeholder,
                            label = label,
                            icon = icon,
                            keyboardType = keyboardType
                        )

                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (confirmedUpdatedToastText != null) {
                                Toast.makeText(
                                    context,
                                    confirmedUpdatedToastText,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            getValue(value)
                            closeDialog()
                        }
                    ){
                        Text(
                            text = "Save",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            if (unconfirmedUpdatedToastText != null) {
                                Toast.makeText(
                                    context,
                                    unconfirmedUpdatedToastText,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            closeDialog()
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
}