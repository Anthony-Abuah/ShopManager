package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.revenue

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.EnterRevenueAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueDayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueTypeNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.RevenueTypePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SaveRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfHours
import com.example.myshopmanagerapp.core.FormRelatedString.SelectRevenueDate
import com.example.myshopmanagerapp.core.FormRelatedString.SelectRevenueType
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueRevenueId
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
fun AddRevenueContent(
    revenue: RevenueEntity,
    isSavingRevenue: Boolean,
    savingRevenueResponse: String,
    revenueIsSaved: Boolean,
    addRevenueDate: (String) -> Unit,
    addRevenueType: (String) -> Unit,
    addRevenueNumberOfHours: (Int) -> Unit,
    addRevenueAmount: (String) -> Unit,
    addOtherInfo: (String) -> Unit,
    addRevenue: (RevenueEntity) -> Unit,
    navigateBack: () -> Unit

) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var confirmationInfo by remember {
        mutableStateOf(false)
    }
    var openRevenueTypes by remember {
        mutableStateOf(false)
    }
    var revenueAmountIsNotValid by remember {
        mutableStateOf(false)
    }
    var revenueTypesJson = UserPreferences(context).getRevenueTypes.collectAsState(initial = null).value

    BasicScreenColumnWithoutBottomBar {
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            DatePickerTextField(
                defaultDate = "${revenue.dayOfWeek}, ${revenue.date.toDateString()}",
                context = context,
                onValueChange = {_dateString->
                    addRevenueDate(_dateString)
                },
                label = SelectRevenueDate
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            BasicTextField1(
                value = revenue.dayOfWeek.toNotNull(),
                onValueChange = {},
                placeholder = emptyString,
                label = RevenueDayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // Revenue Type
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val thisRevenueTypes = revenueTypesJson.toRevenueTypes()
            AutoCompleteWithAddButton(
                label = SelectRevenueType,
                placeholder = RevenueTypePlaceholder,
                listItems = thisRevenueTypes.map { it.revenueType }.plus("Sales"),
                readOnly = false,
                expandedIcon = R.drawable.ic_money_filled,
                unexpandedIcon = R.drawable.ic_money_outline,
                onClickAddButton = {
                    openRevenueTypes = !openRevenueTypes
                },
                getSelectedItem = { addRevenueType(it) }
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            AutoCompleteTextFieldForHours(
                label = NumberOfHours,
                getSelectedHourValue = {_hours->
                    addRevenueNumberOfHours(_hours)
                }
            )
        }

        // Revenue amount
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var revenueAmount by remember {
                mutableStateOf(revenue.revenueAmount.toString())
            }
            BasicTextFieldWithTrailingIconError(
                value = revenueAmount,
                onValueChange = {_amount->
                    revenueAmount = _amount
                    addRevenueAmount(_amount)
                    revenueAmountIsNotValid = amountIsNotValid(revenueAmount)
                },
                isError = revenueAmountIsNotValid,
                readOnly = false,
                placeholder = RevenueAmountPlaceholder,
                label = EnterRevenueAmount,
                icon = R.drawable.ic_money_outline,
                keyboardType = KeyboardType.Number
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var otherInfo by remember {
                mutableStateOf(revenue.otherInfo ?: emptyString)
            }
            DescriptionTextFieldWithTrailingIcon(
                value = otherInfo,
                onValueChange = {
                    otherInfo = it
                    addOtherInfo(it)
                },
                placeholder = emptyString,
                label = EnterShortDescription ,
                icon = R.drawable.ic_short_notes,
                keyboardType = KeyboardType.Text
            )
        }

        Box(modifier = Modifier.padding(
            horizontal = LocalSpacing.current.small,
            vertical = LocalSpacing.current.smallMedium,
        ),
            contentAlignment = Alignment.Center
        ){
            BasicButton(buttonName = SaveRevenue) {
                val uniqueRevenueId = generateUniqueRevenueId("${revenue.date.toDateString()}-Amt${revenue.revenueAmount}")
                when(true){
                    (revenue.revenueAmount < 1.0)->{
                        Toast.makeText(context, "Please enter a valid revenue amount", Toast.LENGTH_LONG).show()
                    }
                    (revenueAmountIsNotValid)->{
                        Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val revenueEntity = RevenueEntity(
                            0,
                            uniqueRevenueId = uniqueRevenueId,
                            date = revenue.date,
                            dayOfWeek = revenue.dayOfWeek,
                            revenueType = revenue.revenueType,
                            numberOfHours = revenue.numberOfHours,
                            revenueAmount = revenue.revenueAmount,
                            uniquePersonnelId = revenue.uniquePersonnelId,
                            otherInfo = revenue.otherInfo
                        )
                        addRevenue(revenueEntity)
                        confirmationInfo = !confirmationInfo
                    }
                }
            }
        }
    }
    BasicTextFieldAlertDialog(
        openDialog = openRevenueTypes,
        title = SelectRevenueType,
        textContent = emptyString,
        placeholder = RevenueTypePlaceholder,
        label = SelectRevenueType,
        icon = R.drawable.ic_money_filled,
        keyboardType = KeyboardType.Text,
        unconfirmedUpdatedToastText = RevenueTypeNotAdded,
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

    ConfirmationInfoDialog(
        openDialog = confirmationInfo,
        isLoading = isSavingRevenue,
        title = null,
        textContent = savingRevenueResponse,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (revenueIsSaved){
            navigateBack()
        }
        confirmationInfo = false
    }

}
