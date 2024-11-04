package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.revenue

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.Hour
import com.example.myshopmanagerapp.core.Constants.Hours
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterRevenueAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.ListOfHours
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfHours
import com.example.myshopmanagerapp.core.FormRelatedString.Personnel_Name
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueAmount
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueHoursPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueType
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueTypePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SelectRevenueType
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueRevenueId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.convertToInt
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toDoubleDecimalPlaces
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toRevenueTypes
import com.example.myshopmanagerapp.core.TypeConverters.toRevenueTypesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.domain.model.RevenueType
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.VerticalDisplayAndEditTextValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun ViewRevenueContent(
    revenue: RevenueEntity,
    personnelName: String,
    currency: String,
    updatingRevenueResponse: String,
    isUpdatingRevenue: Boolean,
    updatingRevenueIsSuccessful: Boolean,
    getUpdatedRevenueDate: (String) -> Unit,
    getUpdatedRevenueType: (String) -> Unit,
    getUpdatedRevenueHours: (Int) -> Unit,
    getUpdatedRevenueAmount: (String) -> Unit,
    getUpdatedRevenueOtherInfo: (String) -> Unit,
    updateRevenue: (RevenueEntity?) -> Unit,
    navigateBack: () -> Unit,
) {
    
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val userPreferences =  UserPreferences(context)

    var openRevenueTypes by remember {
        mutableStateOf(false)
    }
    var confirmationDialog by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = LocalSpacing.current.small),
            thickness = LocalSpacing.current.borderStroke
        )

        BasicScreenColumnWithoutBottomBar {
            var revenueTypesJson =
                userPreferences.getRevenueTypes.collectAsState(initial = null).value

            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
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
                    leadingIcon = R.drawable.revenue,
                    firstText = UniqueRevenueId,
                    secondText = revenue.uniqueRevenueId,
                    readOnly = true
                )
            }

            // Date
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
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
                    secondText = "${revenue.dayOfWeek}, ${revenue.date.toDate().toDateString()}",
                    readOnly = false,
                    isDate = true,
                    value = "${revenue.dayOfWeek}, ${revenue.date.toDate().toDateString()}",
                    getUpdatedValue = { getUpdatedRevenueDate(it) }
                )
            }

            // Day Of The Week
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
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
                    secondText = revenue.dayOfWeek.toNotNull(),
                    readOnly = true,
                )
            }


            // Revenue Type
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
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
                    leadingIcon = R.drawable.money,
                    firstText = RevenueType,
                    secondText = revenue.revenueType.toNotNull(),
                    value = revenue.revenueType.toNotNull(),
                    label = SelectRevenueType,
                    placeholder = RevenueTypePlaceholder,
                    listItems = revenueTypesJson.toRevenueTypes().map { it.revenueType },
                    addNewItem = { openRevenueTypes = !openRevenueTypes },
                    expandedIcon = R.drawable.ic_arrow_up,
                    unexpandedIcon = R.drawable.ic_arrow_down,
                    readOnly = false,
                    isAutoCompleteTextField = true,
                    getUpdatedValue = { getUpdatedRevenueType(it) }
                )
            }


            // Number of hours
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                val hours = if(revenue.numberOfHours == null || revenue.numberOfHours == 1) Hour else Hours
                val numberOfHoursOpened = if(revenue.numberOfHours == null || revenue.numberOfHours < 1) NotAvailable else revenue.numberOfHours.toString().plus(" $hours")
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.quantity,
                    firstText = NumberOfHours,
                    secondText = numberOfHoursOpened,
                    value = revenue.numberOfHours.toNotNull().toString(),
                    label = NumberOfHours,
                    placeholder = RevenueHoursPlaceholder,
                    listItems = ListOfHours,
                    selectOnlyList = true,
                    expandedIcon = R.drawable.ic_arrow_up,
                    unexpandedIcon = R.drawable.ic_arrow_down,
                    readOnly = false,
                    isAutoCompleteTextField = true,
                    getUpdatedValue = { _hours->
                        val hourValue = convertToInt(_hours.take(2).trim())
                        val validHours = if (hourValue > 24) 24 else hourValue
                        getUpdatedRevenueHours(validHours)
                    }
                )
            }

            // Revenue Amount
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
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
                    firstText = RevenueAmount,
                    secondText = "$currency ${revenue.revenueAmount.toDoubleDecimalPlaces()}",
                    value = revenue.revenueAmount.toDoubleDecimalPlaces(),
                    placeholder = RevenueAmountPlaceholder,
                    label = EnterRevenueAmount,
                    textFieldIcon = R.drawable.ic_money_filled,
                    readOnly = false,
                    keyboardType = KeyboardType.Number,
                    getUpdatedValue = { getUpdatedRevenueAmount(it) }
                )
            }

            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
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
                    firstText = Personnel_Name,
                    secondText = personnelName,
                    readOnly = true
                )
            }


            // Revenue Short Notes
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
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
                    secondText = revenue.otherInfo.toNotNull(),
                    value = revenue.otherInfo.toNotNull(),
                    label = EnterShortDescription,
                    placeholder = ShortNotesPlaceholder,
                    textFieldIcon = R.drawable.ic_short_notes,
                    readOnly = false,
                    getUpdatedValue = { getUpdatedRevenueOtherInfo(it) }
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    ),
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
                                (revenue.revenueAmount < 1.0) -> {
                                    Toast.makeText(
                                        context,
                                        "Please enter revenue amount",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                else -> {
                                    updateRevenue(revenue)
                                    confirmationDialog = !confirmationDialog
                                }
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = UpdateChanges,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

            BasicTextFieldAlertDialog(
                openDialog = openRevenueTypes,
                title = RevenueType,
                textContent = emptyString,
                placeholder = RevenueTypePlaceholder,
                label = SelectRevenueType,
                icon = R.drawable.ic_money_filled,
                keyboardType = KeyboardType.Text,
                unconfirmedUpdatedToastText = FormRelatedString.RevenueTypeNotAdded,
                confirmedUpdatedToastText = null,
                getValue = { _revenueType ->
                    val newRevenueType = RevenueType(_revenueType.trim())
                    val revenueTypes = revenueTypesJson.toRevenueTypes()
                    val mutableRevenueTypes = mutableListOf<RevenueType>()
                    if (_revenueType.isBlank()) {
                        Toast.makeText(context, SelectRevenueType, Toast.LENGTH_LONG).show()
                        openRevenueTypes = false
                    } else if (revenueTypes.map { it.revenueType.trim().lowercase(Locale.ROOT) }
                            .contains(_revenueType.trim().lowercase(Locale.ROOT))) {
                        Toast.makeText(
                            context,
                            "Revenue type: $_revenueType already exists",
                            Toast.LENGTH_LONG
                        ).show()
                        openRevenueTypes = false
                    } else {
                        mutableRevenueTypes.addAll(revenueTypes)
                        mutableRevenueTypes.add(newRevenueType)
                        revenueTypesJson =
                            mutableRevenueTypes.toSet().sortedBy { it.revenueType.first() }
                                .toRevenueTypesJson()
                        coroutineScope.launch {
                            UserPreferences(context).saveRevenueTypes(revenueTypesJson.toNotNull())
                        }
                        Toast.makeText(
                            context,
                            "Revenue Type: $_revenueType successfully added",
                            Toast.LENGTH_LONG
                        ).show()
                        openRevenueTypes = false
                    }
                }
            ) {
                openRevenueTypes = false
            }
        }

    }

    ConfirmationInfoDialog(
        openDialog = confirmationDialog,
        isLoading = isUpdatingRevenue,
        title = null,
        textContent = updatingRevenueResponse,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (updatingRevenueIsSuccessful){ navigateBack() }
        confirmationDialog = false
    }
}
