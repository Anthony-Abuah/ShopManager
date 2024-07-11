package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.expense.screen

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.StockReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.expense.MainExpenseReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.*
import java.time.LocalDate

@Composable
fun MainExpenseReportScreen(
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    revenueViewModel: RevenueViewModel = hiltViewModel(),
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
        expenseViewModel.getExpenseAmount(period)
        expenseViewModel.getAverageDailyExpenses(period)
        expenseViewModel.getMaximumExpenseDay(period)
        expenseViewModel.getMinimumExpenseDay(period)
        expenseViewModel.getExpenseTypeAmounts(period)
        revenueViewModel.getRevenueAmount(period)
    }
    Scaffold(
        topBar = {
            StockReportScreenTopBar(
                topBarTitleText = "Expenses Summary",
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
            val expenseAmount = expenseViewModel.expenseAmount.value.itemValue.value.toTwoDecimalPlaces()
            val expensePercentage = expenseAmount.div(revenueAmount).times(100).toTwoDecimalPlaces()
            val allExpenses = expenseViewModel.expenseEntitiesState.value.expenseEntities ?: emptyList()
            val allFilteredExpenses = allExpenses.filter { expense->
                if (period.isAllTime) expense.date >= 0.0
                else expense.date in period.firstDate.toTimestamp() .. period.lastDate.toTimestamp()
            }
            val averageDailyExpense = expenseViewModel.averageDailyExpenses.value.itemValue.value.toTwoDecimalPlaces()
            val minimumExpenseDay = expenseViewModel.minimumExpenseDay.value.itemValue.itemName
            val minimumExpenseAmount = expenseViewModel.minimumExpenseDay.value.itemValue.value.toTwoDecimalPlaces()
            val maximumExpenseDay = expenseViewModel.maximumExpenseDay.value.itemValue.itemName
            val maximumExpenseAmount = expenseViewModel.maximumExpenseDay.value.itemValue.value.toTwoDecimalPlaces()
            val expenseTypeAmount = expenseViewModel.expenseTypeAmount.value.itemValues


            val currency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value

            MainExpenseReportContent(
                currency = if (currency.isNullOrBlank()) GHS else currency,
                allExpenses = allFilteredExpenses,
                expenseTypes = expenseTypeAmount,
                averageDailyExpense = "$averageDailyExpense",
                revenuePercentageAsExpense = "$expensePercentage",
                totalExpenseAmount = "$expenseAmount",
                maxExpenseDay = maximumExpenseDay,
                maximumExpenseAmount = "$maximumExpenseAmount",
                minimumExpenseAmount = "$minimumExpenseAmount",
                minimumExpenseDay =minimumExpenseDay
            )
        }
    }
}
