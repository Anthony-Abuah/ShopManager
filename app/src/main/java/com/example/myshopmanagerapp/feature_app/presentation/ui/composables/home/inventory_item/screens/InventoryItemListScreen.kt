package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.InventoryItemListContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.InventoryItemListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel

@Composable
fun InventoryItemListScreen(
    inventoryItemViewModel: InventoryItemViewModel,
    navigateToAddInventoryItemScreen: () -> Unit,
    navigateToViewInventoryItemScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        inventoryItemViewModel.getAllInventoryItems()
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
    var allItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()

    Scaffold(
        topBar = {
            /*
            if (openSearchBar){
                SearchTopBar(placeholder = "Search item...",
                    getSearchValue = {_searchValue->
                        if (_searchValue.isBlank()){
                            openSearchBar = false
                        }else{
                            allItems = allItems.filter { it.inventoryItemName.contains(_searchValue)}
                            openSearchBar = false
                            openDialogInfo = true
                            openDialogInfo = false
                        }
                    }
                )
            }
            else {
                if (openComparisonBar){
                    val comparisonPlaceholder = if (dialogMessage == "5") "Enter minimum value"
                    else "Enter maximum value"
                    ComparisonTopBar2(placeholder = comparisonPlaceholder,
                        getComparisonValue = {_value->
                            if (_value.isNotBlank()) {
                                when(dialogMessage){
                                    "7"->{
                                        allItems = allItems.filter { it.currentCostPrice.toNotNull() >= Functions.convertToDouble(_value) }
                                        Toast.makeText(context, "Items with cost price greater than $_value are fetched", Toast.LENGTH_LONG).show()
                                    }
                                    "8"->{
                                        allItems = allItems.filter { it.currentCostPrice.toNotNull() <= Functions.convertToDouble(_value) }
                                        Toast.makeText(context, "Items with cost price less than $_value are fetched", Toast.LENGTH_LONG).show()
                                    }
                                    "9"->{
                                        allItems = allItems.filter { it.currentSellingPrice.toNotNull() >= Functions.convertToDouble(_value) }
                                        Toast.makeText(context, "Items with selling price greater than $_value are fetched", Toast.LENGTH_LONG).show()
                                    }
                                    else ->{
                                        allItems = allItems.filter { it.currentSellingPrice.toNotNull() <= Functions.convertToDouble(_value) }
                                        Toast.makeText(context, "Items with selling price less than $_value are fetched", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            openComparisonBar = false
                            openDialogInfo = true
                            openDialogInfo = false
                        })
                }else {
                    CustomerScreenTopBar(
                        topBarTitleText = "Inventory Items",
                        onSort = { _value ->
                            when (_value.number) {
                                1 -> {
                                    allItems = allItems.sortedBy { it.inventoryItemName.take(1) }
                                    dialogMessage = "Items are arranged in alphabetical order"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                2 -> {
                                    allItems = allItems.sortedByDescending { it.inventoryItemName.take(1) }
                                    dialogMessage = "Items are arranged in inverse alphabetical order"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                3 -> {
                                    allItems = allItems.sortedBy { it.currentSellingPrice }
                                    dialogMessage = "Items are arranged in ascending selling prices"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                4 -> {
                                    allItems = allItems.sortedByDescending { it.currentSellingPrice }
                                    dialogMessage = "Items are arranged in descending selling prices"
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
                                7 -> {
                                    dialogMessage = "7"
                                    openComparisonBar = !openComparisonBar
                                }
                                8 -> {
                                    dialogMessage = "8"
                                    openComparisonBar = !openComparisonBar
                                }
                                9 -> {
                                    dialogMessage = "9"
                                    openComparisonBar = !openComparisonBar
                                }
                                10 -> {
                                    dialogMessage = "10"
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
                                    allItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
                                    dialogMessage = "All items are selected"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                                1 -> {
                                    Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                                    openSearchBar = !openSearchBar
                                }
                                2 -> {
                                    dialogMessage = "Total number of items on this list are ${allItems.size}"
                                    openDialogInfo = !openDialogInfo
                                }
                                else -> {
                                    allItems = allItems.take(number)
                                    dialogMessage = "First $number customers selected"
                                    Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        listOfSortItems = listOfInventoryItemsSortItems,
                    ) {
                        navigateBack()
                    }
                }
            }
            */
            InventoryItemListScreenTopBar(
                entireInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities
                    ?: emptyList(),
                allInventoryItems = allItems,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                openDialogInfo = {
                    dialogMessage = it
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                closeSearchBar = { openSearchBar = false },
                closeComparisonBar = { openComparisonBar = false },
                openComparisonBar = { openComparisonBar = true },
                printInventoryItems = {},
                getInventoryItems = { allItems = it }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddInventoryItemScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val isLoading = inventoryItemViewModel.inventoryItemEntitiesState.value.isLoading
            InventoryItemListContent(
                allInventoryItems = allItems,
                isLoading = isLoading,
                isDeletingInventoryItem = inventoryItemViewModel.deleteInventoryItemState.value.isLoading,
                inventoryItemDeletionIsSuccessful = inventoryItemViewModel.deleteInventoryItemState.value.isSuccessful,
                deleteMessage = inventoryItemViewModel.deleteInventoryItemState.value.message,
                onConfirmDelete = {_uniqueItemId->
                    inventoryItemViewModel.deleteInventoryItem(_uniqueItemId)
                },
                navigateToViewInventoryItemScreen = {_uniqueItemId->
                    navigateToViewInventoryItemScreen(_uniqueItemId)
                },
                reloadInventoryItems = {
                    inventoryItemViewModel.getAllInventoryItems()
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
