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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account.AddBankAccountContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel

@Composable
fun AddBankAccountScreen(
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    navigateBack: ()-> Unit
) {
    LaunchedEffect(Unit){
        bankAccountViewModel.getAllBanks()
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Bank Account") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddBankAccountContent(
                addBankAccount = { bankEntity -> bankAccountViewModel.addBankAccount(bankEntity) },
                bankSavingMessage = bankAccountViewModel.addBankAccountState.value.message,
                isSavingBank = bankAccountViewModel.addBankAccountState.value.isLoading,
                bankSavingIsSuccessful = bankAccountViewModel.addBankAccountState.value.isSuccessful,
                navigateBack = navigateBack,
            )
        }
    }
}
