package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.myshopmanagerapp.core.TypeConverters.toPeriodDropDownItemWithDateJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.GeneralReportContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Grey13
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Grey98
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
    navigateToViewInventoryItemsScreen: (String)-> Unit,
    navigateToViewOwingCustomersScreen: ()-> Unit,
    navigateToViewPersonnelScreen: ()-> Unit,
    navigateToViewBankAccountsScreen: ()-> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
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
        inventoryItemViewModel.getPeriodicInventoryItems(period)
        inventoryViewModel.getAllInventories()
        customerViewModel.getAllCustomers()
        personnelViewModel.getAllPersonnel()
        savingsViewModel.getAllSavings()
        bankAccountViewModel.getAllBankAccounts()
        revenueViewModel.getRevenueAmount(period)
        withdrawalViewModel.getAllWithdrawals()
        expenseViewModel.getExpenseAmount(period)
        stockViewModel.getShopValue(period)
    }
    val shopInfoJson = userPreferences.getShopInfo.collectAsState(initial = emptyString).value
    val shopInfo = shopInfoJson.toCompanyEntity()
    val currency =  userPreferences.getCurrency.collectAsState(initial = emptyString).value

    Scaffold(
        topBar = {
            BasicScreenTopBar(
                topBarTitleText = "Shop Overview",
                backgroundColor = if (isSystemInDarkTheme()) Grey13 else Grey98
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
            val allInventoryItems = inventoryItemViewModel.periodicInventoryItems.value.data
            val numberOfInventoryItems = allInventoryItems.count()
            val debtAmount = allCustomers.sumOf { it.debtAmount.toNotNull() }

            val shopValue = stockViewModel.shopValue.value.itemValue.value.toTwoDecimalPlaces()
            val totalRevenue = revenueViewModel.revenueAmount.value.itemValue.value.toTwoDecimalPlaces()
            val totalExpense = expenseViewModel.expenseAmount.value.itemValue.value.toTwoDecimalPlaces()
            val netIncome = totalRevenue.minus(totalExpense)

            GeneralReportContent(
                currency = if (currency.isNullOrBlank()) GHS else currency,
                netIncome = "$netIncome",
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
                getSelectedPeriod = {selectedPeriod->
                    var periodIndex = periods.indexOf(selectedPeriod)
                    if (periodIndex > listOfPeriods.size){ periodIndex = 0 }
                    period = listOfPeriods[periodIndex]
                },
                navigateToViewInventoryItemsScreen = {
                    val periodWithDate = period.toPeriodDropDownItemWithDate()
                    val periodWithDateJson = periodWithDate.toPeriodDropDownItemWithDateJson()
                    navigateToViewInventoryItemsScreen(periodWithDateJson)
                },
                navigateToViewOwingCustomersScreen = navigateToViewOwingCustomersScreen,
                navigateToViewPersonnelScreen = navigateToViewPersonnelScreen,
                navigateToViewBankAccountsScreen = navigateToViewBankAccountsScreen
            )
        }
    }
}
