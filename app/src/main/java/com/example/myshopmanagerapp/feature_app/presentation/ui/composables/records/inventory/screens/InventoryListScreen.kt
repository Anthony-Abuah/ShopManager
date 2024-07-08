package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.screens

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
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.InventoryListContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel


@Composable
fun InventoryListScreen(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    navigateToAddInventoryScreen: () -> Unit,
    navigateToViewInventoryScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect( Unit ){
        inventoryViewModel.getAllInventories()
        inventoryItemViewModel.getAllInventoryItems()
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Inventory List") {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddInventoryScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allInventories = inventoryViewModel.inventoryEntitiesState.value.inventoryEntities ?: emptyList()
            val allSortedInventories = allInventories.sortedByDescending {inventory-> inventory.date }
            val isLoading = inventoryViewModel.inventoryEntitiesState.value.isLoading
            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
            val mapOfInventoryItems = mutableMapOf<String, String>()
            val mapOfInventoryItemsInverse = mutableMapOf<String, String>()
            allInventoryItems.forEach {inventoryItem->
                mapOfInventoryItems[inventoryItem.inventoryItemName] = inventoryItem.uniqueInventoryItemId
                mapOfInventoryItemsInverse[inventoryItem.uniqueInventoryItemId] = inventoryItem.inventoryItemName
            }

            InventoryListContent(
                allInventories = allSortedInventories,
                isLoading = isLoading,
                isDeletingInventory = inventoryViewModel.deleteInventoryState.value.isLoading,
                inventoryDeletionIsSuccessful = inventoryViewModel.deleteInventoryState.value.isSuccessful,
                deleteMessage = inventoryViewModel.deleteInventoryState.value.message,
                printInventoryValues = { date, inventoryDisplayValues->
                    inventoryViewModel.generateInventoryListPDF(context, date, inventoryDisplayValues)
                    confirmationInfoDialog = !confirmationInfoDialog
                },
                getInventoryItem = { inventoryItemId->
                    return@InventoryListContent allInventoryItems.firstOrNull { it.uniqueInventoryItemId == inventoryItemId }
                },
                deleteInventoryItem = { uniqueInventoryId ->
                    inventoryViewModel.deleteInventory(uniqueInventoryId)
                },
                reloadInventoryItems = { inventoryViewModel.getAllInventories() }
            ) { uniqueInventoryId ->
                navigateToViewInventoryScreen(uniqueInventoryId)
            }
        }

        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = inventoryViewModel.generateInventoryListState.value.isLoading,
            title = null,
            textContent = inventoryViewModel.generateInventoryListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            confirmationInfoDialog = false
        }

    }
}
