package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import java.util.*

@Composable
fun DebtCard(
    date: String,
    debtAmount: String,
    customerName: String,
    currency: String,
    open: () -> Unit,
){
    val debtBackground = if (isSystemInDarkTheme()) Red5 else Red99
    val secondaryColor = Grey50
    val debtColor = Red50
    val borderColor = if(isSystemInDarkTheme()) Red40 else Red60

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.default)
        .clickable { open() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.padding(LocalSpacing.current.small),
            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            border = BorderStroke(LocalSpacing.current.divider, MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(LocalSpacing.current.small),
                    painter = painterResource(id = R.drawable.debt),
                    contentDescription = emptyString
                )
            }
        }
        Spacer(modifier = Modifier.width(LocalSpacing.current.small))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.defaultMinSize(minWidth = 100.dp),
                text = customerName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

            Text(
                modifier = Modifier.defaultMinSize(minWidth = 100.dp),
                text = date,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = secondaryColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(LocalSpacing.current.small))

        Card(
            modifier = Modifier.padding(LocalSpacing.current.small),
            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.small),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = debtBackground),
            border = BorderStroke(color = borderColor, width = LocalSpacing.current.divider)
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    ),
                    text = "$currency $debtAmount",
                    fontWeight = FontWeight.ExtraBold,
                    color = debtColor,
                    fontSize = 10.sp
                )
            }
        }
    }

    /*
    Column(modifier = Modifier.fillMaxWidth()) {
        //if (!showAllItems) { HorizontalDivider() }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .background(Color.Transparent)
                .clickable { open() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = LocalSpacing.current.default)
                    .width(LocalSpacing.current.extraLarge)
                    .aspectRatio(1f),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(
                    containerColor = cardContainerColor
                ),
                elevation = CardDefaults.cardElevation(LocalSpacing.current.smallMedium)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.ic_money_outline),
                        contentDescription = emptyString,
                        tint = contentColor
                    )
                }
            }

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
                        text = "Customer: $customerName",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
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
                    val customerDate = if (showAllItems) "Date: $date" else "Last Debt date: $date"
                    Text(
                        text = customerDate,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = contentColor,

                        )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val amount = if (showAllItems) "Amount: $currency $debtAmount" else "Total Debt: $currency $debtAmount"
                    Text(
                        text = amount,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
                    Spacer(modifier = Modifier.height(LocalSpacing.current.default))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_options),
                        contentDescription = emptyString,
                        tint = contentColor
                    )
                    DropdownMenu(
                        modifier = Modifier
                            .width(150.dp),
                        expanded = expandOptionsDropDown,
                        onDismissRequest = { expandOptionsDropDown = false },
                        offset = pressOffset
                    ) {
                        dropDownOptions.forEach { value ->
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
                                        when (value) {
                                            Edit -> {
                                                edit()
                                            }
                                            Delete -> {
                                                delete()
                                            }
                                            show -> {
                                                showAll()
                                            }
                                            else -> {
                                                showAll()
                                            }
                                        }
                                        expandOptionsDropDown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = LocalSpacing.current.default),
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
        if (!showAllItems) { HorizontalDivider() }
    }
    */

}

