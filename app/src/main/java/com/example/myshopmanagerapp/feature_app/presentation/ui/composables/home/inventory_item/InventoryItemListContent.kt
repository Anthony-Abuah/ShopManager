package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.InventoryItemEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun InventoryItemListContent(
    allInventoryItems: InventoryItemEntities?,
    isLoading: Boolean,
    isDeletingInventoryItem: Boolean,
    inventoryItemDeletionIsSuccessful: Boolean,
    deleteMessage: String?,
    reloadInventoryItems:()-> Unit,
    onConfirmDelete:(String)-> Unit,
    navigateToViewInventoryItemScreen: (String)-> Unit
) {
    var deleteConfirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueInventoryItemId by remember {
        mutableStateOf(emptyString)
    }
    if (isLoading){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
    else {
        if (allInventoryItems.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No inventory items to show",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            BasicScreenColumnWithoutBottomBar {
                allInventoryItems.forEachIndexed { index, inventoryItem ->
                    val itemName = inventoryItem.inventoryItemName
                    val sellingPrice = inventoryItem.currentSellingPrice?.toString() ?: "N/A"
                    val costPrice = inventoryItem.currentCostPrice ?: 0.0
                    val manufacturer = inventoryItem.manufacturerName ?: emptyString
                    if (index == 0){
                        HorizontalDivider()
                    }
                    Box(
                        modifier = Modifier.padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InventoryItemCard(
                            inventoryItemName = itemName,
                            sellingPrice = sellingPrice,
                            manufacturer = manufacturer,
                            costPrice = costPrice.toString(),
                            number = index.plus(1).toString(),
                            currency = "GHS",
                            onDelete = {
                                uniqueInventoryItemId = inventoryItem.uniqueInventoryItemId
                                openDeleteConfirmation = !openDeleteConfirmation
                            }
                        ) {
                            navigateToViewInventoryItemScreen(inventoryItem.uniqueInventoryItemId)
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }




    DeleteConfirmationDialog(
        openDialog = openDeleteConfirmation,
        title = "Delete Inventory item",
        textContent = "Are your sure you want to permanently delete this inventory item",
        unconfirmedDeletedToastText = "InventoryItem not deleted",
        confirmedDeleteToastText = "InventoryItem has been successfully removed",
        confirmDelete = {
            onConfirmDelete(uniqueInventoryItemId)
            deleteConfirmationInfoDialog = !deleteConfirmationInfoDialog
        }
    ) {
        openDeleteConfirmation = false
    }

    ConfirmationInfoDialog(
        openDialog = deleteConfirmationInfoDialog,
        isLoading = isDeletingInventoryItem,
        title = null,
        textContent = deleteMessage ?: emptyString,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (inventoryItemDeletionIsSuccessful){
            reloadInventoryItems()
        }
        deleteConfirmationInfoDialog = false
    }


}
