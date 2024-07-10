package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

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
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.StockReportScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.GeneralReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.*
import java.time.LocalDate

@Composable
fun GeneralReportScreen(
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    stockViewModel: StockViewModel = hiltViewModel(),
    revenueViewModel: RevenueViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    withdrawalViewModel: WithdrawalViewModel = hiltViewModel(),
    savingsViewModel: SavingsViewModel = hiltViewModel(),
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
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
        inventoryItemViewModel.getAllInventoryItems()
        inventoryViewModel.getAllInventories()
        customerViewModel.getAllCustomers()
        personnelViewModel.getAllPersonnel()
        savingsViewModel.getAllSavings()
        bankAccountViewModel.getAllBanks()
        revenueViewModel.getShopRevenue(period)
        withdrawalViewModel.getAllWithdrawals()
        expenseViewModel.getShopExpense(period)
        stockViewModel.getShopValue(period)
    }
    val shopInfoJson = userPreferences.getShopInfo.collectAsState(initial = emptyString).value
    val shopInfo = shopInfoJson.toCompanyEntity()
    val currency =  userPreferences.getCurrency.collectAsState(initial = emptyString).value ?: GHS


    Scaffold(
        topBar = {
            StockReportScreenTopBar(
                topBarTitleText = "General Shop Report",
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
            val allSavings = savingsViewModel.savingsEntitiesState.value.savingsEntities ?: emptyList()
            val totalSavings = allSavings.sumOf { savings-> savings.savingsAmount }.toTwoDecimalPlaces()

            val allWithdrawals = withdrawalViewModel.withdrawalEntitiesState.value.withdrawalEntities ?: emptyList()
            val totalWithdrawals = allWithdrawals.sumOf { withdrawal-> withdrawal.withdrawalAmount }.toTwoDecimalPlaces()

            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
            val numberOfOwingCustomers = allCustomers.count {customer-> customer.debtAmount.toNotNull() > 0.0 }
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val numberOfPersonnel = allPersonnel.count()
            val allBankAccounts = bankAccountViewModel.bankAccountEntitiesState.value.bankAccountEntities ?: emptyList()
            val numberOfBankAccounts = allBankAccounts.count()
            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
            val numberOfInventoryItems = allInventoryItems.count()
            val debtAmount = allCustomers.sumOf { it.debtAmount.toNotNull() }

            val shopValue = stockViewModel.shopValue.value.itemValue.value.toTwoDecimalPlaces()
            val totalRevenue = revenueViewModel.shopRevenueAmount.value.itemValue.value.toTwoDecimalPlaces()
            val totalExpense = expenseViewModel.shopExpenseAmount.value.itemValue.value.toTwoDecimalPlaces()

            GeneralReportContent(
                currency = currency,
                numberOfInventoryItems = "$numberOfInventoryItems",
                totalSavings = "$totalSavings",
                numberOfOwingCustomers = "$numberOfOwingCustomers",
                totalRevenues = "$totalRevenue",
                totalExpenses = "$totalExpense",
                totalWithdrawals = "$totalWithdrawals",
                numberOfPersonnel = "$numberOfPersonnel",
                numberOfBankAccounts = "$numberOfBankAccounts",
                shopName = shopInfo?.companyName ?: "Not Registered",
                productsSold = shopInfo?.companyProductsAndServices ?: "Not Registered",
                totalOutstandingDebtAmount = "$debtAmount",
                shopValue = "$shopValue",
            )
        }
    }
}
