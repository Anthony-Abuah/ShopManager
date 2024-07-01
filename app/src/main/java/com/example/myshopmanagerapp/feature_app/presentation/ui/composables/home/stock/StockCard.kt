package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun StockCard(
    stock: StockEntity,
    number: String,
    showAllItems: Boolean = false,
    open: () -> Unit = {},
    edit: () -> Unit,
    delete: ()-> Unit,
    showAll: ()-> Unit,
    inventoryItemName: String,
){
    val contentColor = MaterialTheme.colorScheme.onSurface
    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var expandOptionsDropDown by remember {
        mutableStateOf(false)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val show = if (showAllItems) "Collapse All" else "Show All"
    val dropDownOptions = listOf("Edit", "Delete", show)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable { open() }
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Item route: $inventoryItemName",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Date: ${stock.dayOfWeek}, ${stock.date.toDateString()}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = contentColor
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val isInventory =
                        if (stock.isInventoryStock) "Inventory added" else "Stock taken"
                    Text(
                        text = isInventory,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = contentColor
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val suffix = if (stock.isInventoryStock) "added" else "remaining"
                    Text(
                        text = "Number of units $suffix: ${stock.totalNumberOfUnits} units",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor
                    )
                }
            }
            Column(
                modifier = Modifier
                    .width(LocalSpacing.current.medium)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier
                    .weight(1f)
                    .onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
                    .pointerInput(true) {
                        detectTapGestures(
                            onPress = {
                                expandOptionsDropDown = true
                                pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            }
                        )
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_options),
                        contentDescription = Constants.emptyString,
                        tint = contentColor
                    )
                    DropdownMenu(
                        modifier = Modifier
                            .width(150.dp),
                        expanded = expandOptionsDropDown,
                        onDismissRequest = { expandOptionsDropDown = false },
                        offset = pressOffset
                    ) {
                        dropDownOptions.forEachIndexed { index, value ->
                            Box(
                                modifier = Modifier
                                    .height(LocalSpacing.current.dropDownItem),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                DropdownMenuItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = value,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                    },
                                    onClick = {
                                        when(index){
                                            0->{ edit() }
                                            1->{ delete() }
                                            else->{ showAll() }
                                        }
                                        expandOptionsDropDown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = number,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        color = contentColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        HorizontalDivider()
    }
}
