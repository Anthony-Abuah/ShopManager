package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.ViewInventoryPricesContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel


@Composable
fun ViewInventorySellingPricesScreen(
    inventoryItemViewModel: InventoryItemViewModel,
    navigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Selling Prices") {
                navigateBack()
            }
        },
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewInventoryPricesContent(
                prices = inventoryItemViewModel.inventoryItemInfo.sellingPrices ?: emptyList(),
                inventoryItemName = inventoryItemViewModel.inventoryItemInfo.inventoryItemName
            )
        }
    }
}
