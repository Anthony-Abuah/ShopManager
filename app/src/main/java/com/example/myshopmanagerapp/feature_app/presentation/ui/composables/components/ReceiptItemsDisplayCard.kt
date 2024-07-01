package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ReceiptItemDisplayCard(
    currency: String,
    receiptItems: List<ItemQuantityInfo>,
    getInventoryValue: (ItemQuantityInfo?) -> Unit
){
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    val fullWidth = (100 + 200 + 400).dp
    // Main column
    Column(modifier = Modifier
        .width(fullWidth)
        .wrapContentHeight()
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
                .width(fullWidth)
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
                .width(150.dp)
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
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.errorContainer)
                .width(100.dp)
                .fillMaxHeight()
                .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Remove",
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
                .width(fullWidth)
                .heightIn(max = 350.dp)
                .border(
                    LocalSpacing.current.borderStroke,
                    MaterialTheme.colorScheme.onBackground
                )
                .verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (receiptItems.isEmpty()){
                Box(modifier  = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .requiredWidth(fullWidth)
                    .height(LocalSpacing.current.large)
                    .border(
                        LocalSpacing.current.divider,
                        MaterialTheme.colorScheme.onBackground
                    ),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "No items have been added yet",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else receiptItems.forEachIndexed{ index, receiptItem ->
                val totalUnits = receiptItem.quantity
                val itemName = receiptItem.itemName
                val unitCost = receiptItem.unitPrice.toTwoDecimalPlaces()
                val totalCost = receiptItem.amount.toTwoDecimalPlaces()
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
                        .requiredWidth(fullWidth)
                        .height(LocalSpacing.current.large)
                        .clickable {
                            selectedIndex = index
                        }
                ) {
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
                        .width(150.dp)
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
                        .width(150.dp)
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

                    // Delete Receipt item selected
                    Box(modifier = Modifier
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .width(100.dp)
                        .clickable { getInventoryValue(receiptItem) }
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

        val totalCost = receiptItems.sumOf { it.amount }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .requiredWidth((80 + 175 + 400).dp)
                .requiredHeight(LocalSpacing.current.large)
                .border(
                    LocalSpacing.current.divider,
                    MaterialTheme.colorScheme.onBackground
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(LocalSpacing.current.small),
                text = "Total Cost: $currency $totalCost",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ReceiptItemsDisplayCardOnList(
    currency: String,
    receiptId: String,
    dayOfWeek: String,
    dateString: String,
    customerName: String,
    customerContact: String,
    receiptItems: List<ItemQuantityInfo>,
    saveAsPDF: () -> Unit,
){
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val fullWidth = (100 + 200 + 300).dp
    Card(
        modifier = Modifier.wrapContentHeight()
            .background(backgroundColor),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.default)
    ) {
        // Main column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalScroll(horizontalScrollState)
                .border(
                    LocalSpacing.current.borderStroke,
                    MaterialTheme.colorScheme.onBackground,
                    MaterialTheme.shapes.small
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Invoice Id
            Row(
                modifier = Modifier
                    .requiredWidth(fullWidth)
                    .requiredHeight(LocalSpacing.current.large)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(fullWidth)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Receipt Id: $receiptId",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
            // Customer info row
            Row(
                modifier = Modifier
                    .requiredWidth(fullWidth)
                    .requiredHeight(LocalSpacing.current.iconHeight)
                    .background(backgroundColor)
                    .border(
                        LocalSpacing.current.borderStroke,
                        contentColor
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .requiredWidth(450.dp)
                        .wrapContentHeight()
                        .background(backgroundColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalSpacing.current.small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(LocalSpacing.current.small),
                            text = "Date: $dayOfWeek, $dateString",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalSpacing.current.small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(LocalSpacing.current.small),
                            text = "Customer: $customerName",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalSpacing.current.small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(LocalSpacing.current.small),
                            text = "Customer Contact: $customerContact",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .requiredWidth(150.dp)
                        .requiredHeight(LocalSpacing.current.iconHeight)
                        .background(backgroundColor)
                        .border(
                            LocalSpacing.current.borderStroke,
                            contentColor
                        )
                        .clickable { saveAsPDF() },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pdf),
                            contentDescription = emptyString,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(LocalSpacing.current.small),
                            text = "Save as PDF",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }

            }

            // Header row
            Row(
                modifier = Modifier
                    .requiredWidth(fullWidth)
                    .requiredHeight(LocalSpacing.current.large)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier
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
                Box(
                    modifier = Modifier
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
                Box(
                    modifier = Modifier
                        .width(150.dp)
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
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .fillMaxHeight()
                        .border(LocalSpacing.current.divider, MaterialTheme.colorScheme.onPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
                    .border(
                        LocalSpacing.current.borderStroke,
                        contentColor
                    )
                    .verticalScroll(verticalScrollState),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                receiptItems.forEachIndexed { index, receiptItem ->
                    val totalUnits = receiptItem.quantity
                    val itemName = receiptItem.itemName
                    val unitCost = receiptItem.unitPrice.toTwoDecimalPlaces()
                    val totalCost = receiptItem.amount.toTwoDecimalPlaces()
                    var selectedIndex by remember {
                        mutableStateOf<Int?>(null)
                    }

                    // Value Row
                    Row(
                        modifier = Modifier
                            .background(backgroundColor)
                            .requiredWidth(fullWidth)
                            .height(LocalSpacing.current.large)
                            .clickable {
                                selectedIndex = index
                            }
                    ) {
                        Box(modifier = Modifier
                            .width(100.dp)
                            .clickable { selectedIndex = index }
                            .fillMaxHeight()
                            .border(
                                LocalSpacing.current.divider,
                                contentColor
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
                                selectedIndex = index
                            }
                            .fillMaxHeight()
                            .border(
                                LocalSpacing.current.divider,
                                contentColor
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
                            .requiredWidth(150.dp)
                            .clickable { selectedIndex = index }
                            .fillMaxHeight()
                            .border(
                                LocalSpacing.current.divider,
                                contentColor
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
                            .requiredWidth(150.dp)
                            .clickable {
                                selectedIndex = index
                            }
                            .fillMaxHeight()
                            .border(
                                LocalSpacing.current.divider,
                                contentColor
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
                    }
                }
            }

            val totalCost = receiptItems.sumOf { it.amount }
            Box(
                modifier = Modifier
                    .background(backgroundColor)
                    .requiredWidth(fullWidth)
                    .requiredHeight(LocalSpacing.current.large)
                    .border(
                        LocalSpacing.current.divider,
                        contentColor
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    text = "Total Cost: $currency $totalCost",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}





