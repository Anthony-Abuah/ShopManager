package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.AddInventoryItemContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel


@Composable
fun AddInventoryItemScreen(
    inventoryItemViewModel: InventoryItemViewModel,
    navigateToTakePhotoScreen: () -> Unit,
    navigateToQuantityCategorizationScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Inventory Item") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddInventoryItemContent(
                inventoryItem = inventoryItemViewModel.addInventoryItemInfo,
                isSavingInventoryItem = inventoryItemViewModel.addInventoryItemState.value.isLoading,
                saveInventoryItemMessage = inventoryItemViewModel.addInventoryItemState.value.message,
                inventorySavingIsSuccessful = inventoryItemViewModel.addInventoryItemState.value.isSuccessful,
                addItemName = { inventoryItemViewModel.addItemName(it) },
                addItemManufacturerName = { inventoryItemViewModel.addManufacturerName(it) },
                addItemCategory = { inventoryItemViewModel.addItemCategory(it) },
                addItemSellingPrice = { inventoryItemViewModel.addSellingPrice(it) },
                addItemOtherInfo = { inventoryItemViewModel.addOtherInfo(it) },
                onTakePhoto = { navigateToTakePhotoScreen() },
                navigateToQuantityCategorizationScreen = { navigateToQuantityCategorizationScreen() },
                addInventoryItem = {_inventoryItem-> inventoryItemViewModel.addInventoryItem(_inventoryItem) }
            ) {
                navigateBack()
            }
        }
    }
}
