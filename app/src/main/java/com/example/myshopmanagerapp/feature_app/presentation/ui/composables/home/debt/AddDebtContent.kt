package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt

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
import com.example.myshopmanagerapp.core.FormRelatedString.DebtAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.DebtCustomerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.DebtDayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterDebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.SaveDebt
import com.example.myshopmanagerapp.core.FormRelatedString.SelectDebtCustomer
import com.example.myshopmanagerapp.core.FormRelatedString.SelectDebtDate
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueDebtId
import com.example.myshopmanagerapp.core.Functions.roundDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun AddDebtContent(
    debt: DebtEntity,
    mapOfCustomers: Map<String, String>,
    isSavingDebt: Boolean,
    debtSavingIsSuccessful: Boolean,
    debtSavingMessage: String?,
    addCustomer: () -> Unit,
    addDebtAmount: (String) -> Unit,
    addUniqueCustomerId: (String) -> Unit,
    addDateString: (String) -> Unit,
    addShortDescription: (String) -> Unit,
    addDebt: (DebtEntity) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var customerName by remember {
        mutableStateOf(emptyString)
    }
    var debtValueIsError by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val dateString = debt.date.toDate().toDateString()
            DatePickerTextField(
                defaultDate = "${debt.dayOfWeek}, $dateString",
                context = context,
                onValueChange = {_selectedLocalDateString->
                    addDateString(_selectedLocalDateString)
                },
                label = SelectDebtDate
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            BasicTextField1(
                value = debt.dayOfWeek.toNotNull(),
                onValueChange = {},
                placeholder = emptyString,
                label = DebtDayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // Debt Customer
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisUniqueCustomerId by remember {
                mutableStateOf(debt.uniqueCustomerId)
            }
            AutoCompleteWithAddButton(
                label = SelectDebtCustomer,
                listItems = mapOfCustomers.keys.toList(),
                placeholder = DebtCustomerPlaceholder,
                readOnly = true,
                expandedIcon = R.drawable.ic_person_filled,
                unexpandedIcon = R.drawable.ic_person_outline,
                onClickAddButton = {
                    addCustomer()
                },
                getSelectedItem = {
                    customerName = it
                    thisUniqueCustomerId = mapOfCustomers[it] ?: emptyString
                    addUniqueCustomerId(thisUniqueCustomerId)
                }
            )
        }

        // Debt Amount
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisDebtAmount by remember {
                mutableStateOf(roundDouble(debt.debtAmount).toString())
            }
            BasicTextFieldWithTrailingIconError(
                value = thisDebtAmount.trim(),
                onValueChange = {_amount->
                    thisDebtAmount = _amount
                    addDebtAmount(thisDebtAmount)
                    debtValueIsError = amountIsNotValid(_amount)
                },
                isError = debtValueIsError,
                readOnly = false,
                placeholder = DebtAmountPlaceholder,
                label = EnterDebtAmount,
                icon = R.drawable.ic_money_outline,
                keyboardType = KeyboardType.Number
            )
        }

        // Short Description
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisShortDescription by remember {
                mutableStateOf(debt.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = thisShortDescription,
                onValueChange = {
                    thisShortDescription = it
                    addShortDescription(it)
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
            BasicButton(buttonName = SaveDebt) {
                when(true){
                    (debtValueIsError) ->{
                        Toast.makeText(context, "Please enter valid debt amount", Toast.LENGTH_LONG).show()
                    }
                    (debt.debtAmount < 0.1) ->{
                        Toast.makeText(context, "Please enter valid debt amount", Toast.LENGTH_LONG).show()
                    }
                    (debt.uniqueCustomerId.isEmpty()) ->{
                        Toast.makeText(context, "Please select customer", Toast.LENGTH_LONG).show()
                    }
                    else ->{
                        val dateString = debt.date.toDate().toDateString()
                        val uniqueDebtId = generateUniqueDebtId("$customerName-${debt.debtAmount}-$dateString")
                        val thisDebt = DebtEntity(
                            debtId = 0,
                            uniqueDebtId = uniqueDebtId,
                            date = debt.date,
                            dayOfWeek = debt.dayOfWeek,
                            uniqueCustomerId = debt.uniqueCustomerId,
                            debtAmount = debt.debtAmount,
                            uniquePersonnelId = debt.uniquePersonnelId,
                            otherInfo = debt.otherInfo
                        )
                        addDebt(thisDebt)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingDebt,
        title = null,
        textContent = debtSavingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (debtSavingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
