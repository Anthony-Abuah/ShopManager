package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.Delete
import com.example.myshopmanagerapp.core.Constants.editDelete
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.feature_app.domain.model.QuantityCategorization
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*


@Composable
fun CategoryCard(
    number: String,
    name: String,
    onClickItem: (String) -> Unit,
) {
    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var isContextMenuVisible by remember {
        mutableStateOf(false)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(4.5f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.default),
                    text = "$number.  ${name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }



            Box(modifier = Modifier
                .weight(0.5f)
                .onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
                .pointerInput(true) {
                    detectTapGestures(
                        onPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        }
                    )
                },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_options),
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                DropdownMenu(
                    modifier = Modifier
                        .padding(end = LocalSpacing.current.small),
                    expanded = isContextMenuVisible,
                    onDismissRequest = { isContextMenuVisible = false },
                    offset = pressOffset
                ) {
                    editDelete.forEach { item ->
                        Box(
                            modifier = Modifier
                                .height(LocalSpacing.current.dropDownItem),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = item,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onClickItem(item)
                                    isContextMenuVisible = false
                                }
                            )
                        }
                    }
                }
            }
        }


        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = LocalSpacing.current.small),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}


@Composable
fun QuantityCategorizationCard(
    name: String,
    quantity: String,
    onClickItem: (String, QuantityCategorization) -> Unit,
) {
    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var isContextMenuVisible by remember {
        mutableStateOf(false)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.small),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(4.5f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Size Category: ${name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(modifier = Modifier
                .weight(0.5f)
                .onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
                .pointerInput(true) {
                    detectTapGestures(
                        onPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        }
                    )
                },
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_options),
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                DropdownMenu(
                    modifier = Modifier
                        .padding(end = LocalSpacing.current.small),
                    expanded = isContextMenuVisible,
                    onDismissRequest = { isContextMenuVisible = false },
                    offset = pressOffset
                ) {
                    Box(
                        modifier = Modifier
                            .height(LocalSpacing.current.dropDownItem),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = Delete,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Normal
                                )
                            },
                            onClick = {
                                val unitPerThisSize = convertToDouble(quantity).toInt()
                                onClickItem(Delete, QuantityCategorization(name, unitPerThisSize))
                                isContextMenuVisible = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.small),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Units Per $name: $quantity unit(s)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = LocalSpacing.current.small),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}


