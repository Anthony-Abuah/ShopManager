package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity

@Composable
fun BankAccountCard(
    bankAccount: BankAccountEntity,
    number: String,
    currency: String,
    onDelete: () -> Unit,
    onOpenBankAccount: () -> Unit
){
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
            .clickable { onOpenBankAccount() },
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
                    painter = painterResource(id = R.drawable.ic_bank_account),
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
                    text = "Acc. Name: ${bankAccount.bankAccountName}",
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
                    text = "Bank: ${bankAccount.bankName}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
                    text = "Contact: ${bankAccount.bankContact}",
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
                    text = "Savings: $currency ${bankAccount.accountBalance.toNotNull()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (bankAccount.accountBalance.toNotNull() >= 0.0)contentColor else MaterialTheme.colorScheme.error,
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
                                    if (index == 0){ onOpenBankAccount() }else{ onDelete() }
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
}

