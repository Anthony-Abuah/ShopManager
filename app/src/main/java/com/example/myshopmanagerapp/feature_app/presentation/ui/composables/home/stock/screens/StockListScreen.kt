package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock.screens

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock.StockListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock.StockListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.StockViewModel


@Composable
fun StockListScreen(
    stockViewModel: StockViewModel,
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateToAddStockScreen: () -> Unit,
    navigateToViewStockScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        stockViewModel.getAllStocks()
        inventoryItemViewModel.getAllInventoryItems()
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
    var allStocks = stockViewModel.stockEntitiesState.value.stockEntities ?: emptyList()
    val allItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
    val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()


    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Stock List") {
                navigateBack()
            }
            StockListScreenTopBar(
                entireStocks = stockViewModel.stockEntitiesState.value.stockEntities ?: emptyList(),
                allStocks = allStocks,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                showDateRangePickerBar = openDateRangePickerBar,
                getItemName = {_uniqueInventoryItemId->
                    return@StockListScreenTopBar allItems.firstOrNull { it.uniqueInventoryItemId == _uniqueInventoryItemId }?.inventoryItemName.toNotNull()
                },
                getPersonnelName = {_uniquePersonnelId->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@StockListScreenTopBar "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
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
                printStocks = { /*TODO*/ },
                getStocks = { allStocks = it }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddStockScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StockListContent(
                allStocks = allStocks.sortedByDescending {stock-> stock.date },
                deleteMessage = stockViewModel.deleteStockState.value.message,
                isDeletingStock = stockViewModel.deleteStockState.value.isLoading,
                deleteIsSuccessful = stockViewModel.deleteStockState.value.isSuccessful,
                getInventoryItemName = {_uniqueInventoryItemId->
                    return@StockListContent allItems.firstOrNull { it.uniqueInventoryItemId == _uniqueInventoryItemId }?.inventoryItemName.toNotNull()
                },
                onConfirmDelete = {_uniqueStockId->
                    stockViewModel.deleteStock(_uniqueStockId)
                },
                reloadStockItems = {
                    stockViewModel.getAllStocks()
                },
                navigateToViewStockScreen = {_uniqueStockId->
                    navigateToViewStockScreen(_uniqueStockId)
                }
            )
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
