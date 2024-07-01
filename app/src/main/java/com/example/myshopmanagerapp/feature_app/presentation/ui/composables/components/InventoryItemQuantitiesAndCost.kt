package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

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
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.Functions
import com.example.myshopmanagerapp.core.Functions.generateUniqueInventoryId
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun InventoryItemQuantitiesAndCost(
    closeInventoryDisplay: () -> Unit,
    inventory: InventoryEntity,
    thisInventoryItem: InventoryItemEntity?,
    mapOfInventoryItems: Map<String, String>,
    addNewInventoryItem: () -> Unit,
    addInventoryItemId: (String) -> Unit,
    getInventoryItem: (String) -> InventoryItemEntity?,
    addCostPrices: (String, String) -> Unit,
    addInventoryEntity: (InventoryEntity) -> Unit,
    addInventoryQuantities: (ItemQuantities) -> Unit,
    openConfirmationDialog: () -> Unit,
    getInventoryQuantityDisplayValues: (InventoryQuantityDisplayValues?) -> Unit,
){
    val context = LocalContext.current
    var inventoryItem by remember {
        mutableStateOf(thisInventoryItem)
    }
    Column(modifier = Modifier.fillMaxWidth()
        .padding(LocalSpacing.current.small)
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        var inventoryQuantities by remember {
            mutableStateOf<ItemQuantities>(emptyList())
        }
        var totalNumberOfUnits by remember {
            mutableStateOf( inventory.quantityInfo.getTotalNumberOfUnits() )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            //Create Inventory Item
            Box(
                modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                var inventoryItemName by remember {
                    mutableStateOf(inventoryItem?.inventoryItemName ?: Constants.emptyString)
                }
                AutoCompleteWithAddButton1(
                    value = inventoryItemName,
                    label = FormRelatedString.SelectInventoryItem,
                    listItems = mapOfInventoryItems.keys.toList(),
                    placeholder = FormRelatedString.InventoryItemPlaceholder,
                    readOnly = false,
                    expandedIcon = R.drawable.ic_inventory,
                    unexpandedIcon = R.drawable.ic_inventory,
                    onClickAddButton = { addNewInventoryItem() },
                    getSelectedItem = {
                        inventoryItemName = it
                        addInventoryItemId(mapOfInventoryItems[inventoryItemName].toNotNull())
                        inventoryItem = getInventoryItem(mapOfInventoryItems[inventoryItemName].toNotNull())
                    }
                )
            }

            var expandItemQuantity by remember {
                mutableStateOf(false)
            }
            // Item Quantity
            Box(
                modifier = Modifier
                    .padding(LocalSpacing.current.small)
                    .clickable {
                        expandItemQuantity = !expandItemQuantity
                    },
                contentAlignment = Alignment.Center
            ) {
                ItemQuantityCategorizationTextField(
                    value = "Total units: $totalNumberOfUnits units",
                    onValueChange = {},
                    placeholder = FormRelatedString.InventoryItemQuantityPlaceholder,
                    label = FormRelatedString.AddInventoryQuantity,
                    readOnly = true,
                    expandQuantities = { expandItemQuantity = true },
                    onClickIcon = {
                        if (inventoryItem == null){
                            Toast.makeText(context, "You have not selected an inventory item", Toast.LENGTH_LONG).show()
                        }
                        else { expandItemQuantity = !expandItemQuantity }
                    },
                    icon = R.drawable.ic_quantity
                )
            }

            if (inventoryItem != null) {
                AnimatedVisibility(
                    modifier = Modifier
                        .padding(LocalSpacing.current.small)
                        .background(MaterialTheme.colorScheme.surface),
                    visible = expandItemQuantity
                ) {
                    QuantityCategorizationCard(
                        inventoryItem = inventoryItem!!,
                        itemQuantities = inventory.quantityInfo,
                        discardChanges = { expandItemQuantity = false },
                        getQuantities = {
                            inventoryQuantities = it
                            addInventoryQuantities(it)
                            expandItemQuantity = false
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.smallMedium),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var unitCostPrice by remember { mutableStateOf(inventory.unitCostPrice.toString()) }
                var totalCostPrice by remember { mutableStateOf(inventory.totalCostPrice.toString()) }
                totalNumberOfUnits = inventory.quantityInfo.getTotalNumberOfUnits()

                // Unit cost price
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextFieldWithTrailingIcon(
                        value = unitCostPrice,
                        onValueChange = {
                            unitCostPrice = it
                            totalCostPrice = totalNumberOfUnits.times(Functions.convertToDouble(unitCostPrice)).toString()
                            addCostPrices(unitCostPrice, totalCostPrice)
                        },
                        placeholder = FormRelatedString.InventoryTotalCostPricePlaceholder,
                        label = FormRelatedString.EnterInventoryUnitCostPrice,
                        icon = R.drawable.ic_money_outline,
                        keyboardType = KeyboardType.Number
                    )
                }

                // Total cost price
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    totalCostPrice = inventory.totalCostPrice.toString()
                    BasicTextFieldWithTrailingIcon(
                        value = totalCostPrice,
                        onValueChange = {
                            totalCostPrice = it
                            unitCostPrice = Functions.convertToDouble(totalCostPrice).div(totalNumberOfUnits).toString()
                            addCostPrices(unitCostPrice, totalCostPrice)
                        },
                        placeholder = FormRelatedString.InventoryTotalCostPricePlaceholder,
                        label = FormRelatedString.EnterInventoryTotalCostPrice,
                        icon = R.drawable.ic_money_outline,
                        keyboardType = KeyboardType.Number
                    )
                }

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
                        buttonName = FormRelatedString.Cancel,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        buttonHeight = LocalSpacing.current.topAppBarSize
                    ) {
                        closeInventoryDisplay()
                    }
                }

                // Save
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    BasicButton(
                        buttonName = FormRelatedString.Save,
                    ) {
                        when(true){
                            (inventory.totalNumberOfUnits < 1)->{
                                Toast.makeText(context, "Add item quantity", Toast.LENGTH_LONG).show()
                            }
                            (inventory.unitCostPrice < 1)->{
                                Toast.makeText(context, "Add item cost price", Toast.LENGTH_LONG).show()
                            }
                            (inventory.totalCostPrice < 1)->{
                                Toast.makeText(context, "Add item cost price", Toast.LENGTH_LONG).show()
                            }
                            else->{
                                val uniqueInventoryId = generateUniqueInventoryId("${inventoryItem?.inventoryItemName.toNotNull()}-${inventory.date.toDateString()}")
                                val inventoryEntity = inventory.copy(
                                    uniqueInventoryId = uniqueInventoryId
                                )
                                addInventoryEntity(inventoryEntity)
                                val inventoryQuantityDisplayValues = inventoryItem?.let {
                                    InventoryQuantityDisplayValues(
                                        inventoryItemEntity = it,
                                        totalUnits = inventory.totalNumberOfUnits,
                                        totalCost = inventory.totalCostPrice,
                                        unitCost = inventory.unitCostPrice
                                    )
                                }
                                getInventoryQuantityDisplayValues(inventoryQuantityDisplayValues)

                                openConfirmationDialog()

                                closeInventoryDisplay()
                            }
                        }
                    }
                }
            }
        }
    }


}