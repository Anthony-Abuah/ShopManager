package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.supplier.screens

import android.widget.Toast
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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfSupplierSortItems
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.supplier.SupplierListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.supplier.SupplierListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SupplierViewModel


@Composable
fun SupplierListScreen(
    supplierViewModel: SupplierViewModel,
    navigateToAddSupplierScreen: () -> Unit,
    navigateToViewSupplierScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit){
        supplierViewModel.getAllSuppliers()
    }

    var openDialogInfo by remember {
        mutableStateOf(false)
    }
    var dialogMessage by remember {
        mutableStateOf(emptyString)
    }

    var openSearchBar by remember {
        mutableStateOf(false)
    }
    var selectedNumber by remember {
        mutableStateOf(0)
    }
    var allSuppliers = supplierViewModel.supplierEntitiesState.value.supplierEntities ?: emptyList()

    Scaffold(
        topBar = {
            if (openSearchBar){
                SearchTopBar(placeholder = "Search suppliers...",
                    getSearchValue = {_searchValue->
                        if (_searchValue.isBlank()){
                            openSearchBar = false
                        }else{
                            allSuppliers = allSuppliers.filter { it.supplierName.contains(_searchValue) || it.supplierLocation?.contains(_searchValue) == true || it.supplierRole?.contains(_searchValue) == true }
                            openSearchBar = false
                            openDialogInfo = true
                            openDialogInfo = false
                        }
                    }
                )
            }
            else {
                CustomerScreenTopBar(
                    topBarTitleText = "Suppliers",
                    onSort = { _value ->
                        when (_value.number) {
                            1 -> {
                                allSuppliers = allSuppliers.sortedBy { it.supplierName.take(1) }
                                dialogMessage = "Suppliers are sorted in alphabetical order"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                            2 -> {
                                allSuppliers = allSuppliers.sortedByDescending { it.supplierName.take(1) }
                                dialogMessage = "Suppliers are sorted in inverse alphabetical order"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    listDropDownItems = Constants.listOfListNumbers,
                    onClickListItem = { _listNumber ->
                        selectedNumber = _listNumber.number
                        val number = _listNumber.number
                        when (_listNumber.number) {
                            0 -> {
                                allSuppliers = supplierViewModel.supplierEntitiesState.value.supplierEntities
                                    ?: emptyList()
                                dialogMessage = "All suppliers are selected"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                            1 -> {
                                Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                                openSearchBar = !openSearchBar
                            }
                            2 -> {
                                dialogMessage = "Total number of suppliers on this list are ${allSuppliers.size}"
                                openDialogInfo = !openDialogInfo
                            }
                            else -> {
                                allSuppliers = allSuppliers.take(number)
                                dialogMessage = "First $number suppliers selected"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    listOfSortItems = listOfSupplierSortItems,
                ) {
                    navigateBack()
                }


            }
            SupplierListScreenTopBar(
                entireSuppliers = supplierViewModel.supplierEntitiesState.value.supplierEntities
                    ?: emptyList(),
                allSuppliers = allSuppliers,
                showSearchBar = openSearchBar,
                openDialogInfo = {
                    dialogMessage = it
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                closeSearchBar = { openSearchBar = false },
                printSuppliers = { /*TODO*/ },
                getSuppliers = {
                    allSuppliers = it
                }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddSupplierScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SupplierListContent(
                allSuppliers = allSuppliers,
                isDeletingSupplier = supplierViewModel.deleteSupplierState.value.isLoading,
                supplierDeletionIsSuccessful = supplierViewModel.deleteSupplierState.value.isSuccessful,
                supplierDeletingMessage = supplierViewModel.deleteSupplierState.value.message,
                reloadAllSupplier = { supplierViewModel.getAllSuppliers() },
                onConfirmDelete = {_uniqueSupplierId->
                    supplierViewModel.deleteSupplier(_uniqueSupplierId)
                },
                navigateToViewSupplierScreen = {_uniqueSupplierId->
                    navigateToViewSupplierScreen(_uniqueSupplierId)
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
    }
}
