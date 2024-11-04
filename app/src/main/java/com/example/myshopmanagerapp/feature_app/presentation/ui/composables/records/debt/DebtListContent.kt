package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.OutstandingDebt
import com.example.myshopmanagerapp.core.Constants.TotalDebt
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.DebtEntities
import com.example.myshopmanagerapp.core.DebtRepaymentEntities
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toDoubleDecimalPlaces
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import java.util.*


@Composable
fun DebtListContent(
    allDebts: DebtEntities,
    allDebtRepayments: DebtRepaymentEntities,
    selectedPeriod: String,
    onScrollStateChange: (Boolean) -> Unit,
    getCustomerName: (String) -> String,
    navigateToViewDebtScreen: (String) -> Unit
) {
    val context = LocalContext.current
    val currency =
        UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value.toNotNull().ifBlank { GHS }
    var openOutstandingDebtInfo by remember {
        mutableStateOf(false)
    }
    var isOutstandingDebt by remember {
        mutableStateOf(false)
    }


    val cardContainer = if (isSystemInDarkTheme()) Red80 else Red30
    val onCardContainer = if (isSystemInDarkTheme()) Red10 else Red90

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(LocalSpacing.current.medium),
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier.padding(LocalSpacing.current.noPadding),
            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = cardContainer),
        ) {
            val debtAmount = allDebts.sumOf { it.debtAmount }
            val debtRepaymentAmount = allDebtRepayments.sumOf { it.debtRepaymentAmount }
            val outstandingDebt = debtAmount.minus(debtRepaymentAmount)
            val debtDisplay =
                if (isOutstandingDebt) {"$currency ${if (outstandingDebt == 0.0) "0.00" else outstandingDebt.toDoubleDecimalPlaces()}"}
                else {"$currency ${if (debtAmount == 0.0) "0.00" else debtAmount.toDoubleDecimalPlaces()}"}
            Column(modifier = Modifier.padding(LocalSpacing.current.default)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isOutstandingDebt) OutstandingDebt else TotalDebt,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = onCardContainer
                        )

                        Spacer(modifier = Modifier.width(LocalSpacing.current.small))

                        if (isOutstandingDebt) {
                            Icon(
                                modifier = Modifier
                                    .size(LocalSpacing.current.medium)
                                    .clickable {
                                        openOutstandingDebtInfo = !openOutstandingDebtInfo
                                    },
                                imageVector = Icons.Default.Info,
                                contentDescription = emptyString,
                                tint = onCardContainer
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .padding(LocalSpacing.current.small)
                            .clickable { isOutstandingDebt = !isOutstandingDebt },
                        elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = cardContainer),
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(LocalSpacing.current.small)
                                .size(24.dp),
                            painter = painterResource(id = R.drawable.ic_swap),
                            tint = onCardContainer,
                            contentDescription = emptyString
                        )
                    }
                }

                Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

                Column(modifier = Modifier, horizontalAlignment = Alignment.Start) {

                    if (isOutstandingDebt) {
                        val thisDebtAmount =
                            if (debtAmount == 0.0) "0.00" else debtAmount.toDoubleDecimalPlaces()
                        val thisDebtRepaymentAmount =
                            if (debtRepaymentAmount == 0.0) "0.00" else debtAmount.toDoubleDecimalPlaces()
                        Text(
                            text = "($currency $thisDebtAmount - $currency $thisDebtRepaymentAmount)",
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = onCardContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))
                    }
                    Text(
                        text = debtDisplay,
                        fontFamily = robotoBold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Red5 else Color.White
                    )
                }

                HorizontalDivider(
                    color = onCardContainer,
                    modifier = Modifier.padding(vertical = LocalSpacing.current.default),
                    thickness = LocalSpacing.current.divider
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_time_filled),
                        tint = onCardContainer,
                        contentDescription = emptyString
                    )

                    Spacer(modifier = Modifier.width(LocalSpacing.current.default))

                    Text(
                        text = selectedPeriod,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = onCardContainer
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Text(
            text = "Customers with debt",
            fontFamily = robotoBold,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        Card(
            modifier = Modifier.padding(LocalSpacing.current.noPadding),
            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        ) {
            if (allDebts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No debts to show!",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else {
                val scrollState = rememberScrollState()
                val isScrolling by remember {
                    derivedStateOf { scrollState.isScrollInProgress }
                }
                onScrollStateChange(isScrolling)
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .verticalScroll(scrollState)
                ) {
                    allDebts.forEachIndexed { index, debt ->
                        val dateString = debt.date.toDate().toLocalDate().toDateString()
                        val dayOfWeek = debt.dayOfWeek?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                        }
                        val customerName = getCustomerName(debt.uniqueCustomerId)
                        if (index != 0) HorizontalDivider(
                            color = BlueGrey50,
                            thickness = LocalSpacing.current.divider
                        )
                        DebtCard(
                            date = "$dayOfWeek, $dateString",
                            debtAmount = debt.debtAmount.toDoubleDecimalPlaces(),
                            customerName = customerName,
                            currency = currency
                        ) {
                            navigateToViewDebtScreen(debt.uniqueDebtId)
                        }
                    }
                }
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = openOutstandingDebtInfo,
        isLoading = false,
        title = null,
        textContent = "The outstanding debt is calculated by subtracting the debt that has been paid from the debt amount owed",
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        openOutstandingDebtInfo = false
    }
}
