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
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.AddInventoryContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel
import java.util.*


@Composable
fun AddInventoryScreen(
    inventoryViewModel: InventoryViewModel,
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    navigateToAddInventoryItemScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    LaunchedEffect(Unit){
        inventoryItemViewModel.getAllInventoryItems()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Inventory") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
            val mapOfInventoryItem = mutableMapOf<String, String>()
            allInventoryItems.forEach {item->
                mapOfInventoryItem[item.inventoryItemName] = item.uniqueInventoryItemId
            }
            AddInventoryContent(
                inventory = inventoryViewModel.addInventoryInfo,
                deleteMessage = inventoryViewModel.deleteInventoryState.value.message,
                saveInventoryMessage = inventoryViewModel.addInventoryState.value.message,
                isSavingInventory = inventoryViewModel.addInventoryState.value.isLoading,
                isDeletingInventory = inventoryViewModel.deleteInventoryState.value.isLoading,
                inventorySavingIsSuccessful = inventoryViewModel.addInventoryState.value.isSuccessful,
                inventoryDeletionIsSuccessful = inventoryViewModel.deleteInventoryState.value.isSuccessful,
                inventoryDisplayValues = inventoryViewModel.inventoryQuantityDisplayValues,
                mapOfInventoryItems = mapOfInventoryItem,
                getInventoryItem = {_uniqueInventoryItemId ->
                    return@AddInventoryContent allInventoryItems.firstOrNull { it.uniqueInventoryItemId == _uniqueInventoryItemId }
                },
                addInventoryDate = {_date->
                    val date = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    inventoryViewModel.addDate(date, dayOfWeek)
                },
                addOtherInfo = {_otherInfo->
                    inventoryViewModel.addOtherInfo(_otherInfo)
                },
                addReceiptId = {_receiptId->
                    inventoryViewModel.addReceiptId(_receiptId)
                },
                addInventoryItemId = { _uniqueItemId->
                    inventoryViewModel.addUniqueInventoryItemId(_uniqueItemId)
                },
                addCostPrices = { _unitCostPrice, _totalCostPrice->
                    val unitCostPrice = convertToDouble(_unitCostPrice)
                    val totalCostPrice = convertToDouble(_totalCostPrice)
                    inventoryViewModel.addCostPrices(unitCostPrice, totalCostPrice)
                },
                addInventoryDisplayCardValue = {_inventoryDisplayCardValues ->
                    inventoryViewModel.addInventoryQuantityValues(_inventoryDisplayCardValues)
                },
                addInventoryQuantities = { _itemQuantities->
                    inventoryViewModel.addItemQuantities(_itemQuantities)
                    val totalCostPrice = inventoryViewModel.addInventoryInfo.unitCostPrice.times(
                        _itemQuantities.getTotalNumberOfUnits()
                    )
                    inventoryViewModel.addCostPrices(
                        inventoryViewModel.addInventoryInfo.unitCostPrice,
                        totalCostPrice
                    )
                },
                addNewInventoryItem = {
                    navigateToAddInventoryItemScreen()
                },
                deleteInventory = {_uniqueInventoryId->
                    inventoryViewModel.deleteInventory(_uniqueInventoryId)
                },
                clearInventory = {
                    inventoryViewModel.clearInventory()
                },
                addInventoryEntity = { _inventoryEntity ->
                    inventoryViewModel.addInventoryWithStock(_inventoryEntity)
                },
                navigateBack = navigateBack,
            )
        }
    }
}
