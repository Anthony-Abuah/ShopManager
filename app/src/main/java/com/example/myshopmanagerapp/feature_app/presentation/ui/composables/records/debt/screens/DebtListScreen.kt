package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.screens

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
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.DebtListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.DebtListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel


@Composable
fun DebtListScreen(
    debtViewModel: DebtViewModel,
    customerViewModel: CustomerViewModel= hiltViewModel(),
    personnelViewModel: PersonnelViewModel= hiltViewModel(),
    navigateToAddDebtScreen: () -> Unit,
    navigateToViewDebtScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit){
        debtViewModel.getAllDebt()
        customerViewModel.getAllCustomers()
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
    var openDateRangeBar by remember {
        mutableStateOf(false)
    }
    var openSearchBar by remember {
        mutableStateOf(false)
    }
    var allDebts = debtViewModel.debtEntitiesState.value.debtEntities?.sortedByDescending { it.date } ?: emptyList()
    val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
    val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
    val mapOfCustomers = mutableMapOf<String, String>()
    allCustomers.forEach { customer->
        mapOfCustomers[customer.uniqueCustomerId] = customer.customerName
    }
    Scaffold(
        topBar = {
            DebtListScreenTopBar(
                entireDebts = debtViewModel.debtEntitiesState.value.debtEntities ?: emptyList(),
                allDebts = allDebts,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                showDateRangePickerBar = openDateRangeBar,
                getPersonnelName = {_uniquePersonnelId ->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@DebtListScreenTopBar "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
                },
                getCustomerName = {_uniqueCustomerId ->
                    val customer = allCustomers.firstOrNull { it.uniqueCustomerId == _uniqueCustomerId }
                    return@DebtListScreenTopBar "${customer?.customerName}"
                },
                openDialogInfo = {_dialogMessage ->
                    dialogMessage = _dialogMessage
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                openDateRangePickerBar = { openDateRangeBar = true },
                closeSearchBar = { openSearchBar = false },
                closeDateRangePickerBar = { openDateRangeBar = false },
                closeComparisonBar = { openComparisonBar = true },
                openComparisonBar = { openComparisonBar = true },
                printDebts = {
                    debtViewModel.generateDebtListPDF(context, allDebts, mapOfCustomers)
                    openPDFDialog = !openPDFDialog
                },
                getDebts = { allDebts = it }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddDebtScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DebtListContent(
                allDebts = allDebts,
                debtDeletionIsSuccessful = debtViewModel.deleteDebtState.value.isSuccessful,
                debtDeletionMessage = debtViewModel.deleteDebtState.value.message,
                isDeletingDebt = debtViewModel.deleteDebtState.value.isLoading,
                reloadAllDebts = { debtViewModel.getAllDebt() },
                getCustomerName = { _uniqueCustomerId ->
                    val thisCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
                    return@DebtListContent thisCustomers.firstOrNull<CustomerEntity>{_customer -> _customer.uniqueCustomerId == _uniqueCustomerId }?.customerName.toNotNull()
                },
                onConfirmDelete = {_uniqueDebtId->
                    debtViewModel.deleteDebt(_uniqueDebtId)
                }
            ) {_uniqueDebtId->
                navigateToViewDebtScreen(_uniqueDebtId)
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
            isLoading = debtViewModel.generateDebtListState.value.isLoading,
            title = null,
            textContent = debtViewModel.generateDebtListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openPDFDialog = false
        }
    }

}
