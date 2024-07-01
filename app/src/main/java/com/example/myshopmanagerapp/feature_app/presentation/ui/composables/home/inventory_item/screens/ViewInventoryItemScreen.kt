package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.ViewInventoryItemContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel


@Composable
fun ViewInventoryItemScreen(
    inventoryItemViewModel: InventoryItemViewModel,
    uniqueInventoryItemId: String,
    navigateToViewSellingPricesScreen: () -> Unit,
    navigateToViewCostPricesScreen: () -> Unit,
    navigateToQuantityCategoriesScreen: () -> Unit,
    navigateToViewInventoryStockScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        inventoryItemViewModel.getInventoryItem(uniqueInventoryItemId)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Inventory Item") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewInventoryItemContent(
                inventoryItem = inventoryItemViewModel.inventoryItemInfo,
                inventoryItemUpdateIsSuccessful = inventoryItemViewModel.updateInventoryItemState.value.isSuccessful,
                isUpdatingInventoryItem = inventoryItemViewModel.updateInventoryItemState.value.isLoading,
                updateInventoryItemMessage = inventoryItemViewModel.updateInventoryItemState.value.message,
                getUpdatedItemName = {_name->
                    inventoryItemViewModel.updateItemName(_name)
                },
                getUpdatedItemManufacturer = {_name->
                    inventoryItemViewModel.updateManufacturerName(_name)
                },
                getUpdatedItemCategory = {_category->
                    inventoryItemViewModel.updateItemCategory(_category)
                },
                getUpdatedOtherInfo = {_otherInfo->
                    inventoryItemViewModel.updateOtherInfo(_otherInfo)
                },
                updateInventoryItem = {
                    inventoryItemViewModel.updateInventoryItem(it)
                },
                navigateToSellingPricesScreen = { navigateToViewSellingPricesScreen() },
                navigateToCostPricesScreen = { navigateToViewCostPricesScreen() },
                navigateToStocksScreen = { navigateToViewInventoryStockScreen() },
                navigateToQuantityCategoriesScreen = { navigateToQuantityCategoriesScreen() },
            ) {
                navigateBack()
            }
        }
    }
}
