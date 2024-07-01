package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock.ViewStockContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.StockViewModel
import java.util.*


@Composable
fun ViewStockScreen(
    stockViewModel: StockViewModel,
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    uniqueStockId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        stockViewModel.getStock(uniqueStockId)
        inventoryItemViewModel.getAllInventoryItems()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Stock") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
            ViewStockContent(
                stock = stockViewModel.stockInfo,
                stockUpdatingIsSuccessful = stockViewModel.updateStockState.value.isSuccessful,
                updateStockMessage = stockViewModel.updateStockState.value.message,
                getUpdatedStockDate = { _date->
                    val date = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString() }
                    stockViewModel.updateStockDate(date, dayOfWeek)
                },
                getInventoryItem = { _uniqueInventoryItemId ->
                    return@ViewStockContent allInventoryItems.firstOrNull {
                        it.uniqueInventoryItemId == _uniqueInventoryItemId }
                },
                getUpdatedOtherInfo = { _otherInfo->
                    stockViewModel.updateOtherInfo(_otherInfo)
                },
                getUpdatedStockQuantities = { _quantities->
                    stockViewModel.updateStockQuantities(_quantities)
                },
                updateStock = {_stock->
                    stockViewModel.updateStock(_stock)
                },
                isUpdatingStockInfo = stockViewModel.updateStockState.value.isLoading,
            ) {
                navigateBack()
            }
        }
    }
}
