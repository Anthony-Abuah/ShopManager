package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue.screens

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.StockReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue.MainRevenueReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtRepaymentViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.RevenueViewModel
import java.time.LocalDate

@Composable
fun MainRevenueReportScreen(
    revenueViewModel: RevenueViewModel = hiltViewModel(),
    debtViewModel: DebtViewModel = hiltViewModel(),
    debtRepaymentViewModel: DebtRepaymentViewModel = hiltViewModel(),
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val periods = listOfPeriods.map { it.titleText }
    var period by remember {
        mutableStateOf(
            PeriodDropDownItem(
            titleText = "All Time",
            isAllTime = true,
            firstDate = LocalDate.now().minusYears(10),
            lastDate = LocalDate.now())
        )
    }
    LaunchedEffect(period) {
        revenueViewModel.getAllRevenues()
        revenueViewModel.getMinRevenue(period)
        revenueViewModel.getMaxRevenue(period)
        revenueViewModel.getRevenueAmount(period)
        debtViewModel.getPeriodicDebtAmount(period)
        debtRepaymentViewModel.getPeriodicDebtRepaymentAmount(period)
        revenueViewModel.getRevenueDays(period)
        revenueViewModel.getRevenueHours(period)
        revenueViewModel.getAverageRevenueHours(period)
        inventoryViewModel.getInventoryCost(period)
    }
    Scaffold(
        topBar = {
            StockReportScreenTopBar(
                topBarTitleText = "Revenue Summary",
                periodDropDownItems = listOfPeriods,
                onClickItem = {_period->
                    period = _period
                    Toast.makeText(context, _period.titleText, Toast.LENGTH_LONG).show()
                }
            ) {
                navigateBack()
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val revenueAmount = revenueViewModel.revenueAmount.value.itemValue.value.toTwoDecimalPlaces()
            val totalDebtAmount = debtViewModel.debtAmount.value.itemValue.value
            val totalDebtRepaymentAmount = debtRepaymentViewModel.debtRepaymentAmount.value.itemValue.value
            val totalHours = revenueViewModel.revenueHours.value.itemValue.value
            val totalDays = revenueViewModel.revenueDays.value.itemValue.value
            val maxRevenueDay = revenueViewModel.maxRevenue.value.itemValue.itemName
            val minimumRevenueDay = revenueViewModel.minRevenue.value.itemValue.itemName
            val maximumRevenueAmount = revenueViewModel.maxRevenue.value.itemValue.value.toTwoDecimalPlaces()
            val minimumRevenueAmount = revenueViewModel.minRevenue.value.itemValue.value.toTwoDecimalPlaces()
            val averageDailyRevenue = revenueAmount.div(totalDays).toTwoDecimalPlaces()
            val averageHourlyRevenue = revenueViewModel.averageRevenueHours.value.itemValue.value.toTwoDecimalPlaces()
            val averageDailyHours = totalHours.div(totalDays).toTwoDecimalPlaces()
            val debtPercentage = totalDebtAmount.div(revenueAmount).times(100.0).toTwoDecimalPlaces()
            val debtBalance = totalDebtAmount.minus(totalDebtRepaymentAmount).toTwoDecimalPlaces()
            val inventoryCost = inventoryViewModel.inventoryCost.value.itemValue.value.toTwoDecimalPlaces()

            val allRevenues = revenueViewModel.revenueEntitiesState.value.revenueEntities ?: emptyList()
            val allFilteredRevenue = allRevenues.filter {revenue->
                if (period.isAllTime) revenue.date >= 0.0
                else revenue.date in period.firstDate.toTimestamp() .. period.lastDate.toTimestamp()
            }

            val currency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value

            MainRevenueReportContent(
                currency = if (currency.isNullOrBlank()) GHS else currency,
                allRevenues = allFilteredRevenue,
                inventoryCost = "$inventoryCost",
                debtPercentage = "$debtPercentage",
                averageDailyHours = if (averageDailyHours==0.0) NotAvailable else "$averageDailyHours",
                averageDailyRevenue = "$averageDailyRevenue",
                averageHourlyRevenue = "$averageHourlyRevenue",
                totalDays = "${totalDays.toInt()}",
                totalHours = if (totalHours==0.0) NotAvailable else "${totalHours.toInt()}",
                outstandingDebtAmount = "$debtBalance",
                totalRevenueAmount = "$revenueAmount",
                totalDebtAmount = "$totalDebtAmount",
                totalDebtRepaymentAmount = "$totalDebtRepaymentAmount",
                maxRevenueDay = maxRevenueDay,
                maximumRevenueAmount = maximumRevenueAmount.toString(),
                minimumRevenueAmount = minimumRevenueAmount.toString(),
                minimumRevenueDay = minimumRevenueDay,
                getSelectedPeriod = {selectedPeriod->
                    var periodIndex = periods.indexOf(selectedPeriod)
                    if (periodIndex >= listOfPeriods.size){ periodIndex = 0 }
                    period = listOfPeriods[periodIndex]
                }
            )
        }
    }
}
