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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.ONE
import com.example.myshopmanagerapp.core.Constants.Quantity_Categorization
import com.example.myshopmanagerapp.core.Constants.Unit
import com.example.myshopmanagerapp.core.Constants.ZERO
import com.example.myshopmanagerapp.core.FormRelatedString.EnterQuantityOfUnits
import com.example.myshopmanagerapp.core.FormRelatedString.EnterSizeName
import com.example.myshopmanagerapp.core.FormRelatedString.QuantityPerUnitPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SizeNamePlaceholder
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.feature_app.domain.model.QuantityCategorization
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import com.vanpra.composematerialdialogs.MaterialDialog

@Composable
fun QuantityCategorizationAlertDialog(
    openDialog: Boolean,
    quantityCategorization: QuantityCategorization = QuantityCategorization(Unit, ONE),
    unconfirmedUpdatedToastText: String?,
    confirmedUpdatedToastText: String?,
    getValue: (String, Int)-> Unit,
    openSizeNameInfoDialog: () -> Unit,
    openSizeQuantityInfoDialog: () -> Unit,
    closeDialog: () -> Unit
) {
    var sizeName by remember {
        mutableStateOf(quantityCategorization.sizeName)
    }
    var quantityPerUnit by remember {
        mutableStateOf(quantityCategorization.unitsPerThisSize.toString())
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
                            text = Quantity_Categorization,
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
                        BasicTextFieldWithTrailingIconError(
                            value = sizeName,
                            onValueChange = { sizeName = it },
                            onClickTrailingIcon = openSizeNameInfoDialog,
                            readOnly = false,
                            isError = false,
                            placeholder = SizeNamePlaceholder,
                            label = EnterSizeName,
                            icon = R.drawable.ic_category,
                            keyboardType = KeyboardType.Text
                        )

                        BasicTextFieldWithTrailingIconError(
                            value = quantityPerUnit,
                            onValueChange = { quantityPerUnit = it },
                            onClickTrailingIcon =  openSizeQuantityInfoDialog,
                            readOnly = false,
                            isError = false,
                            placeholder = QuantityPerUnitPlaceholder,
                            label = EnterQuantityOfUnits,
                            icon = R.drawable.ic_quantity,
                            keyboardType = KeyboardType.Number
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (confirmedUpdatedToastText != null) {
                                Toast.makeText(context, confirmedUpdatedToastText, Toast.LENGTH_LONG).show()
                            }
                            val unitQuantity = convertToDouble(quantityPerUnit).toInt()
                            val thisUnitQuantity = if (unitQuantity == ZERO) ONE else unitQuantity
                            val thisSizeName = if (unitQuantity == ZERO) Unit else sizeName
                            closeDialog()
                            getValue(thisSizeName, thisUnitQuantity)
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

