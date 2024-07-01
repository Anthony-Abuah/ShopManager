package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.CustomerListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.CustomerListScreenTopBar
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
            /*if (openSearchBar){
                SearchTopBar(placeholder = "Search customer...",
                    getSearchValue = {_searchValue->
                        if (_searchValue.isBlank()){
                            openSearchBar = false
                        }else{
                            allCustomers = allCustomers.filter { it.customerName.contains(_searchValue)}
                            openSearchBar = false
                            openDialogInfo = true
                            openDialogInfo = false
                        }
                    }
                )
            }
            else {
                if (openComparisonBar){
                    val comparisonPlaceholder = if (dialogMessage == "5") "Enter minimum debt value"
                    else "Enter maximum debt"
                    ComparisonTopBar2(placeholder = comparisonPlaceholder,
                        getComparisonValue = {_value->
                            if (_value.isNotBlank()) {
                                if (dialogMessage == "5") {
                                    allCustomers = allCustomers.filter { it.debtAmount.toNotNull() >= convertToDouble(_value) }
                                    Toast.makeText(context, "Customers with debt amount greater than $_value are fetched", Toast.LENGTH_LONG).show()
                                } else {
                                    allCustomers = allCustomers.filter { it.debtAmount.toNotNull() <= convertToDouble(_value) }
                                    Toast.makeText(context, "Customers with debt amount less than $_value are fetched", Toast.LENGTH_LONG).show()
                                }
                            }
                            openComparisonBar = false
                            openDialogInfo = true
                            openDialogInfo = false
                        })
                }
                else {
                    CustomerScreenTopBar(
                        topBarTitleText = "Customers",
                        onClickPDF = {
                            if (allCustomers.isEmpty()){
                                Toast.makeText(context, "No customers selected", Toast.LENGTH_LONG).show()
                            }else{
                                customerViewModel.generateCustomerListPDF(context, allCustomers)
                                openPDFDialog = !openPDFDialog
                            }
                        },
                        onSort = { _value ->
                            when (_value.number) {
                                1 -> {
                                    allCustomers = allCustomers.sortedBy { it.customerName.take(1) }
                                    dialogMessage = "Customers are sorted in ascending alphabetical order"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                2 -> {
                                    allCustomers = allCustomers.sortedByDescending { it.customerName.take(1) }
                                    dialogMessage = "Customers are sorted in descending alphabetical order"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                3 -> {
                                    allCustomers = allCustomers.sortedBy { it.debtAmount }
                                    dialogMessage = "Customers are sorted in ascending order of debts"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                4 -> {
                                    allCustomers = allCustomers.sortedByDescending { it.debtAmount }
                                    dialogMessage = "Customers are sorted in descending order of debts"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                5 -> {
                                    dialogMessage = "5"
                                    openComparisonBar = !openComparisonBar
                                }
                                6 -> {
                                    dialogMessage = "6"
                                    openComparisonBar = !openComparisonBar
                                }

                            }
                        },
                        listDropDownItems = listOfListNumbers,
                        onClickListItem = { _listNumber ->
                            selectedNumber = _listNumber.number
                            val number = _listNumber.number
                            when (_listNumber.number) {
                                0 -> {
                                    allCustomers = customerViewModel.customerEntitiesState.value.customerEntities
                                        ?: emptyList()
                                    dialogMessage = "All customers are selected"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                1 -> {
                                    Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                                    openSearchBar = !openSearchBar
                                }
                                2 -> {
                                    dialogMessage =
                                        "Total number of customers on this list are ${allCustomers.size}" +
                                        "\nTotal amount of debts on this list is GHS ${
                                            allCustomers.sumOf { it.debtAmount.toNotNull() }
                                            .toTwoDecimalPlaces()}"
                                    openDialogInfo = !openDialogInfo
                                }
                                else -> {
                                    allCustomers = allCustomers.take(number)
                                    dialogMessage = "First $number customers selected"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        listOfSortItems = listOfCustomerSortItems,
                    ) {
                        navigateBack()
                    }
                }
            }*/
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


