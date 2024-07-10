package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.savings.screens

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.savings.SavingsListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.savings.SavingsListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SavingsViewModel


@Composable
fun SavingsListScreen(
    savingsViewModel: SavingsViewModel,
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateToAddSavingsScreen: () -> Unit,
    navigateToViewSavingsScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit){
        savingsViewModel.getAllSavings()
        bankAccountViewModel.getAllBankAccounts()
        personnelViewModel.getAllPersonnel()
    }
    var openPDFDialog by remember {
        mutableStateOf(false)
    }
    var openDialogInfo by remember {
        mutableStateOf(false)
    }
    var dialogMessage by remember {
        mutableStateOf(emptyString)
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

    var allSavings = savingsViewModel.savingsEntitiesState.value.savingsEntities ?: emptyList()
    val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
    val allBankAccounts = bankAccountViewModel.bankAccountEntitiesState.value.bankAccountEntities ?: emptyList()

    val mapOfBankAccounts = mutableMapOf<String, String>()
    allBankAccounts.forEach { bankAccount->
        mapOfBankAccounts[bankAccount.uniqueBankAccountId] = bankAccount.bankAccountName
    }
    Scaffold(
        topBar = {
            SavingsListScreenTopBar(
                entireSavings = savingsViewModel.savingsEntitiesState.value.savingsEntities
                    ?: emptyList(),
                allSavings = allSavings,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                showDateRangePickerBar = openDateRangePickerBar,
                getPersonnelName = {_uniquePersonnelId ->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@SavingsListScreenTopBar "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
                },
                getBankAccountName = {_uniqueBankAccountId ->
                    val bankAccount = allBankAccounts.firstOrNull { it.uniqueBankAccountId == _uniqueBankAccountId }
                    return@SavingsListScreenTopBar bankAccount?.bankAccountName.toNotNull()
                },
                openDialogInfo = {
                    dialogMessage = it
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                closeSearchBar = { openSearchBar = false },
                closeDateRangePickerBar = { openDateRangePickerBar = false },
                closeComparisonBar = { openComparisonBar = false },
                openComparisonBar = { openComparisonBar = true },
                openDateRangePickerBar = { openDateRangePickerBar = true },
                printSavings = {
                    savingsViewModel.generateSavingsListPDF(context, allSavings, mapOfBankAccounts)
                    openPDFDialog = !openPDFDialog
                },
                getSavings = { allSavings = it }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddSavingsScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SavingsListContent(
                allSavings = allSavings,
                isDeletingSavings = savingsViewModel.deleteSavingsState.value.isLoading,
                savingsDeletionMessage = savingsViewModel.deleteSavingsState.value.message,
                savingsDeletionIsSuccessful = savingsViewModel.deleteSavingsState.value.isSuccessful,
                getBankAccountName = {_uniqueBankAccountId ->
                    val bankAccount = allBankAccounts.firstOrNull { it.uniqueBankAccountId == _uniqueBankAccountId }
                    return@SavingsListContent bankAccount?.bankAccountName.toNotNull()
                },
                reloadAllSavings = { savingsViewModel.getAllSavings() },
                onConfirmDelete = {_uniqueSavingsId->
                    savingsViewModel.deleteSavings(_uniqueSavingsId)
                }
            ) { _uniqueSavingsId ->
                navigateToViewSavingsScreen(_uniqueSavingsId)
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
            isLoading = savingsViewModel.generateSavingsListState.value.isLoading,
            title = null,
            textContent = savingsViewModel.generateSavingsListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openPDFDialog = false
        }
    }
}
