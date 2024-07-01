package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.InventoryEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*


@Composable
fun InventoryListContent(
    allInventories: InventoryEntities,
    isLoading: Boolean,
    isDeletingInventory: Boolean,
    inventoryDeletionIsSuccessful: Boolean,
    deleteMessage: String?,
    printInventoryValues: (date: String, List<InventoryQuantityDisplayValues>) -> Unit,
    getInventoryItem: (String) -> InventoryItemEntity?,
    deleteInventoryItem: (String) -> Unit,
    reloadInventoryItems: () -> Unit,
    navigateToViewInventoryScreen: (String) -> Unit,
) {
    var inventoryDisplayValue by remember {
        mutableStateOf<InventoryQuantityDisplayValues?>(null)
    }
    var uniqueInventoryId by remember {
        mutableStateOf(emptyString)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var deleteConfirmationInfoDialog by remember {
        mutableStateOf(false)
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
        if (allInventories.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No inventories have been added yet!",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        else {
            val groupedInventories = allInventories.groupBy { inventory ->
                val dayOfWeek = inventory.dayOfWeek.lowercase(Locale.ROOT)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                val dateString = inventory.date.toDateString()
                "$dayOfWeek, $dateString"
            }

            BasicScreenColumnWithoutBottomBar {
                groupedInventories.forEach { groupedInventory ->
                    val date = groupedInventory.key
                    val displayValues = groupedInventory.value.map {_inventoryEntity ->
                        val inventoryItem = getInventoryItem(_inventoryEntity.uniqueInventoryItemId)
                        inventoryItem?.let {it-> _inventoryEntity.toInventoryQuantityDisplayValues(it) }
                    }
                    Box(
                        modifier = Modifier.padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InventoryCard(
                            date = date,
                            listOfDisplayValues = displayValues.filterNotNull(),
                            printInventoryValues = {_displayValues->
                                printInventoryValues(date, _displayValues)
                            },
                            getSelectedDisplayCardValues = {_inventoryDisplayValue ->
                                inventoryDisplayValue = _inventoryDisplayValue
                                uniqueInventoryId = _inventoryDisplayValue?.uniqueInventoryId ?: emptyString
                                navigateToViewInventoryScreen(uniqueInventoryId)
                            }
                        ) { _inventoryDisplayValue ->
                            inventoryDisplayValue = _inventoryDisplayValue
                            uniqueInventoryId = _inventoryDisplayValue?.uniqueInventoryId ?: emptyString
                            openDeleteConfirmation = !openDeleteConfirmation
                        }
                    }
                }
            }
        }
    }

    DeleteConfirmationDialog(
        openDialog = openDeleteConfirmation,
        title = "Delete Inventory",
        textContent = "Are you sure you want to delete this inventory?",
        unconfirmedDeletedToastText = "Did not delete inventory",
        confirmedDeleteToastText = null,
        confirmDelete = {
            deleteInventoryItem(uniqueInventoryId)
            deleteConfirmationInfoDialog = !deleteConfirmationInfoDialog
        }) {
        openDeleteConfirmation = false
    }
    ConfirmationInfoDialog(
        openDialog = deleteConfirmationInfoDialog,
        isLoading = isDeletingInventory,
        title = null,
        textContent = deleteMessage ?: emptyString,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (inventoryDeletionIsSuccessful){
            reloadInventoryItems()
        }
        deleteConfirmationInfoDialog = false
    }

}
