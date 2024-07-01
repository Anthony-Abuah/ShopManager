package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.core.FormRelatedString.Cancel
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOf
import com.example.myshopmanagerapp.core.FormRelatedString.QuantityInSizeNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SaveQuantity
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.getItemQuantities
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantity
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun QuantityCategorizationCard(
    inventoryItem: InventoryItemEntity,
    itemQuantities: ItemQuantities,
    discardChanges: ()-> Unit,
    getQuantities: (ItemQuantities)-> Unit
){
    val thisItemQuantities = inventoryItem.quantityCategorizations.getItemQuantities(itemQuantities).sortedBy { it.unitsPerSize }
    var mutableItemQuantities = thisItemQuantities.toMutableList()
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(LocalSpacing.current.small),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.noElevation)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            mutableItemQuantities.forEach { itemQuantity ->
                if (itemQuantity.unitsPerSize == 1){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        var quantity by remember {
                            mutableStateOf(itemQuantity.quantity.toString())
                        }
                        ItemQuantityCategorizationTextField(
                            value = quantity,
                            onValueChange = {_quantity->
                                quantity = _quantity
                                val quantityValue = convertToDouble(quantity).toInt()
                                if(mutableItemQuantities.remove(itemQuantity)){
                                    mutableItemQuantities = mutableItemQuantities.plus(
                                        ItemQuantity(
                                            sizeName = itemQuantity.sizeName,
                                            quantity = quantityValue,
                                            unitsPerSize = itemQuantity.unitsPerSize
                                        )
                                    ).sortedBy { it.unitsPerSize }.toMutableList()
                                }
                            },
                            placeholder = QuantityInSizeNamePlaceholder,
                            label = EnterNumberOf.plus(itemQuantity.sizeName),
                            keyboardType = KeyboardType.Number
                        )
                    }
                }
                else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = LocalSpacing.current.extraSmall),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var quantity by remember {
                            mutableStateOf(itemQuantity.quantity.toString())
                        }
                        Box(
                            modifier = Modifier
                                .weight(1.5f)
                                .padding(LocalSpacing.current.borderStroke),
                            contentAlignment = Alignment.Center
                        ) {
                            ItemQuantityCategorizationTextField(
                                value = quantity,
                                onValueChange = { _quantity ->
                                    quantity = _quantity
                                    val quantityValue = convertToDouble(quantity).toInt()
                                    if (mutableItemQuantities.remove(itemQuantity)) {
                                        mutableItemQuantities = mutableItemQuantities.plus(
                                            ItemQuantity(
                                                sizeName = itemQuantity.sizeName,
                                                quantity = quantityValue,
                                                unitsPerSize = itemQuantity.unitsPerSize
                                            )
                                        ).sortedBy { it.unitsPerSize }.toMutableList()
                                    }
                                },
                                placeholder = QuantityInSizeNamePlaceholder,
                                label = EnterNumberOf.plus(itemQuantity.sizeName),
                                keyboardType = KeyboardType.Number
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(LocalSpacing.current.borderStroke),
                            contentAlignment = Alignment.Center
                        ) {
                            ItemQuantityCategorizationTextField(
                                value = itemQuantity.unitsPerSize.toString(),
                                onValueChange = { },
                                placeholder = QuantityInSizeNamePlaceholder,
                                readOnly = true,
                                label = "Number of units per ${itemQuantity.sizeName}",
                                keyboardType = KeyboardType.Number
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier
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
                        discardChanges()
                    }
                }

                // Save
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    CustomizeButton(
                        buttonName = SaveQuantity,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        buttonHeight = LocalSpacing.current.topAppBarSize
                    ) {
                        getQuantities(mutableItemQuantities.sortedBy { it.unitsPerSize })
                    }
                }
            }
        }
    }
}

