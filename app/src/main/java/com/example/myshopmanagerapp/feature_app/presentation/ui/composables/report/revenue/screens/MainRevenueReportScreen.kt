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
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.StockReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue.MainRevenueReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.ExpenseViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.RevenueViewModel
import java.time.LocalDate

@Composable
fun MainRevenueReportScreen(
    revenueViewModel: RevenueViewModel = hiltViewModel(),
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
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
        expenseViewModel.getAllExpenses()
        revenueViewModel.getAllRevenues()
        revenueViewModel.getMinRevenue(period)
        revenueViewModel.getMaxRevenue(period)
        revenueViewModel.getMaxExpense(period)
        revenueViewModel.getMinExpense(period)
        revenueViewModel.getRevenueAmount(period)
        revenueViewModel.getExpenseAmount(period)
        revenueViewModel.getDebtAmount(period)
        revenueViewModel.getDebtRepaymentAmount(period)
        revenueViewModel.getShopRevenue(period)
        revenueViewModel.getRevenueDays(period)
        revenueViewModel.getRevenueHours(period)
    }
    Scaffold(
        topBar = {
            StockReportScreenTopBar(
                topBarTitleText = "Revenue Report",
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
            val expenseAmount = revenueViewModel.expenseAmount.value.itemValue.value.toTwoDecimalPlaces()
            val totalDebtAmount = revenueViewModel.debtAmount.value.itemValue.value
            val totalDebtRepaymentAmount = revenueViewModel.debtRepaymentAmount.value.itemValue.value
            val totalHours = revenueViewModel.revenueHours.value.itemValue.value
            val totalDays = revenueViewModel.revenueDays.value.itemValue.value
            val maxRevenueDay = revenueViewModel.maxRevenue.value.itemValue.itemName
            val minimumRevenueDay = revenueViewModel.minRevenue.value.itemValue.itemName
            val maxExpenseDay = revenueViewModel.maxExpense.value.itemValue.itemName
            val minExpenseDay = revenueViewModel.minExpense.value.itemValue.itemName
            val maximumRevenueAmount = revenueViewModel.maxRevenue.value.itemValue.value.toTwoDecimalPlaces()
            val minimumRevenueAmount = revenueViewModel.minRevenue.value.itemValue.value.toTwoDecimalPlaces()
            val maxExpenseAmount = revenueViewModel.maxExpense.value.itemValue.value.toTwoDecimalPlaces()
            val minExpenseAmount = revenueViewModel.minExpense.value.itemValue.value.toTwoDecimalPlaces()
            val netIncomeAmount = revenueAmount.minus(expenseAmount).toTwoDecimalPlaces()
            val averageDailyRevenue = revenueAmount.div(totalDays).toTwoDecimalPlaces()
            val averageHourlyRevenue = revenueAmount.div(totalHours).toTwoDecimalPlaces()
            val averageDailyHours = totalHours.div(totalDays).toTwoDecimalPlaces()
            val debtPercentage = totalDebtAmount.div(revenueAmount).times(100.0).toTwoDecimalPlaces()
            val debtBalance = totalDebtAmount.minus(totalDebtRepaymentAmount).toTwoDecimalPlaces()
            val expensePercentage = expenseAmount.div(revenueAmount).times(100.0).toTwoDecimalPlaces()
            val inventoryCost = revenueViewModel.shopRevenueAmount.value.itemValue.value.toTwoDecimalPlaces()

            val allRevenues = revenueViewModel.revenueEntitiesState.value.revenueEntities ?: emptyList()
            val allFilteredRevenue = allRevenues.filter {revenue->
                if (period.isAllTime) revenue.date >= 0.0
                else revenue.date in period.firstDate.toTimestamp() .. period.lastDate.toTimestamp()
            }
            val allExpenses = expenseViewModel.expenseEntitiesState.value.expenseEntities ?: emptyList()
            val allFilteredExpense = allExpenses.filter {expense->
                if (period.isAllTime) expense.date >= 0.0
                else expense.date in period.firstDate.toTimestamp() .. period.lastDate.toTimestamp()
            }


            MainRevenueReportContent(
                currency = "GHS",
                allRevenues = allFilteredRevenue,
                allExpenses = allFilteredExpense,
                totalRevenueAmount = "$revenueAmount",
                expenseAmount = "$expenseAmount",
                totalDebtAmount = "$totalDebtAmount",
                totalDebtRepaymentAmount = "$totalDebtRepaymentAmount",
                maxRevenueDay = maxRevenueDay,
                maximumRevenueAmount = maximumRevenueAmount.toString(),
                netIncomeAmount = netIncomeAmount.toString(),
                minimumRevenueAmount = minimumRevenueAmount.toString(),
                minimumRevenueDay = minimumRevenueDay,
                minExpenseAmount = minExpenseAmount.toString(),
                minExpenseDay = minExpenseDay,
                maxExpenseDay = maxExpenseDay,
                maxExpenseAmount = maxExpenseAmount.toString(),
                averageDailyRevenue = "$averageDailyRevenue",
                averageHourlyRevenue = "$averageHourlyRevenue",
                averageDailyHours = "$averageDailyHours",
                debtBalance = "$debtBalance",
                debtPercentage = "$debtPercentage",
                expensePercentage = "$expensePercentage",
                inventoryCost = "$inventoryCost",
                totalDays = "${totalDays.toInt()}",
                totalHours = "${totalHours.toInt()}",
            )
        }
    }
}
