package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.withdrawal.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.withdrawal.WithdrawalListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.withdrawal.WithdrawalListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.WithdrawalViewModel


@Composable
fun WithdrawalListScreen(
    withdrawalViewModel: WithdrawalViewModel,
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    navigateToAddWithdrawalScreen: () -> Unit,
    navigateToViewWithdrawalScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit){
        withdrawalViewModel.getAllWithdrawals()
        bankAccountViewModel.getAllBanks()
        personnelViewModel.getAllPersonnel()
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
    var openDateRangePickerBar by remember {
        mutableStateOf(false)
    }
    var openSearchBar by remember {
        mutableStateOf(false)
    }

    val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
    var allWithdrawals = withdrawalViewModel.withdrawalEntitiesState.value.withdrawalEntities ?: emptyList()
    val allBankAccounts = bankAccountViewModel.bankAccountEntitiesState.value.bankAccountEntities ?: emptyList()

    val mapOfBankAccounts = mutableMapOf<String, String>()
    allBankAccounts.forEach { bankAccount->
        mapOfBankAccounts[bankAccount.uniqueBankAccountId] = bankAccount.bankAccountName
    }
    Scaffold(
        topBar = {
            WithdrawalListScreenTopBar(
                entireWithdrawals = withdrawalViewModel.withdrawalEntitiesState.value.withdrawalEntities ?: emptyList(),
                allWithdrawals = allWithdrawals,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                showDateRangePickerBar = openDateRangePickerBar,
                openDialogInfo = {
                    dialogMessage = it
                    openDialogInfo = !openDialogInfo
                },
                getPersonnelName = {_uniquePersonnelId ->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@WithdrawalListScreenTopBar "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
                },
                getBankAccountName = {_uniqueBankAccountId ->
                    val bankAccount = allBankAccounts.firstOrNull { it.uniqueBankAccountId == _uniqueBankAccountId }
                    return@WithdrawalListScreenTopBar bankAccount?.bankAccountName.toNotNull()
                },
                openSearchBar = { openSearchBar = true },
                closeSearchBar = { openSearchBar = false },
                closeDateRangePickerBar = { openDateRangePickerBar = false },
                closeComparisonBar = { openComparisonBar = false },
                openComparisonBar = { openComparisonBar = true },
                openDateRangePickerBar = { openDateRangePickerBar = true },
                printWithdrawal = {
                    withdrawalViewModel.generateWithdrawalListPDF(context, allWithdrawals, mapOfBankAccounts)
                    openPDFDialog = !openPDFDialog
                },
                getWithdrawal = { allWithdrawals = it }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddWithdrawalScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WithdrawalListContent(
                allWithdrawal = allWithdrawals,
                withdrawalDeletionIsSuccessful = withdrawalViewModel.deleteWithdrawalState.value.isSuccessful,
                withdrawalDeletionMessage = withdrawalViewModel.deleteWithdrawalState.value.message,
                isDeletingWithdrawal = withdrawalViewModel.deleteWithdrawalState.value.isLoading,
                reloadAllWithdrawal = { withdrawalViewModel.getAllWithdrawals() },
                getBankName = {_uniqueBankAccountId ->
                    val bankAccount = allBankAccounts.firstOrNull { it.uniqueBankAccountId == _uniqueBankAccountId }
                    return@WithdrawalListContent bankAccount?.bankAccountName.toNotNull()
                },
                getPersonnelName = {_uniquePersonnelId ->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@WithdrawalListContent "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
                },
                onConfirmDelete = {_uniqueWithdrawalId->
                    withdrawalViewModel.deleteWithdrawal(_uniqueWithdrawalId)
                }
            ) { _uniqueWithdrawalId ->
                navigateToViewWithdrawalScreen(_uniqueWithdrawalId)
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
        ConfirmationInfoDialog(
            openDialog = openPDFDialog,
            isLoading = withdrawalViewModel.generateWithdrawalListState.value.isLoading,
            title = null,
            textContent = withdrawalViewModel.generateWithdrawalListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openPDFDialog = false
        }
    }
}
