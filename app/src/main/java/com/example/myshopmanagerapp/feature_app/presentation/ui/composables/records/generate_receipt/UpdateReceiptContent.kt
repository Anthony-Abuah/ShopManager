package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.CustomerEntities
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.ReceiptCustomerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ReceiptDayOfWeek
import com.example.myshopmanagerapp.core.FormRelatedString.SelectReceiptCustomer
import com.example.myshopmanagerapp.core.FormRelatedString.SelectReceiptDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*


@Composable
fun UpdateReceiptContent(
    receipt: ReceiptEntity,
    receiptCreatedMessage: String,
    receiptIsCreated: Boolean,
    receiptDisplayItems: List<ItemQuantityInfo>,
    inventoryItems: List<InventoryItemEntity>,
    allCustomers: CustomerEntities,
    updateReceiptDate: (String) -> Unit,
    updateReceiptItems: (List<ItemQuantityInfo>) -> Unit,
    createInventoryItem: () -> Unit,
    updateCustomer: (CustomerEntity?) -> Unit,
    addNewCustomer: () -> Unit,
    updateReceipt: () -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val thisCurrency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value
    val currency = if (thisCurrency.isNullOrBlank()) FormRelatedString.GHS else thisCurrency


    var customer by remember {
        mutableStateOf<CustomerEntity?>(null)
    }
    var customerName by remember {
        mutableStateOf(emptyString)
    }

    var openAddItemDisplayDialog by remember {
        mutableStateOf(false)
    }
    var addItemDisplayDialogMessage by remember {
        mutableStateOf(emptyString)
    }
    var openReceiptView by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var confirmationPromptDialog by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var deleteConfirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var deleteReceiptItem by remember {
        mutableStateOf<ItemQuantityInfo?>(null)
    }

    BasicScreenColumnWithoutBottomBar {
        // Select Date
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            val dayOfWeek = receipt.date.toLocalDate().dayOfWeek.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val dateString = receipt.date.toDateString()
            DatePickerTextField(
                defaultDate = "$dayOfWeek, $dateString",
                context = context,
                onValueChange = { _dateString ->
                    updateReceiptDate(_dateString)
                },
                label = SelectReceiptDate
            )
        }

        // Day
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            val dayOfWeek = receipt.date.toLocalDate().dayOfWeek.toString().lowercase()
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                    else it.toString()
                }

            BasicTextField1(
                value = dayOfWeek,
                onValueChange = {},
                placeholder = emptyString,
                label = ReceiptDayOfWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // Receipt Customer
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            AutoCompleteWithAddButton(
                value = receipt.customerName,
                label = SelectReceiptCustomer,
                listItems = allCustomers.map { it.customerName },
                placeholder = ReceiptCustomerPlaceholder,
                readOnly = false,
                expandedIcon = R.drawable.ic_person_filled,
                unexpandedIcon = R.drawable.ic_person_outline,
                onClickAddButton = { addNewCustomer() },
                getSelectedItem = {
                    customerName = it
                    customer = allCustomers.firstOrNull{customerEntity -> customerEntity.customerName == it }
                    updateCustomer(customer)
                }
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

        // Add Receipt item button
        Box(
            modifier = Modifier.padding(LocalSpacing.current.default),
            contentAlignment = Alignment.Center
        ) {
            if (!openReceiptView) {
                BasicButton(buttonName = "Update Receipt Item") {
                    openReceiptView = true
                }
            }
        }

        // Create and add Receipt items
        CreateAndAddReceipt(
            openReceiptView = openReceiptView,
            inventoryItems = inventoryItems,
            receiptDisplayItems = receiptDisplayItems,
            createInventoryItem = { createInventoryItem() },
            getItemDisplayDialogMessage = { addItemDisplayDialogMessage = it },
            openOrCloseItemDisplayDialog = { openAddItemDisplayDialog = !openAddItemDisplayDialog },
            getReceiptItems = { updateReceiptItems(it) }
        ) { openReceiptView = false }


        // Display Receipt Items
        Box(
            modifier = Modifier.padding(LocalSpacing.current.default),
            contentAlignment = Alignment.Center
        ) {
            ReceiptItemDisplayCard(
                currency = currency,
                receiptItems = receiptDisplayItems
            )
            { _inventoryValue ->
                deleteReceiptItem = _inventoryValue
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
            BasicButton(buttonName = "Update Receipt") {
                confirmationPromptDialog = !confirmationPromptDialog
            }
        }
    }

    DeleteConfirmationDialog(
        openDialog = openDeleteConfirmation,
        title = "Remove Item",
        textContent = "Are you sure you want to remove this ${deleteReceiptItem?.itemName}?",
        unconfirmedDeletedToastText = "Did not delete item",
        confirmedDeleteToastText = "${deleteReceiptItem?.itemName} is removed",
        confirmDelete = {
            updateReceiptItems(receiptDisplayItems.minus(deleteReceiptItem!!))
            addItemDisplayDialogMessage = "${deleteReceiptItem?.itemName} removed from receipt list"
            deleteConfirmationInfoDialog = !deleteConfirmationInfoDialog
        }) {
        openDeleteConfirmation = false
    }

    UpdateConfirmationDialog(
        openDialog = confirmationPromptDialog,
        title = "Confirm",
        textContent = "Are you sure you want to update this receipt?",
        unconfirmedUpdatedToastText = null,
        confirmedUpdatedToastText = null,
        confirmUpdate = {
            updateReceipt()
            confirmationInfoDialog = !confirmationInfoDialog
        }) {
        confirmationPromptDialog = false
    }
    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = false,
        title = null,
        textContent = receiptCreatedMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (receiptIsCreated){
            navigateBack()
        }
        confirmationInfoDialog = false
    }


    ConfirmationInfoDialog(
        openDialog = openAddItemDisplayDialog,
        isLoading = false,
        title = null,
        textContent = addItemDisplayDialogMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        openAddItemDisplayDialog = false
    }

}

