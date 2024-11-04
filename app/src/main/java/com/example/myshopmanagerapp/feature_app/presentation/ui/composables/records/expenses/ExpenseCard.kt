package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.Personnel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*

@Composable
fun ExpenseCard(
    expense: ExpenseEntity,
    currency: String,
    number: String,
    personnel: String,
    onDelete: () -> Unit,
    onOpenCard: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onSurface
    val cardContainerColor = MaterialTheme.colorScheme.surface

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

    val dropDownOptions = listOf("Edit", "Delete")

    Column(modifier = Modifier
        .fillMaxWidth()
        .border(
            width = LocalSpacing.current.divider,
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.onBackground
        )
        .padding(LocalSpacing.current.medium)
        .clickable { onOpenCard() }
    ) {
        val date = expense.date.toDateString()
        val dayOfWeek = expense.dayOfWeek?.lowercase(Locale.ROOT)
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            ?: Constants.NotAvailable
        val expenseName = expense.expenseName
        val expenseType = expense.expenseType
        val isSales = expenseName.lowercase().contains(Constants.Sales.lowercase().take(4))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.shapes.large
                )
                .padding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = LocalSpacing.current.default,
                    vertical = LocalSpacing.current.extraSmall
                ),
                text = date,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        Row(modifier = Modifier) {
            Card(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .size(40.dp)
                    .clip(CircleShape)
                    .padding(LocalSpacing.current.extraSmall),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(LocalSpacing.current.medium)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.expense),
                    contentDescription = emptyString
                )
            }

            Spacer(modifier = Modifier.width(LocalSpacing.current.small))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(modifier = Modifier.weight(1f)) {

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = expenseType.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                },
                                lineHeight = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        }


                        Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(end = LocalSpacing.current.extraSmall)
                                    .size(LocalSpacing.current.default),
                                imageVector = Icons.Default.Circle,
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.onBackground
                            )

                            Box(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = expenseName,
                                    lineHeight = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            }

                        }


                    }

                    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.large
                            )
                            .padding(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = LocalSpacing.current.default,
                                vertical = LocalSpacing.current.extraSmall
                            ),
                            text = dayOfWeek,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(LocalSpacing.current.default))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "$currency ${expense.expenseAmount}",
                                lineHeight = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        HorizontalDivider(
            thickness = LocalSpacing.current.divider,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = Personnel,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    modifier = Modifier
                        .padding(LocalSpacing.current.default)
                        .size(LocalSpacing.current.small),
                    imageVector = Icons.Default.Circle,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = personnel,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            
            Spacer(modifier = Modifier.width(LocalSpacing.current.small))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.large
                    )
                    .clip(CircleShape)
                    .padding(LocalSpacing.current.extraSmall)
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        

    }

    /*
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(Color.Transparent)
            .clickable { onOpenCard() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.default)
                .width(LocalSpacing.current.extraLarge)
                .fillMaxHeight(),
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
                    painter = painterResource(id = R.drawable.ic_expense),
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
                    text = expense.expenseName,
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
                Text(
                    text = "Date: ${expense.dayOfWeek}, ${expense.date.toDateString()}",
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
                Text(
                    text = "Exp. Type: ${expense.expenseType}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.extraSmall),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Amount: $currency ${expense.expenseAmount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(modifier = Modifier
            .width(LocalSpacing.current.medium)
            .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
                    contentDescription = emptyString,
                    tint = contentColor
                )
                DropdownMenu(modifier = Modifier
                    .width(150.dp),
                    expanded = expandOptionsDropDown,
                    onDismissRequest = { expandOptionsDropDown = false },
                    offset = pressOffset
                ) {
                    dropDownOptions.forEachIndexed{ index, value->
                        Box(modifier = Modifier
                            .height(LocalSpacing.current.dropDownItem),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = {
                                    Row(modifier = Modifier.fillMaxWidth(),
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
                                    if (index == 0){ onOpenCard() }else{ onDelete() }
                                    expandOptionsDropDown = false
                                }
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(text = number,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    color = contentColor,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
    */
}

