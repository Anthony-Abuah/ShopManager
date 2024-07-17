package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt

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
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.AddInventoryQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.AddItem
import com.example.myshopmanagerapp.core.FormRelatedString.Cancel
import com.example.myshopmanagerapp.core.FormRelatedString.EnterTotalAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterUnitSellingPrice
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemQuantityPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PricePlaceholder
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun CreateAndAddReceipt(
    openReceiptView: Boolean,
    inventoryItems: List<InventoryItemEntity>,
    receiptDisplayItems: List<ItemQuantityInfo>,
    createInventoryItem: ()-> Unit,
    getItemDisplayDialogMessage: (String)-> Unit,
    openOrCloseItemDisplayDialog: ()-> Unit,
    getReceiptItems: (List<ItemQuantityInfo>)-> Unit,
    closeReceiptView: ()-> Unit,

    ) {
    val context = LocalContext.current

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
            var itemQuantities by remember { mutableStateOf(emptyList<ItemQuantity>()) }
            var inventoryItem by remember { mutableStateOf<InventoryItemEntity?>(null) }
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
                    label = FormRelatedString.SelectInventoryItem,
                    listItems = inventoryItems.map { it.inventoryItemName },
                    placeholder = FormRelatedString.InventoryItemPlaceholder,
                    readOnly = true,
                    expandedIcon = R.drawable.ic_inventory,
                    unexpandedIcon = R.drawable.ic_inventory,
                    onClickAddButton = { createInventoryItem() },
                    getSelectedItem = {
                        inventoryItemName = it
                        inventoryItem = inventoryItems.firstOrNull { item-> it == item.inventoryItemName }
                    }
                )
            }

            // Item Quantity
            var expandItemQuantity by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .padding(LocalSpacing.current.small)
                    .clickable { expandItemQuantity = !expandItemQuantity },
                contentAlignment = Alignment.Center
            ) {
                val numberOfUnits = itemQuantities.getTotalNumberOfUnits()
                ItemQuantityCategorizationTextField(
                    value = "Quantity: $numberOfUnits units",
                    onValueChange = {},
                    placeholder = InventoryItemQuantityPlaceholder,
                    label = AddInventoryQuantity,
                    readOnly = true,
                    expandQuantities = { expandItemQuantity = true },
                    onClickIcon = { expandItemQuantity = !expandItemQuantity },
                    icon = R.drawable.ic_quantity
                )
            }


            AnimatedVisibility(
                modifier = Modifier
                    .padding(LocalSpacing.current.small)
                    .background(MaterialTheme.colorScheme.surface),
                visible = expandItemQuantity
            ) {
                if (inventoryItem != null) {
                    QuantityCategorizationCard(
                        inventoryItem = inventoryItem!!,
                        itemQuantities = itemQuantities,
                        discardChanges = { expandItemQuantity = false },
                        getQuantities = {
                            itemQuantities = it
                            expandItemQuantity = false
                        }
                    )
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
                            val numberOfUnits = itemQuantities.getTotalNumberOfUnits().toNotNull()
                            totalCostPrice = "${_amount.toDouble().times(numberOfUnits)}"
                        }
                    },
                    isError = priceValueIsWrong,
                    readOnly = false,
                    placeholder = PricePlaceholder,
                    label = EnterUnitSellingPrice,
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
                            val numberOfUnits = itemQuantities.getTotalNumberOfUnits().toNotNull()
                            unitCostPrice = "${_amount.toDouble().div(numberOfUnits.toDouble())}"
                        }
                    },
                    isError = priceValueIsWrong,
                    readOnly = false,
                    placeholder = PricePlaceholder,
                    label = EnterTotalAmount,
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
                        closeReceiptView()
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
                        when(true){
                            (inventoryItemName.isEmpty())-> {
                                Toast.makeText(context, "Please add item name", Toast.LENGTH_LONG).show()
                            }
                            (unitCostPrice.isEmpty() || totalCostPrice.isEmpty()) ->{
                                Toast.makeText(context, "Please add the cost of item", Toast.LENGTH_LONG).show()
                            }
                            (receiptDisplayItems.size > 20)->{
                                getItemDisplayDialogMessage( "Cannot add more than 20 items to this receipt"
                                        + "\nCreate another receipt and add more items" )
                                openOrCloseItemDisplayDialog()
                            }
                            (itemQuantities.getTotalNumberOfUnits() < 1)->{
                                getItemDisplayDialogMessage( "Please add the quantity of $inventoryItemName you want to add" )
                                openOrCloseItemDisplayDialog()
                            }
                            (receiptDisplayItems.map { it.itemName }.contains(inventoryItemName))->{
                                getItemDisplayDialogMessage("$inventoryItemName has already been added")
                                openOrCloseItemDisplayDialog()
                            }
                            else->{
                                val itemQuantityInfo = ItemQuantityInfo(
                                    inventoryItemName,
                                    itemQuantities.getTotalNumberOfUnits().toDouble(),
                                    convertToDouble(unitCostPrice),
                                    convertToDouble(totalCostPrice)
                                )
                                getReceiptItems(receiptDisplayItems.plus(itemQuantityInfo))
                                getItemDisplayDialogMessage("$inventoryItemName is added to receipt list")
                                openOrCloseItemDisplayDialog()
                                closeReceiptView()
                            }
                        }
                    }
                }

            }
        }
    }
}
