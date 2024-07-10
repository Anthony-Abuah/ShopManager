package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.ItemsSold
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfBankAccounts
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfInventoryItems
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfOwingCustomers
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfPersonnel
import com.example.myshopmanagerapp.core.FormRelatedString.ShopName
import com.example.myshopmanagerapp.core.FormRelatedString.ShopValue
import com.example.myshopmanagerapp.core.FormRelatedString.ShopValueInfo
import com.example.myshopmanagerapp.core.FormRelatedString.TotalExpenses
import com.example.myshopmanagerapp.core.FormRelatedString.TotalOutstandingDebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalRevenues
import com.example.myshopmanagerapp.core.FormRelatedString.TotalSavingsAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalWithdrawals
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewTextValueRow
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun GeneralReportContent(
    currency: String,
    numberOfInventoryItems: String,
    totalSavings: String,
    numberOfOwingCustomers: String,
    totalRevenues: String,
    totalExpenses: String,
    totalWithdrawals: String,
    numberOfPersonnel: String,
    numberOfBankAccounts: String,
    shopName: String,
    productsSold: String,
    totalOutstandingDebtAmount: String,
    shopValue: String,
    navigateToViewInventoryItemsScreen: ()-> Unit,
    navigateToViewOwingCustomersScreen: ()-> Unit,
    navigateToViewPersonnelScreen: ()-> Unit,
    navigateToViewBankAccountsScreen: ()-> Unit,
){
    var openShowValueInfo by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar{
        HorizontalDivider()
        // Shop name
        ViewTextValueRow(viewTitle = ShopName, viewValue = shopName)

        HorizontalDivider()

        // Items sold
        ViewTextValueRow(viewTitle = ItemsSold, viewValue = productsSold)

        HorizontalDivider()

        // Number of inventory items
        ViewTextValueRow(
            viewTitle = NumberOfInventoryItems,
            viewValue = "$numberOfInventoryItems item(s)",
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            showInfo = true,
            onClick = navigateToViewInventoryItemsScreen
        )

        HorizontalDivider()

        // Number Of Personnel
        ViewTextValueRow(
            viewTitle = NumberOfPersonnel,
            viewValue = "$numberOfPersonnel personnel",
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            showInfo = true,
            onClick = navigateToViewPersonnelScreen
        )

        HorizontalDivider()

        // Total Revenue
        ViewTextValueRow(
            viewTitle = TotalRevenues,
            viewValue = "$currency $totalRevenues"
        )
        HorizontalDivider()

        // Total Expense
        ViewTextValueRow(
            viewTitle = TotalExpenses,
            viewValue = "$currency $totalExpenses"
        )

        HorizontalDivider()

        // Number of owing customers
        ViewTextValueRow(
            viewTitle = NumberOfOwingCustomers,
            viewValue = "$numberOfOwingCustomers customer(s)",
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            showInfo = true,
            onClick = navigateToViewOwingCustomersScreen
        )

        HorizontalDivider()

        // Number of bank accounts
        ViewTextValueRow(
            viewTitle = NumberOfBankAccounts,
            viewValue = "$numberOfBankAccounts bank account(s)",
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            showInfo = true,
            onClick = navigateToViewBankAccountsScreen
        )
        HorizontalDivider()

        // Total Savings Amount
        ViewTextValueRow(
            viewTitle = TotalSavingsAmount,
            viewValue = "$currency $totalSavings"
        )

        HorizontalDivider()

        // Total Withdrawal
        ViewTextValueRow(
            viewTitle = TotalWithdrawals,
            viewValue = "$currency $totalWithdrawals"
        )
        HorizontalDivider()

        // Debt Amount
        ViewTextValueRow(
            viewTitle = TotalOutstandingDebtAmount,
            viewValue = "$currency $totalOutstandingDebtAmount"
        )

        HorizontalDivider()

        //Shop value
        ViewTextValueRow(
            viewTitle = ShopValue,
            viewValue = "$currency $shopValue",
            showInfo = true,
            onClick = {
                openShowValueInfo = !openShowValueInfo
            }
        )
        HorizontalDivider()

        Spacer(modifier = Modifier.height(LocalSpacing.current.small))

    }
    ConfirmationInfoDialog(
        openDialog = openShowValueInfo,
        isLoading = false,
        title = emptyString,
        textContent = ShopValueInfo,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        openShowValueInfo = false
    }

}