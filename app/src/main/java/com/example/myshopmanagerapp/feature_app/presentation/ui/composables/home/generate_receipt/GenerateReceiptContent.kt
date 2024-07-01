package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.generate_receipt

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.Cancel
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemQuantityPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.AddInventoryQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.AddItem
import com.example.myshopmanagerapp.core.FormRelatedString.CostPricePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterTotalCostPrice
import com.example.myshopmanagerapp.core.FormRelatedString.EnterUnitCostPrice
import com.example.myshopmanagerapp.core.FormRelatedString.ReceiptCustomerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ReceiptDayOfWeek
import com.example.myshopmanagerapp.core.FormRelatedString.SelectInventoryItem
import com.example.myshopmanagerapp.core.FormRelatedString.SelectReceiptCustomer
import com.example.myshopmanagerapp.core.FormRelatedString.SelectReceiptDate
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityCategorization
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun GenerateReceiptContent(
    dateString: String,
    dayOfWeek: String,
    receiptCreatedMessage: String,
    receiptIsCreated: Boolean,
    receiptDisplayItems: List<ItemQuantityInfo>,
    inventoryItems: List<String>,
    mapOfCustomers: Map<String, String>,
    addReceiptDate: (String) -> Unit,
    getUniqueCustomerId: (String) -> Unit,
    getCustomerName: (String) -> Unit,
    getReceiptItems: (List<ItemQuantityInfo>) -> Unit,
    createInventoryItem: () -> Unit,
    addCustomer: () -> Unit,
    saveReceipt: () -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    var itemQuantityInfo by remember {
        mutableStateOf<ItemQuantityInfo?>(null)
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
            DatePickerTextField(
                defaultDate = "$dayOfWeek, $dateString",
                context = context,
                onValueChange = { _dateString ->
                    addReceiptDate(_dateString)
                },
                label = SelectReceiptDate
            )
        }

        // Day
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
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
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisUniqueCustomerId by remember {
                mutableStateOf(emptyString)
            }
            AutoCompleteWithAddButton(
                label = SelectReceiptCustomer,
                listItems = mapOfCustomers.keys.toList(),
                placeholder = ReceiptCustomerPlaceholder,
                readOnly = false,
                expandedIcon = R.drawable.ic_person_filled,
                unexpandedIcon = R.drawable.ic_person_outline,
                onClickAddButton = { addCustomer() },
                getSelectedItem = {
                    thisUniqueCustomerId = mapOfCustomers[it].toNotNull()
                    getUniqueCustomerId(thisUniqueCustomerId)
                    getCustomerName(it)
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
                BasicButton(buttonName = "Add Receipt Item") {
                    openReceiptView = true
                }
            }
        }

        // Create and add Receipt items
        AnimatedVisibility(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            visible = openReceiptView
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                var itemQuantities by remember {
                    mutableStateOf<ItemQuantityCategorization?>(null)
                }
                var inventoryItemName by remember { mutableStateOf(emptyString) }
                var totalCostPrice by remember { mutableStateOf(emptyString) }
                var unitCostPrice by remember { mutableStateOf(emptyString) }

                //Create Inventory Item
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    AutoCompleteWithAddButton1(
                        value = inventoryItemName,
                        label = SelectInventoryItem,
                        listItems = inventoryItems,
                        placeholder = InventoryItemPlaceholder,
                        readOnly = true,
                        expandedIcon = R.drawable.ic_inventory,
                        unexpandedIcon = R.drawable.ic_inventory,
                        onClickAddButton = { createInventoryItem() },
                        getSelectedItem = { inventoryItemName = it }
                    )
                }

                // Item Quantity
                var expandItemQuantity by remember {
                    mutableStateOf(false)
                }
                Box(
                    modifier = Modifier
                        .padding(LocalSpacing.current.small)
                        .clickable { expandItemQuantity = !expandItemQuantity },
                    contentAlignment = Alignment.Center
                ) {
                    val numberOfUnits = itemQuantities?.unit.toNotNull()
                    val totalNumberOfUnits = itemQuantities?.totalNumberOfUnits.toNotNull()
                    val totalNumberOfPacks = itemQuantities?.totalNumberOfSize1.toNotNull()
                    val quantityValue = "$totalNumberOfPacks packs, $numberOfUnits units"
                    ItemQuantityCategorizationTextField(
                        value = "$quantityValue \nTotal units: $totalNumberOfUnits units",
                        onValueChange = {},
                        placeholder = InventoryItemQuantityPlaceholder,
                        label = AddInventoryQuantity,
                        readOnly = true,
                        onClickIcon = {
                            expandItemQuantity = !expandItemQuantity
                        },
                        icon = R.drawable.ic_quantity
                    )
                }
                AnimatedVisibility(
                    modifier = Modifier
                        .padding(LocalSpacing.current.small)
                        .background(MaterialTheme.colorScheme.surface),
                    visible = expandItemQuantity
                ) {
                    ItemQuantityCategorizationCard(
                        itemQuantityCategorization = itemQuantities,
                        discardChanges = { expandItemQuantity = false }
                    ) { _itemQuantityCategorization ->
                        itemQuantities = _itemQuantityCategorization
                        expandItemQuantity = false
                    }
                }

                // Unit cost price
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    var priceValueIsWrong by remember { mutableStateOf(false) }
                    BasicTextFieldWithTrailingIconError(
                        value = unitCostPrice,
                        onValueChange = {_amount->
                            unitCostPrice = _amount
                            priceValueIsWrong = amountIsNotValid(_amount)
                            if (!priceValueIsWrong){
                                val numberOfUnits = itemQuantities?.totalNumberOfUnits.toNotNull()
                                totalCostPrice = "${_amount.toDouble().times(numberOfUnits)}"
                            }
                        },
                        isError = priceValueIsWrong,
                        readOnly = false,
                        placeholder = CostPricePlaceholder,
                        label = EnterUnitCostPrice,
                        icon = R.drawable.ic_money_outline,
                        keyboardType = KeyboardType.Number
                    )
                }

                // Total cost price
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    var priceValueIsWrong by remember { mutableStateOf(false) }
                    BasicTextFieldWithTrailingIconError(
                        value = totalCostPrice,
                        onValueChange = {_amount->
                            totalCostPrice = _amount
                            priceValueIsWrong = amountIsNotValid(_amount)
                            if (!priceValueIsWrong){
                                val numberOfUnits = itemQuantities?.totalNumberOfUnits.toNotNull()
                                unitCostPrice = "${_amount.toDouble().div(numberOfUnits.toDouble())}"
                            }
                        },
                        isError = priceValueIsWrong,
                        readOnly = false,
                        placeholder = CostPricePlaceholder,
                        label = EnterTotalCostPrice,
                        icon = R.drawable.ic_money_outline,
                        keyboardType = KeyboardType.Number
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = LocalSpacing.current.smallMedium),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Discard
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomizeButton(
                            buttonName = Cancel,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            buttonHeight = LocalSpacing.current.topAppBarSize
                        ) {
                            openReceiptView = false
                        }
                    }

                    // Add
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomizeButton(
                            isLoading = false,
                            buttonName = AddItem,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            buttonHeight = LocalSpacing.current.topAppBarSize
                        ) {
                            if (inventoryItemName.isEmpty()) {
                                Toast.makeText(context, "Please add item route", Toast.LENGTH_LONG).show()
                            } else if (itemQuantities == null) {
                                Toast.makeText(context, "Please add the quantity of item selected", Toast.LENGTH_LONG).show()
                            } else if (unitCostPrice.isEmpty() || totalCostPrice.isEmpty()) {
                                Toast.makeText(context, "Please add the cost of item", Toast.LENGTH_LONG).show()
                            } else {
                                if (receiptDisplayItems.size > 20){
                                    addItemDisplayDialogMessage = "Cannot add more than 20 items to this receipt" +
                                            "\nCreate another receipt and add more items"
                                    openAddItemDisplayDialog = !openAddItemDisplayDialog
                                }else if (receiptDisplayItems.map { it.itemName }.contains(inventoryItemName)){
                                    addItemDisplayDialogMessage = "$inventoryItemName has already been added"
                                    openAddItemDisplayDialog = !openAddItemDisplayDialog
                                }else {
                                    itemQuantityInfo = ItemQuantityInfo(
                                        inventoryItemName,
                                        itemQuantities!!.totalNumberOfUnits.toDouble(),
                                        convertToDouble(unitCostPrice),
                                        convertToDouble(totalCostPrice)
                                    )
                                    getReceiptItems(receiptDisplayItems.plus(itemQuantityInfo!!))
                                    addItemDisplayDialogMessage = "$inventoryItemName is added to receipt list"
                                    openAddItemDisplayDialog = !openAddItemDisplayDialog
                                    openReceiptView = !openReceiptView
                                }
                            }
                        }
                    }
                }
            }
        }


        // Display Receipt Items
        Box(
            modifier = Modifier.padding(LocalSpacing.current.default),
            contentAlignment = Alignment.Center
        ) {
            ReceiptItemDisplayCard(
                currency = "GHS",
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
            BasicButton(buttonName = "Save Receipt") {
                confirmationPromptDialog = !confirmationPromptDialog
            }
        }

        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Remove Item",
            textContent = "Are you sure you want to remove this ${deleteReceiptItem?.itemName}?",
            unconfirmedDeletedToastText = "Did not delete item",
            confirmedDeleteToastText = "${deleteReceiptItem?.itemName} is removed",
            confirmDelete = {
                getReceiptItems(receiptDisplayItems.minus(deleteReceiptItem!!))
                addItemDisplayDialogMessage = "${deleteReceiptItem?.itemName} removed from receipt list"
                deleteConfirmationInfoDialog = !deleteConfirmationInfoDialog
            }) {
            openDeleteConfirmation = false
        }

        UpdateConfirmationDialog(
            openDialog = confirmationPromptDialog,
            title = "Confirm",
            textContent = "Are you sure you want to create this receipt?",
            unconfirmedUpdatedToastText = null,
            confirmedUpdatedToastText = null,
            confirmUpdate = {
                saveReceipt()
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
}

