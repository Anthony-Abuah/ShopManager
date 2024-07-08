package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.ReceiptEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.UpdateConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ReceiptListContent(
    allReceipts: ReceiptEntities,
    isLoading: Boolean,
    savePDFConfirmationMessage: String?,
    navigateToUpdateReceipt: (String) -> Unit,
    saveAsPDF: (ReceiptEntity) -> Unit,
) {
    var receiptEntity by remember {
        mutableStateOf<ReceiptEntity?>(null)
    }
    var savePDFConfirmation by remember {
        mutableStateOf(false)
    }
    var savePDFConfirmationInfo by remember {
        mutableStateOf(false)
    }

    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        if (allReceipts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No receipts have been created yet!",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            BasicScreenColumnWithoutBottomBar {
                allReceipts.fastForEachIndexed { index, receipt ->
                    if (index == 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(LocalSpacing.current.small),
                            thickness = 0.25.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Box(
                        modifier = Modifier.padding(LocalSpacing.current.default),
                        contentAlignment = Alignment.Center
                    ) {
                        ReceiptCard(receipt, navigateToUpdateReceipt ) {_receiptEntity ->
                            receiptEntity = _receiptEntity
                            savePDFConfirmation = !savePDFConfirmation
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(LocalSpacing.current.small),
                        thickness = 0.25.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }

    UpdateConfirmationDialog(
        openDialog = savePDFConfirmation,
        title = emptyString,
        textContent = "Are you sure you want to save this receipt as PDF",
        unconfirmedUpdatedToastText = null,
        confirmedUpdatedToastText = null,
        confirmUpdate = { saveAsPDF(receiptEntity!!)
            savePDFConfirmationInfo = !savePDFConfirmationInfo
        }) {
        savePDFConfirmation = false
    }

    ConfirmationInfoDialog(
        openDialog = savePDFConfirmationInfo,
        isLoading = false,
        title = null,
        textContent = savePDFConfirmationMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        savePDFConfirmationInfo = false
    }
}
