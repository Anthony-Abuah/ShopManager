package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt_repayment.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt_repayment.DebtRepaymentListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt_repayment.DebtRepaymentListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtRepaymentViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel


@Composable
fun DebtRepaymentListScreen(
    debtRepaymentViewModel: DebtRepaymentViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel(),
    navigateToAddDebtRepaymentScreen: () -> Unit,
    navigateToViewDebtRepaymentScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit){
        debtRepaymentViewModel.getAllDebtRepayment()
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
    var openDateRangePickerBar by remember {
        mutableStateOf(false)
    }
    var openSearchBar by remember {
        mutableStateOf(false)
    }

    var allDebtRepayments = debtRepaymentViewModel.debtRepaymentEntitiesState.value.debtRepaymentEntities ?: emptyList()
    val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
    val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
    val mapOfCustomers = mutableMapOf<String, String>()
    allCustomers.forEach { customer-> mapOfCustomers[customer.uniqueCustomerId] = customer.customerName }

    Scaffold(
        topBar = {
            DebtRepaymentListScreenTopBar(
                entireDebtRepayments = debtRepaymentViewModel.debtRepaymentEntitiesState.value.debtRepaymentEntities
                    ?: emptyList(),
                allDebtRepayments = allDebtRepayments,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                showDateRangePickerBar = openDateRangePickerBar,
                getCustomerName = {_uniqueCustomerId ->
                    val customer = allCustomers.firstOrNull { it.uniqueCustomerId == _uniqueCustomerId }
                    return@DebtRepaymentListScreenTopBar "${customer?.customerName}"
                },
                getPersonnelName = {_uniquePersonnelId ->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@DebtRepaymentListScreenTopBar "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
                },
                openDialogInfo = { _dialogMessage ->
                    dialogMessage = _dialogMessage
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                closeSearchBar = { openSearchBar = false },
                closeDateRangePickerBar = { openDateRangePickerBar = false },
                closeComparisonBar = { openComparisonBar = false },
                openComparisonBar = { openComparisonBar = true },
                openDateRangePickerBar = { openDateRangePickerBar = true },
                printDebtRepayments = {
                    debtRepaymentViewModel.generateDebtRepaymentListPDF(context, allDebtRepayments, mapOfCustomers)
                    openPDFDialog = !openPDFDialog },
                getDebtRepayments = { allDebtRepayments = it }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddDebtRepaymentScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DebtRepaymentListContent(
                allDebtRepayments = allDebtRepayments,
                debtRepaymentDeletionIsSuccessful = debtRepaymentViewModel.deleteDebtRepaymentState.value.isSuccessful,
                debtRepaymentDeletionMessage = debtRepaymentViewModel.deleteDebtRepaymentState.value.message,
                isDeletingDebtRepayment = debtRepaymentViewModel.deleteDebtRepaymentState.value.isLoading,
                reloadAllDebtRepayments = { debtRepaymentViewModel.getAllDebtRepayment() },
                getCustomerName = {_uniqueCustomerId->
                    val thisCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
                    return@DebtRepaymentListContent thisCustomers.firstOrNull<CustomerEntity>{ _customer -> _customer.uniqueCustomerId == _uniqueCustomerId }?.customerName.toNotNull()
                },
                onConfirmDelete = {_uniqueDebtRepaymentId->
                    debtRepaymentViewModel.deleteDebtRepayment(_uniqueDebtRepaymentId)
                }
            ) { _uniqueDebtRepaymentId ->
                navigateToViewDebtRepaymentScreen(_uniqueDebtRepaymentId)
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
            isLoading = debtRepaymentViewModel.generateDebtRepaymentListState.value.isLoading,
            title = null,
            textContent = debtRepaymentViewModel.generateDebtRepaymentListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openPDFDialog = false
        }
    }
}
