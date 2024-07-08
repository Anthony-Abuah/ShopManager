package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses.ExpenseListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses.ExpenseListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.ExpenseViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel


@Composable
fun ExpenseListScreen(
    expenseViewModel: ExpenseViewModel,
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateToAddExpenseScreen: () -> Unit,
    navigateToViewExpenseScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        expenseViewModel.getAllExpenses()
        personnelViewModel.getAllPersonnel()
    }
    val context = LocalContext.current
    var openDialogInfo by remember {
        mutableStateOf(false)
    }
    var openPDFDialog by remember {
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
    var allExpenses = expenseViewModel.expenseEntitiesState.value.expenseEntities ?: emptyList()

    Scaffold(
        topBar = {
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            ExpenseListScreenTopBar(
                entireExpenses = expenseViewModel.expenseEntitiesState.value.expenseEntities ?: emptyList(),
                allExpenses = allExpenses,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                showDateRangePickerBar = openDateRangePickerBar,
                getPersonnelName = { _uniquePersonnelId ->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@ExpenseListScreenTopBar "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
                },
                openDialogInfo = {
                    dialogMessage = it
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                openDateRangePickerBar = { openDateRangePickerBar = true },
                closeSearchBar = { openSearchBar = false },
                closeDateRangePickerBar = { openDateRangePickerBar = false },
                closeComparisonBar = { openComparisonBar = false },
                openComparisonBar = { openComparisonBar = true },
                printExpenses = {
                    expenseViewModel.generateExpenseListPDF(context, allExpenses)
                    openPDFDialog = true
                },
                getExpenses = {allExpenses = it}
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddExpenseScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ExpenseListContent(
                allExpenses = allExpenses,
                navigateToViewExpenseScreen = {_uniqueExpenseId->
                    navigateToViewExpenseScreen(_uniqueExpenseId)
                },
                onConfirmDelete = {_uniqueExpenseId->
                    expenseViewModel.deleteExpense(_uniqueExpenseId)
                    expenseViewModel.getAllExpenses()
                }
            )
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
            isLoading = expenseViewModel.generateExpenseListState.value.isLoading,
            title = null,
            textContent = expenseViewModel.generateExpenseListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openPDFDialog = false
        }
    }
}
