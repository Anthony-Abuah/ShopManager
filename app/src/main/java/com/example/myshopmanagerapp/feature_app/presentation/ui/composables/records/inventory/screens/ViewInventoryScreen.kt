package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.ViewInventoryContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel
import java.util.*


@Composable
fun ViewInventoryScreen(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    inventoryItemViewModel: InventoryItemViewModel =  hiltViewModel(),
    uniqueInventoryId:  String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        inventoryViewModel.getInventory(uniqueInventoryId)
        inventoryItemViewModel.getAllInventoryItems()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Inventory") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
            val mapOfItems = mutableMapOf<String, String>()
            allInventoryItems.forEach { item ->
                mapOfItems[item.uniqueInventoryItemId] = item.inventoryItemName
            }

            ViewInventoryContent(
                inventory = inventoryViewModel.inventoryInfo,
                updateInventoryMessage = inventoryViewModel.updateInventoryState.value.message,
                inventoryUpdatingIsSuccessful = inventoryViewModel.updateInventoryState.value.isSuccessful,
                isUpdatingInventory = inventoryViewModel.updateInventoryState.value.isLoading,
                currency = "GHS",
                getUpdatedInventoryDate = {_dateString->
                    val date = _dateString.toLocalDate().toDate().time
                    val dayOfWeek = _dateString.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    inventoryViewModel.updateInventoryDate(date, dayOfWeek)
                },
                getUpdatedInventoryItemQuantity = {_quantity->
                    inventoryViewModel.updateInventoryQuantity(_quantity)
                },
                getUpdatedReceiptId = { _receiptId->
                    inventoryViewModel.updateInventoryReceiptId(_receiptId)
                },
                getInventoryItem = {_uniqueInventoryItemId ->
                    return@ViewInventoryContent allInventoryItems.firstOrNull {
                        it.uniqueInventoryItemId == _uniqueInventoryItemId } ?: InventoryItemEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyList(), null, emptyList(), null, null, null, null, null, emptyString)
                },
                getUpdatedCostPrices = { _unitCostPrice, totalCostPrice->
                    inventoryViewModel.updateInventoryCostPrices(convertToDouble( _unitCostPrice), convertToDouble( totalCostPrice))
                },
                getUpdatedOtherInfo = { _otherInfo->
                    inventoryViewModel.updateInventoryOtherInfo(_otherInfo)
                },
                updateInventory = {_inventory->
                    inventoryViewModel.updateInventory(_inventory)
                }
            ) {
                navigateBack()
            }
        }
    }
}
