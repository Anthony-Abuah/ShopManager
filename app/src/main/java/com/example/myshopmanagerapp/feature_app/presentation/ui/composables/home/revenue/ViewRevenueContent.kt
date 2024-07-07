package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.revenue

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.EnterRevenueAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.ListOfHours
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfHours
import com.example.myshopmanagerapp.core.FormRelatedString.Personnel_Name
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueAmount
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueHoursPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueInformation
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueType
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueTypePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SelectRevenueType
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueRevenueId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.convertToInt
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toRevenueTypes
import com.example.myshopmanagerapp.core.TypeConverters.toRevenueTypesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.domain.model.RevenueType
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
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

    BasicScreenColumnWithoutBottomBar {
        var revenueTypesJson = userPreferences.getRevenueTypes.collectAsState(initial = null).value
        // Revenue Photo
        ViewPhoto(icon = R.drawable.ic_money_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewInfo(info = RevenueInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Savings Id
        ViewTextValueRow(viewTitle = UniqueRevenueId, viewValue = revenue.uniqueRevenueId)

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = Date,
            viewDateString = "${revenue.dayOfWeek}, ${revenue.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedRevenueDate(it) }
        )

        HorizontalDivider()

        // Day Of The Week
        ViewTextValueRow(viewTitle = FormRelatedString.DayOfTheWeek, viewValue = revenue.dayOfWeek.toNotNull())

        HorizontalDivider()

        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = RevenueType,
            viewValue = revenue.revenueType.toNotNull(),
            placeholder = RevenueTypePlaceholder,
            label = SelectRevenueType,
            expandedIcon = R.drawable.ic_money_filled,
            unexpandedIcon = R.drawable.ic_money_outline,
            listItems = revenueTypesJson.toRevenueTypes().map { it.revenueType }.plus("Sales"),
            onClickAddButton = { openRevenueTypes = !openRevenueTypes },
            getUpdatedValue = { getUpdatedRevenueType(it) }
        )

        HorizontalDivider()

        // Hours worked
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = NumberOfHours,
            viewValue = revenue.numberOfHours.toNotNull().toString(),
            placeholder = RevenueHoursPlaceholder,
            label = NumberOfHours,
            expandedIcon = R.drawable.ic_money_filled,
            unexpandedIcon = R.drawable.ic_money_outline,
            listItems = ListOfHours,
            onClickAddButton = { /*TODO*/ },
            getUpdatedValue = { getUpdatedRevenueHours(convertToInt(it.take(2).trim())) }
        )

        HorizontalDivider()

        // Revenue Amount
        ViewOrUpdateNumberValueRow(
            viewTitle = RevenueAmount,
            viewValue = "$currency ${revenue.revenueAmount}",
            placeholder = RevenueAmountPlaceholder,
            label = EnterRevenueAmount,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = { getUpdatedRevenueAmount(it) }
        )
        HorizontalDivider()

        // Personnel
        ViewTextValueRow(
            viewTitle = Personnel_Name,
            viewValue = personnelName
        )
        HorizontalDivider()

        // Revenue Short Notes
        ViewOrUpdateTextValueRow(
            viewTitle = RevenueShortNotes,
            viewValue = revenue.otherInfo ?: NotAvailable,
            placeholder = ShortNotesPlaceholder,
            label = EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = { getUpdatedRevenueOtherInfo(it) }
        )

        HorizontalDivider()

        Box(modifier = Modifier.padding(horizontal = LocalSpacing.current.smallMedium,
            vertical = LocalSpacing.current.large)){
            BasicButton(buttonName = UpdateChanges) {
                when(true){
                    (revenue.revenueAmount < 1.0) -> {
                        Toast.makeText(context, "Please enter revenue amount", Toast.LENGTH_LONG).show()
                    }
                    else ->{
                        updateRevenue(revenue)
                        confirmationDialog = !confirmationDialog
                    }
                }
            }
        }

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
                if (_revenueType.isBlank()){
                    Toast.makeText(context, SelectRevenueType, Toast.LENGTH_LONG).show()
                    openRevenueTypes = false
                }
                else if (revenueTypes.map { it.revenueType.trim().lowercase(Locale.ROOT) }.contains(_revenueType.trim().lowercase(Locale.ROOT))) {
                    Toast.makeText(context, "Revenue type: $_revenueType already exists", Toast.LENGTH_LONG).show()
                    openRevenueTypes = false
                } else {
                    mutableRevenueTypes.addAll(revenueTypes)
                    mutableRevenueTypes.add(newRevenueType)
                    revenueTypesJson = mutableRevenueTypes.toSet().sortedBy { it.revenueType.first() }.toRevenueTypesJson()
                    coroutineScope.launch {
                        UserPreferences(context).saveRevenueTypes(revenueTypesJson.toNotNull())
                    }
                    Toast.makeText(context, "Revenue Type: $_revenueType successfully added", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            openRevenueTypes = false
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
