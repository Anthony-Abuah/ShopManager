package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account.BankAccountListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account.BankAccountListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel


@Composable
fun BankAccountListScreen(
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    navigateToAddBankScreen: () -> Unit,
    navigateToViewBankScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        bankAccountViewModel.getAllBanks()
    }
    var openPDFDialog by remember {
        mutableStateOf(false)
    }
    var openDialogInfo by remember {
        mutableStateOf(false)
    }
    var dialogMessage by remember {
        mutableStateOf(Constants.emptyString)
    }
    var openComparisonBar by remember {
        mutableStateOf(false)
    }
    var openSearchBar by remember {
        mutableStateOf(false)
    }
    var allBankAccounts = bankAccountViewModel.bankAccountEntitiesState.value.bankAccountEntities ?: emptyList()

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Bank Accounts") {
                navigateBack()
            }
            BankAccountListScreenTopBar(
                entireBankAccounts = bankAccountViewModel.bankAccountEntitiesState.value.bankAccountEntities
                    ?: emptyList(),
                allBankAccounts = allBankAccounts,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                openDialogInfo = {
                    dialogMessage = it
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                openComparisonBar = { openComparisonBar = true },
                closeSearchBar = { openSearchBar = false },
                closeComparisonBar = { openComparisonBar = false },
                printBankAccounts = { /*TODO*/ },
                getBankAccounts = {
                    allBankAccounts = it
                }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddBankScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BankAccountListContent(
                allBanks = allBankAccounts,
                onConfirmDelete = {uniqueBankId->
                    bankAccountViewModel.deleteBankAccount(uniqueBankId)
                },
                bankDeletingIsSuccessful = bankAccountViewModel.deleteBankState.value.isSuccessful,
                bankDeletingMessage = bankAccountViewModel.deleteBankState.value.message,
                isDeletingBank = bankAccountViewModel.deleteBankState.value.isLoading,
                reloadBankInfo = { bankAccountViewModel.getAllBanks() },
            ) {uniqueBankId->
                navigateToViewBankScreen(uniqueBankId)
            }
        }
        ConfirmationInfoDialog(
            openDialog = openDialogInfo,
            isLoading = false,
            title = null,
            textContent = dialogMessage,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openDialogInfo = false
        }
    }
}
