package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.EnterReceiptId
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryDayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryReceiptPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SelectInventoryDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun AddInventoryContent(
    inventory: InventoryEntity,
    deleteMessage: String?,
    saveInventoryMessage: String?,
    isSavingInventory: Boolean,
    isDeletingInventory: Boolean,
    inventorySavingIsSuccessful: Boolean,
    inventoryDeletionIsSuccessful: Boolean,
    inventoryDisplayValues: List<InventoryQuantityDisplayValues>,
    mapOfInventoryItems: Map<String, String>,
    getInventoryItem: (String) -> InventoryItemEntity?,
    addInventoryDate: (String) -> Unit,
    addOtherInfo: (String) -> Unit,
    addCostPrices: (String, String) -> Unit,
    addReceiptId: (String) -> Unit,
    addInventoryItemId: (String) -> Unit,
    addInventoryDisplayCardValue: (List<InventoryQuantityDisplayValues>) -> Unit,
    addInventoryQuantities: (ItemQuantities) -> Unit,
    addNewInventoryItem: () -> Unit,
    clearInventory: () -> Unit,
    deleteInventory: (String) -> Unit,
    addInventoryEntity: (InventoryEntity) -> Unit,
    navigateBack: () -> Unit,
) {

    val context = LocalContext.current

    var inventoryQuantityDisplayValues by remember {
        mutableStateOf<InventoryQuantityDisplayValues?>(null)
    }
    var openInventoryDisplay by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var deleteConfirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var deleteInventoryValueItem by remember {
        mutableStateOf<InventoryQuantityDisplayValues?>(null)
    }

    BasicScreenColumnWithoutBottomBar {
        // Select Date
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            DatePickerTextField(
                defaultDate = "${inventory.dayOfWeek}, ${inventory.date.toDateString()}",
                context = context,
                onValueChange = { _dateString ->
                    addInventoryDate(_dateString)
                },
                label = SelectInventoryDate
            )
        }

        // Day
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField1(
                value = inventory.dayOfWeek,
                onValueChange = {},
                placeholder = emptyString,
                label = InventoryDayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // Receipt Id
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisReceiptId by remember { mutableStateOf(inventory.receiptId.toNotNull()) }
            BasicTextFieldWithTrailingIcon(
                value = thisReceiptId,
                onValueChange = {
                    thisReceiptId = it
                    addReceiptId(thisReceiptId)
                },
                placeholder = InventoryReceiptPlaceholder,
                label = EnterReceiptId,
                icon = R.drawable.ic_receipt,
                keyboardType = KeyboardType.Text
            )
        }

        // Short Description
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var otherInfo by remember {
                mutableStateOf(inventory.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = otherInfo,
                onValueChange = {
                    otherInfo = it
                    addOtherInfo(it)
                },
                placeholder = emptyString,
                label = EnterShortDescription,
                icon = R.drawable.ic_short_notes,
                keyboardType = KeyboardType.Text
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

        // Add Inventory item
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            if (!openInventoryDisplay) {
                BasicButton(buttonName = "Add inventory") {
                    openInventoryDisplay = true
                }
            }
        }


        // Create and add Inventory item
        AnimatedVisibility(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            visible = openInventoryDisplay
        ) {
            val inventoryItem by remember {
                mutableStateOf<InventoryItemEntity?>(null)
            }
            InventoryItemQuantitiesAndCost(
                closeInventoryDisplay = { openInventoryDisplay = false },
                inventory = inventory,
                thisInventoryItem = inventoryItem,
                mapOfInventoryItems = mapOfInventoryItems,
                addNewInventoryItem = { addNewInventoryItem() },
                addInventoryItemId = { addInventoryItemId(it) },
                getInventoryItem = { getInventoryItem(it) },
                addCostPrices = { unitCost, totalCost -> addCostPrices(unitCost, totalCost) },
                addInventoryEntity = { addInventoryEntity(it) },
                addInventoryQuantities = { addInventoryQuantities(it) },
                openConfirmationDialog = { confirmationInfoDialog = true }
            ) { inventoryQuantityDisplayValues = it }
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

        // Display Inventory Items
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            InventoryDisplayCard(inventoryCardDisplayValues = inventoryDisplayValues )
            { _inventoryQuantityValue ->
                deleteInventoryValueItem = _inventoryQuantityValue
                openDeleteConfirmation = !openDeleteConfirmation
            }
        }

        Box(
            modifier = Modifier.padding(
                horizontal = LocalSpacing.current.small,
                vertical = LocalSpacing.current.smallMedium,
            ),
            contentAlignment = Alignment.Center
        ) {
            BasicButton(buttonName = "Go back") {
                navigateBack()
            }
        }

        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Inventory",
            textContent = "Are you sure you want to delete this inventory?",
            unconfirmedDeletedToastText = "Did not delete inventory",
            confirmedDeleteToastText = deleteMessage,
            confirmDelete = {
                val uniqueInventoryId = deleteInventoryValueItem?.inventoryItemEntity?.uniqueInventoryItemId ?: emptyString
                deleteInventory(uniqueInventoryId)
                deleteConfirmationInfoDialog = !deleteConfirmationInfoDialog
            }) {
            openDeleteConfirmation = false
        }

        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = isSavingInventory,
            title = null,
            textContent = saveInventoryMessage ?: emptyString,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            if (inventorySavingIsSuccessful) {
                val inventoryDisplayCardValues = (inventoryDisplayValues.plus(inventoryQuantityDisplayValues).filterNotNull())
                addInventoryDisplayCardValue(inventoryDisplayCardValues)
                clearInventory()
            }
            confirmationInfoDialog = false
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
                val thisInventoryDisplayValues = (inventoryDisplayValues.minus(deleteInventoryValueItem!!))
                addInventoryDisplayCardValue(thisInventoryDisplayValues)
            }
            deleteConfirmationInfoDialog = false
        }
    }
}

