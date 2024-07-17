package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ReceiptItemsDisplayCardOnList
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*

@Composable
fun ReceiptCard(
    receiptEntity: ReceiptEntity,
    deleteReceipt: (String)-> Unit,
    navigateToUpdateReceiptScreen: (String)-> Unit,
    saveAsPDF: (ReceiptEntity)-> Unit,
){
    val context = LocalContext.current
    val thisCurrency = UserPreferences(context).getCurrency.collectAsState(initial = Constants.emptyString).value
    val currency = if (thisCurrency.isNullOrBlank()) FormRelatedString.GHS else thisCurrency

    val localDate = receiptEntity.date.toDate().toLocalDate()
    val dayOfWeek = localDate.dayOfWeek.toString().lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    val dateString = localDate.toDateString()
    val customerName = receiptEntity.customerName
    val customerContact = receiptEntity.customerContact

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.Transparent, MaterialTheme.shapes.small)
            .clickable { navigateToUpdateReceiptScreen(receiptEntity.uniqueReceiptId) },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
            contentAlignment = Alignment.CenterStart
        ) {
            ReceiptItemsDisplayCardOnList(
                currency = currency,
                receiptId = receiptEntity.uniqueReceiptId,
                dayOfWeek = dayOfWeek,
                dateString = dateString,
                customerContact = customerContact.toNotNull(),
                customerName = customerName,
                receiptItems = receiptEntity.items,
                delete = { deleteReceipt(receiptEntity.uniqueReceiptId) },
                saveAsPDF = { saveAsPDF(receiptEntity) }
            )
        }
    }
}

