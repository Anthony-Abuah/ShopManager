package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account.ViewBankAccountContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel


@Composable
fun ViewBankAccountScreen(
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    uniqueBankId: String,
    navigateBack: ()-> Unit
) {
    LaunchedEffect(Unit){
        bankAccountViewModel.getBankAccount(uniqueBankId)
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Bank") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val bankAccount = bankAccountViewModel.bankAccountInfo ?: BankAccountEntity(0, emptyString,  emptyString,  emptyString,  emptyString,  emptyString,  emptyString,  0.0)
            ViewBankAccountContent(
                bankInfo = bankAccount,
                currency = "GHS",
                isUpdatingBank = bankAccountViewModel.updateBankAccountState.value.isLoading,
                bankUpdatingIsSuccessful = bankAccountViewModel.updateBankAccountState.value.isSuccessful,
                bankUpdatingMessage = bankAccountViewModel.updateBankAccountState.value.message,
                getUpdatedBankName = { value-> bankAccountViewModel.updateBankName(value)},
                getUpdatedBankAccountName = { value-> bankAccountViewModel.updateBankAccountName(value)},
                getUpdatedBankContact = {value-> bankAccountViewModel.updateBankContact(value)},
                getUpdatedBankLocation = {value-> bankAccountViewModel.updateBankLocation(value)},
                getUpdatedOtherInfo = { value-> bankAccountViewModel.updateBankOtherInfo(value)},
                updateBank = { bankAccountViewModel.bankAccountInfo?.let { _bank -> bankAccountViewModel.updateBankAccount(_bank) } },
            ){
                navigateBack()
            }
        }
    }
}
