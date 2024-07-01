package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.stock.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.Functions.roundDouble
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.StockReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.stock.MainStockReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel

@Composable
fun MainStockReportScreen(
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit){
        inventoryItemViewModel.getAllInventoryItems()
        inventoryItemViewModel.getShopItemCostValues()
        inventoryItemViewModel.getShopItemSalesValues()
        inventoryItemViewModel.getMaximumInventoryItem()
        inventoryItemViewModel.getMinimumInventoryItem()
        inventoryItemViewModel.getShopItemProfitValues()
        inventoryItemViewModel.getShopItemProfitPercentageValues()
    }
    Scaffold(
        topBar = {
            StockReportScreenTopBar(
                topBarTitleText = "Stock Report",
                periodDropDownItems = listOfPeriods,
                onClickItem = {
                    Toast.makeText(context, "${it.titleText} is selected", Toast.LENGTH_LONG).show()
                }
            ) {
                navigateBack()
            }
        },
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
            val inventoryCostValue = roundDouble(inventoryItemViewModel.itemCostValues.value.itemValues.sumOf { item-> item.value })
            val expectedSalesAmount = roundDouble(inventoryItemViewModel.itemSalesValues.value.itemValues.sumOf { item-> item.value })
            val maximumInventoryItemName = inventoryItemViewModel.maximumInventoryItem?.inventoryItemName ?: emptyString
            val minimumInventoryItemName = inventoryItemViewModel.minimumInventoryItem?.inventoryItemName ?: emptyString
            val numberOfMaximumInventoryItems = inventoryItemViewModel.maximumInventoryItem?.totalNumberOfUnits ?: 0
            val numberOfMinimumInventoryItems = inventoryItemViewModel.minimumInventoryItem?.totalNumberOfUnits ?: 0
            val profitAmount = roundDouble(inventoryItemViewModel.itemProfitValues.value.itemValues.sumOf { item-> item.value })
            val profitPercentage = roundDouble(profitAmount.div(inventoryCostValue).times(100.0))

            MainStockReportContent(
                totalNumberOfInventoryItems = "${allInventoryItems.size} items",
                inventoryValue = "GHS $inventoryCostValue",
                expectedSalesAmount = "GHS $expectedSalesAmount",
                mostAvailableInventoryItem = maximumInventoryItemName,
                leastAvailableInventoryItem = minimumInventoryItemName,
                numberOfLeastAvailableInventoryItem = numberOfMinimumInventoryItems.toString(),
                numberOfMostAvailableInventoryItem = numberOfMaximumInventoryItems.toString(),
                expectedProfitAmount = "GHS $profitAmount",
                expectedProfitPercentage = "$profitPercentage %",
            )
        }
    }
}

