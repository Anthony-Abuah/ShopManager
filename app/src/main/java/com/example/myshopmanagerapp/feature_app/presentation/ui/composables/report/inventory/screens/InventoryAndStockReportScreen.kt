package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.UnknownItem
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.core.TypeConverters.toPeriodDropDownItemWithDateJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.InventoryReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.InventoryAndStockReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.StockViewModel
import java.time.LocalDate

@Composable
fun InventoryAndStockReportScreen(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    stockViewModel: StockViewModel = hiltViewModel(),
    navigateToViewInventoryItems: (String)-> Unit,
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val periods = listOfPeriods.map { it.titleText }
    var period by remember {
        mutableStateOf(PeriodDropDownItem(
            titleText = "All Time",
            isAllTime = true,
            firstDate = LocalDate.now().minusYears(10),
            lastDate = LocalDate.now()
        ))
    }
    LaunchedEffect(period){
        inventoryItemViewModel.getPeriodicInventoryItems(period)
        stockViewModel.getShopValue(period)
        stockViewModel.getExpectedSalesAmount(period)
        inventoryViewModel.getInventoryItemQuantities(period)

    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            InventoryReportScreenTopBar(
                topBarTitleText = "Inventory And Stock Summary",
                periodDropDownItems = listOfPeriods,
                onClickItem = {_period->
                    period = _period
                    Toast.makeText(context, _period.titleText, Toast.LENGTH_LONG).show()
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
            val totalInventoryItemsValue = stockViewModel.shopValue.value.itemValue.value
            val expectedSalesAmount = stockViewModel.expectedSalesAmount.value.itemValue.value
            val expectedProfit = expectedSalesAmount.minus(totalInventoryItemsValue)
            val expectedProfitPercentage = expectedProfit.div(expectedSalesAmount).times(100).toTwoDecimalPlaces()
            val mapOfInventoryItems = inventoryItemViewModel.periodicInventoryItems.value.data
            val mostAvailableItem = mapOfInventoryItems.maxByOrNull { it.value }?.key?.inventoryItemName ?: emptyString
            val numberOfMostAvailableInventoryItem = mapOfInventoryItems.maxByOrNull { it.value }?.value
            val leastAvailableItem = mapOfInventoryItems.minByOrNull { it.value }?.key?.inventoryItemName ?: emptyString
            val numberOfLeastAvailableInventoryItem = mapOfInventoryItems.minByOrNull { it.value }?.value
            val mostExpensiveItem = mapOfInventoryItems.maxByOrNull { it.key?.currentSellingPrice.toNotNull() }?.key?.inventoryItemName ?: emptyString
            val numberOfMostExpensiveInventoryItem = mapOfInventoryItems.maxByOrNull { it.key?.currentSellingPrice.toNotNull() }?.key?.currentSellingPrice ?: NotAvailable
            val leastExpensiveItem = mapOfInventoryItems.minByOrNull { it.key?.currentSellingPrice.toNotNull() }?.key?.inventoryItemName ?: emptyString
            val numberOfLeastExpensiveInventoryItem = mapOfInventoryItems.minByOrNull { it.key?.currentSellingPrice.toNotNull() }?.key?.currentSellingPrice ?: NotAvailable
            val inventoryItems = mapOfInventoryItems.map { ItemValue(it.key?.inventoryItemName ?: UnknownItem, it.value.toDouble()) }
            val currency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value
            InventoryAndStockReportContent(
                currency = if (currency.isNullOrBlank()) GHS else currency,
                inventoryItems = inventoryItems,
                numberOfInventoryItems = mapOfInventoryItems.count().toString(),
                totalInventoryItemsValue = totalInventoryItemsValue.toString(),
                expectedSalesAmount = expectedSalesAmount.toString(),
                expectedProfitAmount = expectedProfit.toString(),
                expectedProfitPercentage = expectedProfitPercentage.toString(),
                mostAvailableInventoryItem = mostAvailableItem,
                leastAvailableInventoryItem = leastAvailableItem,
                numberOfMostAvailableInventoryItem = numberOfMostAvailableInventoryItem?.toString() ?: NotAvailable,
                numberOfLeastAvailableInventoryItem = numberOfLeastAvailableInventoryItem?.toString() ?: NotAvailable,
                mostExpensiveInventoryItem = mostExpensiveItem,
                leastExpensiveInventoryItem = leastExpensiveItem,
                priceOfMostExpensiveInventoryItem = numberOfMostExpensiveInventoryItem.toString(),
                priceOfLeastExpensiveInventoryItem = numberOfLeastExpensiveInventoryItem.toString(),
                getSelectedPeriod = {selectedPeriod->
                    var periodIndex = periods.indexOf(selectedPeriod)
                    if (periodIndex >= listOfPeriods.size){ periodIndex = 0 }
                    period = listOfPeriods[periodIndex]
                }
            ) {
                val periodWithDate = period.toPeriodDropDownItemWithDate()
                val periodWithDateJson = periodWithDate.toPeriodDropDownItemWithDateJson()
                navigateToViewInventoryItems(periodWithDateJson)
            }
        }
    }
}

