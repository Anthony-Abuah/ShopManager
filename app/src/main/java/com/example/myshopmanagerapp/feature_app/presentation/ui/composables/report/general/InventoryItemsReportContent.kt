package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.InventoryItemEntities
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.InventoryItemReportCard

@Composable
fun InventoryItemsReportContent(
    inventoryItems: InventoryItemEntities
){
    val context = LocalContext.current
    val currency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value ?: GHS
    BasicScreenColumnWithoutBottomBar{
        HorizontalDivider()
        inventoryItems.forEachIndexed { index, inventoryItem ->
            val sellingPrice = inventoryItem.currentSellingPrice?.toString() ?: NotAvailable
            val currentCostPrice = inventoryItem.currentCostPrice?.toString() ?: NotAvailable
            InventoryItemReportCard(
                inventoryItemName = inventoryItem.inventoryItemName,
                sellingPrice = sellingPrice,
                costPrice = currentCostPrice,
                number = index.plus(1).toString(),
                currency = currency
            )
            HorizontalDivider()
        }
    }


}