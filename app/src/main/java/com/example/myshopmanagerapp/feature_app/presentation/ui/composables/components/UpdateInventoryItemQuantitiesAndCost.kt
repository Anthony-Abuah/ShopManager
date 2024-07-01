package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.FormRelatedString.AddInventoryQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemQuantityPlaceholder
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun UpdateInventoryItemQuantitiesAndCost(
    closeInventoryDisplay: () -> Unit,
    inventory: InventoryEntity,
    inventoryItem: InventoryItemEntity,
    addInventoryQuantities: (ItemQuantities) -> Unit,
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.small)
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        var inventoryQuantities by remember {
            mutableStateOf<ItemQuantities>(emptyList())
        }
        val totalNumberOfUnits by remember {
            mutableStateOf( inventory.quantityInfo.getTotalNumberOfUnits() )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
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
                    placeholder = InventoryItemQuantityPlaceholder,
                    label = AddInventoryQuantity,
                    readOnly = true,
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
                QuantityCategorizationCard(
                    inventoryItem = inventoryItem,
                    itemQuantities = inventory.quantityInfo,
                    discardChanges = { expandItemQuantity = false },
                    getQuantities = {
                        inventoryQuantities = it
                        addInventoryQuantities(it)
                        expandItemQuantity = false
                        closeInventoryDisplay()
                    }
                )
            }
        }
    }


}