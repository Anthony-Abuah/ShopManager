package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.BankAccountEntities
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.CustomerEntities
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account.BankAccountCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.CustomerCard

@Composable
fun BankAccountsReportContent(
    bankAccounts: BankAccountEntities
){
    val context = LocalContext.current
    val currency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value ?: GHS
    BasicScreenColumnWithoutBottomBar{
        HorizontalDivider()
        bankAccounts.forEachIndexed { index, bankAccount ->
            BankAccountCard(bankAccount = bankAccount,
                number = index.plus(1).toString(),
                currency = currency,
                onDelete = { /*TODO*/ }) {}
            HorizontalDivider()
        }
    }


}