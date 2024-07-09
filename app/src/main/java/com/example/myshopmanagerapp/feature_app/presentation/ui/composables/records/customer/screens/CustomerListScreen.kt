package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.CustomerListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.CustomerListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel


@Composable
fun CustomerListScreen(
    customerViewModel: CustomerViewModel,
    navigateToAddCustomerScreen: () -> Unit,
    navigateToViewCustomerScreen: (customerId: String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        customerViewModel.getAllCustomers()
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
    var allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()

    Scaffold(
        topBar = {
            CustomerListScreenTopBar(
                entireCustomers = customerViewModel.customerEntitiesState.value.customerEntities
                    ?: emptyList(),
                allCustomers = allCustomers,
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
                printCustomers = {
                    customerViewModel.generateCustomerListPDF(context, allCustomers)
                    openPDFDialog = !openPDFDialog
                },
                getCustomers = { allCustomers = it }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddCustomerScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomerListContent (
                customers = allCustomers,
                isDeletingCustomer = customerViewModel.deleteCustomerState.value.isLoading,
                customerDeletingMessage = customerViewModel.deleteCustomerState.value.message,
                customerDeletionIsSuccessful = customerViewModel.deleteCustomerState.value.isSuccessful,
                reloadAllCustomers = { customerViewModel.getAllCustomers() },
                onConfirmDelete = {_uniqueCustomerId->
                    customerViewModel.deleteCustomer(_uniqueCustomerId)
                }
            ){_uniqueCustomerId->
                navigateToViewCustomerScreen(_uniqueCustomerId)
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
            isLoading = customerViewModel.generateCustomerListState.value.isLoading,
            title = null,
            textContent = customerViewModel.generateCustomerListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openPDFDialog = false
        }
    }
}


