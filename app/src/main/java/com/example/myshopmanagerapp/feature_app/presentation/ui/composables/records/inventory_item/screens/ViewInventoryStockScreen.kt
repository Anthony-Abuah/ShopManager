package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.ViewInventoryStockContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel


@Composable
fun ViewInventoryStockScreen(
    inventoryItemViewModel: InventoryItemViewModel,
    navigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Item Stocks") {
                navigateBack()
            }
        },
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewInventoryStockContent(
                stockEntities = inventoryItemViewModel.inventoryItemInfo.stockInfo ?: emptyList(),
                inventoryItemName = inventoryItemViewModel.inventoryItemInfo.inventoryItemName
            )
        }
    }
}
