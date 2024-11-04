package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.DebtEntities
import com.example.myshopmanagerapp.core.DebtRepaymentEntities
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.DebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.DebtAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.DebtCustomer
import com.example.myshopmanagerapp.core.FormRelatedString.DebtInformation
import com.example.myshopmanagerapp.core.FormRelatedString.DebtShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterDebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.Personnel_Name
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.TotalDebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalDebtRepaymentAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalOutstandingDebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueDebtId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toDoubleDecimalPlaces
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.InfoDisplayCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.VerticalDisplayAndEditTextValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import java.util.*


@Composable
fun ViewDebtContent(
    allDebts: DebtEntities,
    allDebtRepayments: DebtRepaymentEntities,
    debt: DebtEntity,
    currency: String,
    isUpdatingDebt: Boolean,
    debtUpdatingIsSuccessful: Boolean,
    debtUpdateMessage: String?,
    customerName: String,
    personnelName: String,
    getUpdatedDebtDate: (String) -> Unit,
    getUpdatedDebtAmount: (String) -> Unit,
    getUpdatedDebtShortNotes: (String) -> Unit,
    updateDebt: (DebtEntity) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    val cardContainer = if (isSystemInDarkTheme()) Red5 else Red99
    val onCardContainer1 = if (isSystemInDarkTheme()) Red50 else Red40
    val onCardContainer2 = if (isSystemInDarkTheme()) Red99 else Red5
    val secondaryTextColor = Grey50

    BasicScreenColumnWithoutBottomBar {

        HorizontalDivider(
            modifier = Modifier.padding(bottom = LocalSpacing.current.small),
            thickness = LocalSpacing.current.borderStroke
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.small))

        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = LocalSpacing.current.medium)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(LocalSpacing.current.default))

            // Debt History/Info Boxes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val totalDebtAmount = allDebts.sumOf { it.debtAmount }
                val totalDebtRepaymentAmount =
                    allDebtRepayments.sumOf { it.debtRepaymentAmount }
                val recentDebtAmount = allDebts.maxByOrNull { it.date }?.debtAmount.toNotNull()
                val outstandingDebt = totalDebtAmount.minus(totalDebtRepaymentAmount)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    val displayDebtAmount =
                        if (totalDebtAmount == 0.0) "0.00" else totalDebtAmount.toDoubleDecimalPlaces()

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                LocalSpacing.current.divider,
                                onCardContainer1,
                                MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        InfoDisplayCard(
                            image = R.drawable.debt,
                            imageWidth = 32.dp,
                            bigText = "$currency $displayDebtAmount",
                            bigTextSize = 16.sp,
                            bigTextColor = onCardContainer1,
                            smallText = TotalDebtAmount,
                            smallTextSize = 10.sp,
                            smallTextColor = onCardContainer2,
                            backgroundColor = cardContainer,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }

                    Spacer(modifier = Modifier.height(LocalSpacing.current.default))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                LocalSpacing.current.divider,
                                onCardContainer1,
                                MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val displayDebtRepaymentAmount =
                            if (totalDebtRepaymentAmount == 0.0) "0.00" else totalDebtRepaymentAmount.toDoubleDecimalPlaces()
                        InfoDisplayCard(
                            image = R.drawable.debt_payment,
                            imageWidth = 32.dp,
                            bigText = "$currency $displayDebtRepaymentAmount",
                            bigTextSize = 16.sp,
                            bigTextColor = onCardContainer1,
                            smallText = TotalDebtRepaymentAmount,
                            smallTextSize = 10.sp,
                            smallTextColor = onCardContainer2,
                            backgroundColor = cardContainer,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }

                Spacer(modifier = Modifier.width(LocalSpacing.current.default))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                LocalSpacing.current.divider,
                                onCardContainer1,
                                MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val displayRecentDebtAmount =
                            if (recentDebtAmount == 0.0) "0.00" else recentDebtAmount.toDoubleDecimalPlaces()
                        InfoDisplayCard(
                            image = R.drawable.debt,
                            imageWidth = 32.dp,
                            bigText = "$currency $displayRecentDebtAmount",
                            bigTextSize = 16.sp,
                            bigTextColor = onCardContainer1,
                            smallText = "Most recent debt amount",
                            smallTextSize = 10.sp,
                            smallTextColor = onCardContainer2,
                            backgroundColor = cardContainer,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }

                    Spacer(modifier = Modifier.height(LocalSpacing.current.default))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                LocalSpacing.current.divider,
                                onCardContainer1,
                                MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val displayOutstandingDebtAmount =
                            if (outstandingDebt == 0.0) "0.00" else outstandingDebt.toDoubleDecimalPlaces()
                        InfoDisplayCard(
                            image = R.drawable.debt,
                            imageWidth = 32.dp,
                            bigText = "$currency $displayOutstandingDebtAmount",
                            bigTextSize = 16.sp,
                            bigTextColor = onCardContainer1,
                            smallText = TotalOutstandingDebtAmount,
                            smallTextSize = 10.sp,
                            smallTextColor = onCardContainer2,
                            backgroundColor = cardContainer,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = LocalSpacing.current.small),
            thickness = LocalSpacing.current.borderStroke
        )


        // This Customer's debt history text
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(start = LocalSpacing.current.medium)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(vertical = LocalSpacing.current.default),
                text = "${customerName.toEllipses(25)}'s debt history",
                fontFamily = robotoBold,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // This Customer's debt history
        Card(
            modifier = Modifier.padding(horizontal = LocalSpacing.current.medium),
            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                allDebts.forEachIndexed { index, debt ->
                    val dateString = debt.date.toDate().toLocalDate().toDateString()
                    val dayOfWeek = debt.dayOfWeek?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                    }
                    if (index != 0) HorizontalDivider(
                        color = BlueGrey50,
                        thickness = LocalSpacing.current.divider
                    )
                    DebtCard(
                        date = "$dayOfWeek, $dateString",
                        debtAmount = debt.debtAmount.toDoubleDecimalPlaces(),
                        customerName = customerName,
                        currency = currency
                    ) { }
                }
            }
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        HorizontalDivider(
            modifier = Modifier.padding(bottom = LocalSpacing.current.small),
            thickness = LocalSpacing.current.borderStroke
        )

        // Debt Information
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(start = LocalSpacing.current.medium)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(vertical = LocalSpacing.current.default),
                text = DebtInformation,
                fontFamily = robotoBold,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // This debt Info
        Column(modifier = Modifier
            .padding(horizontal = LocalSpacing.current.medium)
            .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.debt,
                    firstText = UniqueDebtId,
                    firstTextSize = 14.sp,
                    firstTextFontWeight = FontWeight.SemiBold,
                    firstTextColor = secondaryTextColor,
                    secondText = debt.uniqueDebtId,
                    secondTextSize = 14.sp,
                    secondTextFontWeight = FontWeight.Bold,
                    secondTextColor = MaterialTheme.colorScheme.onSurface,
                    readOnly = true
                )
            }

            // Date
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.date,
                    firstText = Date,
                    firstTextSize = 14.sp,
                    firstTextFontWeight = FontWeight.SemiBold,
                    firstTextColor = secondaryTextColor,
                    secondText = "${debt.dayOfWeek}, ${debt.date.toDate().toDateString()}",
                    secondTextSize = 14.sp,
                    secondTextFontWeight = FontWeight.Bold,
                    secondTextColor = MaterialTheme.colorScheme.onSurface,
                    readOnly = false,
                    isDate = true,
                    value = "${debt.dayOfWeek}, ${debt.date.toDate().toDateString()}",
                    getUpdatedValue = { getUpdatedDebtDate(it) }
                )
            }

            // Day Of The Week
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.ic_day,
                    firstText = DayOfTheWeek,
                    firstTextSize = 14.sp,
                    firstTextFontWeight = FontWeight.SemiBold,
                    firstTextColor = secondaryTextColor,
                    secondText = debt.dayOfWeek.toNotNull(),
                    secondTextSize = 14.sp,
                    secondTextFontWeight = FontWeight.Bold,
                    secondTextColor = MaterialTheme.colorScheme.onSurface,
                    readOnly = true,
                )
            }

            // Customer's name
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    leadingIcon = R.drawable.customer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    firstText = DebtCustomer,
                    firstTextSize = 14.sp,
                    firstTextFontWeight = FontWeight.SemiBold,
                    firstTextColor = secondaryTextColor,
                    secondText = customerName,
                    secondTextSize = 14.sp,
                    secondTextFontWeight = FontWeight.Bold,
                    secondTextColor = MaterialTheme.colorScheme.onSurface,
                    readOnly = true
                )
            }

            //Debt Amount
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.ic_money_filled,
                    firstText = DebtAmount,
                    firstTextSize = 14.sp,
                    firstTextFontWeight = FontWeight.SemiBold,
                    firstTextColor = secondaryTextColor,
                    secondText = "$currency ${debt.debtAmount.toDoubleDecimalPlaces()}",
                    secondTextSize = 14.sp,
                    secondTextFontWeight = FontWeight.Bold,
                    secondTextColor = MaterialTheme.colorScheme.onSurface,
                    value = debt.debtAmount.toTwoDecimalPlaces().toString(),
                    placeholder = DebtAmountPlaceholder,
                    label = EnterDebtAmount,
                    textFieldIcon = R.drawable.ic_money_filled,
                    readOnly = false,
                    keyboardType = KeyboardType.Number,
                    getUpdatedValue = { getUpdatedDebtAmount(it) }
                )
            }

            // Personnel
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    leadingIcon = R.drawable.personnel,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    firstText = Personnel_Name,
                    firstTextSize = 14.sp,
                    firstTextFontWeight = FontWeight.SemiBold,
                    firstTextColor = secondaryTextColor,
                    secondText = personnelName,
                    secondTextSize = 14.sp,
                    secondTextFontWeight = FontWeight.Bold,
                    secondTextColor = MaterialTheme.colorScheme.onSurface,
                    readOnly = true
                )
            }

            // Debt Short Notes
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.ic_short_notes,
                    firstText = ShortNotes,
                    firstTextSize = 14.sp,
                    firstTextFontWeight = FontWeight.SemiBold,
                    firstTextColor = secondaryTextColor,
                    secondText = debt.otherInfo.toNotNull(),
                    secondTextSize = 14.sp,
                    secondTextFontWeight = FontWeight.Bold,
                    secondTextColor = MaterialTheme.colorScheme.onSurface,
                    value = debt.otherInfo.toNotNull(),
                    label = EnterShortDescription,
                    placeholder = DebtShortNotesPlaceholder,
                    textFieldIcon = R.drawable.ic_short_notes,
                    readOnly = false,
                    getUpdatedValue = { getUpdatedDebtShortNotes(it) }
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.default))

            // Update
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalSpacing.current.buttonHeight)
                        .padding(LocalSpacing.current.small)
                        .clickable {
                            when (true) {
                                (debt.debtAmount < 0.1) -> {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please enter valid debt amount",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                else -> {
                                    val thisDebt = DebtEntity(
                                        debtId = 0,
                                        uniqueDebtId = debt.uniqueDebtId,
                                        date = debt.date,
                                        dayOfWeek = debt.dayOfWeek,
                                        uniqueCustomerId = debt.uniqueCustomerId,
                                        debtAmount = debt.debtAmount,
                                        uniquePersonnelId = debt.uniquePersonnelId,
                                        otherInfo = debt.otherInfo
                                    )
                                    updateDebt(thisDebt)
                                    confirmationInfoDialog = !confirmationInfoDialog
                                }
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = UpdateChanges,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        }


    }



    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingDebt,
        title = null,
        textContent = debtUpdateMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (debtUpdatingIsSuccessful) {
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
