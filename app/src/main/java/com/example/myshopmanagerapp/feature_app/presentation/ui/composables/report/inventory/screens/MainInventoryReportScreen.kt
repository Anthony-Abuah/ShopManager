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
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.Functions.roundDouble
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toRoundedInt
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.InventoryReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.StockReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.MainInventoryReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun MainInventoryReportScreen(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    var period by remember {
        mutableStateOf(PeriodDropDownItem(
            titleText = "All Time",
            isAllTime = true,
            firstDate = LocalDate.now().minusYears(10),
            lastDate = LocalDate.now()
        ))
    }
    LaunchedEffect(period){
        inventoryViewModel.getTotalCostValue(period)
        inventoryViewModel.getInventoryItemQuantities(period)
        inventoryViewModel.getTotalExpectedSalesValue(period)
        inventoryViewModel.getTotalNumberOfItems(period)
        inventoryViewModel.getMostAvailableItem(period)
        inventoryViewModel.getLeastAvailableItem(period)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            InventoryReportScreenTopBar(
                topBarTitleText = "Inventory Report",
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
            val totalCostValue = inventoryViewModel.totalCostValue.value.itemValue.value
            val totalExpectedSalesValue = inventoryViewModel.totalExpectedSalesValue.value.itemValue.value
            val expectedProfitAmount = totalExpectedSalesValue.minus(totalCostValue)

            MainInventoryReportContent(
                itemValues = inventoryViewModel.inventoryItemQuantities.value.itemValues,
                totalNumberOfInventoryItems = "${inventoryViewModel.totalNumberOfItems.value.itemValue.value.toRoundedInt()} items",
                inventoryValue = "GHS ${roundDouble(totalCostValue)}",
                expectedSalesAmount = "GHS ${roundDouble(totalExpectedSalesValue)}",
                mostAvailableInventoryItem = inventoryViewModel.mostAvailableItem.value.itemValue.itemName,
                leastAvailableInventoryItem = inventoryViewModel.leastAvailableItem.value.itemValue.itemName,
                numberOfLeastAvailableInventoryItem = inventoryViewModel.leastAvailableItem.value.itemValue.value.toRoundedInt().toString(),
                numberOfMostAvailableInventoryItem = inventoryViewModel.mostAvailableItem.value.itemValue.value.toRoundedInt().toString(),
                expectedProfitAmount = "GHS ${roundDouble(expectedProfitAmount)}",
                expectedProfitPercentage = "${roundDouble(expectedProfitAmount.div(totalCostValue).times(100.0).toNotNull())} %",
            )
        }
    }
}

