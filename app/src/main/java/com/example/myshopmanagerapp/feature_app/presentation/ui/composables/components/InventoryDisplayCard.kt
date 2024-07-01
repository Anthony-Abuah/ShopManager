package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.roundDouble
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun InventoryDisplayCard(
    currency: String = "GHS",
    inventoryCardDisplayValues: List<InventoryQuantityDisplayValues>,
    getInventoryValue: (InventoryQuantityDisplayValues?) -> Unit
){
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    // Main column
    Column(modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 350.dp)
        .horizontalScroll(horizontalScrollState)
        .border(
            LocalSpacing.current.borderStroke,
            MaterialTheme.colorScheme.onBackground
        ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(LocalSpacing.current.large)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Box(modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Unit Qty.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier
                .width(175.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Inventory Item Name",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Unit Cost",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Total Cost",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.errorContainer)
                .width(100.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Delete",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    LocalSpacing.current.borderStroke,
                    MaterialTheme.colorScheme.onBackground
                )
                .verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (inventoryCardDisplayValues.isEmpty()){
                Box(modifier  = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .requiredWidth((80 + 175 + 300).dp)
                    .height(LocalSpacing.current.large)
                    .border(
                        LocalSpacing.current.divider,
                        MaterialTheme.colorScheme.onBackground
                    ),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "No inventory has been added yet",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else inventoryCardDisplayValues.forEachIndexed{index, inventoryValue ->
                val totalUnits = inventoryValue.totalUnits
                val itemName = inventoryValue.inventoryItemEntity.inventoryItemName
                val unitCost = inventoryValue.unitCost
                val totalCost = inventoryValue.totalCost
                var selectedIndex by remember {
                    mutableStateOf<Int?>(null)
                }
                val isSelected = index == selectedIndex

                val backgroundColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background
                val contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground

                // Value Row
                Row(
                    modifier = Modifier
                        .background(backgroundColor)
                        .fillMaxWidth()
                        .height(LocalSpacing.current.large)
                        .clickable {
                            selectedIndex = index
                        }
                ) {
                    Box(modifier = Modifier
                        .width(80.dp)
                        .clickable {
                            selectedIndex = index
                        }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = totalUnits.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(modifier = Modifier
                        .width(175.dp)
                        .clickable {
                            selectedIndex = index
                        }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = LocalSpacing.current.small),
                            text = itemName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(modifier = Modifier
                        .width(100.dp)
                        .clickable { selectedIndex = index }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$currency $unitCost",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(modifier = Modifier
                        .width(100.dp)
                        .clickable {
                            selectedIndex = index
                        }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$currency $totalCost",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(modifier = Modifier
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .width(100.dp)
                        .clickable { getInventoryValue(inventoryValue) }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = emptyString,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }

                }
            }
        }
        val totalCost = inventoryCardDisplayValues.sumOf { it.totalCost }
        if (totalCost > 0.5) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .requiredWidth((80 + 175 + 300).dp)
                    .requiredHeight(LocalSpacing.current.large)
                    .border(
                        LocalSpacing.current.divider,
                        MaterialTheme.colorScheme.onBackground
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Total Cost: $currency $totalCost",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun InventoryDisplayCardOnList(
    currency: String = "GHS",
    inventoryCardDisplayValues: List<InventoryQuantityDisplayValues>,
    onPrintInventory: (List<InventoryQuantityDisplayValues>) -> Unit = {},
    getInventoryValue: (InventoryQuantityDisplayValues?) -> Unit,
    deleteInventoryValue: (InventoryQuantityDisplayValues?) -> Unit
){
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    // Main column
    Column(modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 350.dp)
        .horizontalScroll(horizontalScrollState)
        .border(
            LocalSpacing.current.borderStroke,
            MaterialTheme.colorScheme.onBackground
        ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(LocalSpacing.current.large)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Box(modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Unit Qty.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Inventory Item Name",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier
                .width(125.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Unit Cost",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Total Cost",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .width(100.dp)
                .fillMaxHeight()
                .clickable { onPrintInventory(inventoryCardDisplayValues) }
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.width(LocalSpacing.current.extraSmall))
                Text(
                    text = "Print",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    LocalSpacing.current.borderStroke,
                    MaterialTheme.colorScheme.onBackground
                )
                .verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (inventoryCardDisplayValues.isEmpty()){
                Box(modifier  = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .requiredWidth((100 + 200 + 125 + 150 + 100).dp)
                    .height(LocalSpacing.current.large)
                    .border(
                        LocalSpacing.current.divider,
                        MaterialTheme.colorScheme.onBackground
                    ),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "No inventory has been added yet",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else inventoryCardDisplayValues.forEachIndexed{index, inventoryValue ->
                val totalUnits = inventoryValue.totalUnits
                val itemName = inventoryValue.inventoryItemEntity.inventoryItemName
                val unitCost = roundDouble(inventoryValue.unitCost)
                val totalCost = roundDouble(inventoryValue.totalCost)
                var selectedIndex by remember {
                    mutableStateOf<Int?>(null)
                }
                val isSelected = index == selectedIndex

                val backgroundColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background
                val contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground

                // Value Row
                Row(
                    modifier = Modifier
                        .background(backgroundColor)
                        .fillMaxWidth()
                        .height(LocalSpacing.current.large)
                        .clickable {
                            getInventoryValue(inventoryValue)
                            selectedIndex = index
                        }
                ) {
                    Box(modifier = Modifier
                        .width(100.dp)
                        .clickable {
                            getInventoryValue(inventoryValue)
                            selectedIndex = index
                        }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = totalUnits.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(modifier = Modifier
                        .width(200.dp)
                        .clickable {
                            getInventoryValue(inventoryValue)
                            selectedIndex = index
                        }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = LocalSpacing.current.small),
                            text = itemName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(modifier = Modifier
                        .width(125.dp)
                        .clickable {
                            getInventoryValue(inventoryValue)
                            selectedIndex = index
                        }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$currency $unitCost",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(modifier = Modifier
                        .width(150.dp)
                        .clickable {
                            getInventoryValue(inventoryValue)
                            selectedIndex = index
                        }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$currency $totalCost",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(modifier = Modifier
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .width(100.dp)
                        .clickable { deleteInventoryValue(inventoryValue) }
                        .fillMaxHeight()
                        .border(
                            LocalSpacing.current.divider,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = emptyString,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }

                }
            }
        }
        val totalCost = inventoryCardDisplayValues.sumOf { it.totalCost }
        if (totalCost > 0.5) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .requiredWidth((100 + 200 + 125 + 150 + 100).dp)
                    .requiredHeight(LocalSpacing.current.large)
                    .border(
                        LocalSpacing.current.divider,
                        MaterialTheme.colorScheme.onBackground
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Total Cost: $currency $totalCost",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



