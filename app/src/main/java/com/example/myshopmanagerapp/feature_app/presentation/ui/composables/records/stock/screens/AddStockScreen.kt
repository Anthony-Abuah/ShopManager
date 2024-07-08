package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.stock.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.stock.AddStockContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.StockViewModel
import java.util.*


@Composable
fun AddStockScreen(
    stockViewModel: StockViewModel,
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        inventoryItemViewModel.getAllInventoryItems()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Stock") {
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
            allInventoryItems.forEach { item->
                mapOfItems[item.inventoryItemName] = item.uniqueInventoryItemId
            }
            AddStockContent(
                stock = stockViewModel.addStockInfo,
                mapOfItems = mapOfItems,
                addStockDate = {_date->
                    val date = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    stockViewModel.addStockDate(date, dayOfWeek)
                },
                addStockQuantities = {_quantity->
                    stockViewModel.addRemainingStock(_quantity)
                },
                addStockItemId = {_itemId->
                    stockViewModel.addUniqueItemId(_itemId)
                },
                addStockOtherInfo = {_otherInfo->
                    stockViewModel.addOtherInfo(_otherInfo)
                },
                addStock = {_addStockInfo->
                    stockViewModel.addStock(_addStockInfo)
                },
                getInventoryItem = {uniqueInventoryItemId->
                    return@AddStockContent allInventoryItems.firstOrNull<InventoryItemEntity> { it.uniqueInventoryItemId == uniqueInventoryItemId }
                },
                isSavingStockInfo = stockViewModel.addStockState.value.isLoading,
                stockSavingIsSuccessful = stockViewModel.addStockState.value.isSuccessful,
                stockSavingMessage = stockViewModel.addStockState.value.message,
            ) {
                navigateBack()
            }
        }
    }
}
