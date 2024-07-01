package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.ONE
import com.example.myshopmanagerapp.core.Constants.Unit
import com.example.myshopmanagerapp.core.FormRelatedString.SizeNameInfo
import com.example.myshopmanagerapp.core.FormRelatedString.SizeQuantityInfo
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.domain.model.QuantityCategorization
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.QuantityCategorizationAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.QuantityCategorizationContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.ViewInventoryPricesContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.ViewInventoryStockContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import java.util.*


@Composable
fun ViewInventoryCostPricesScreen(
    inventoryItemViewModel: InventoryItemViewModel,
    navigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Cost Prices") {
                navigateBack()
            }
        },
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewInventoryPricesContent(
                prices = inventoryItemViewModel.inventoryItemInfo.costPrices ?: emptyList(),
                inventoryItemName = inventoryItemViewModel.inventoryItemInfo.inventoryItemName
            )
        }
    }
}
