package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt_repayment


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
import com.example.myshopmanagerapp.core.FormRelatedString.DebtRepaymentAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.DebtRepaymentCustomerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.DebtRepaymentDayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterDebtRepaymentAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.SaveDebtRepayment
import com.example.myshopmanagerapp.core.FormRelatedString.SelectDebtRepaymentCustomer
import com.example.myshopmanagerapp.core.FormRelatedString.SelectDebtRepaymentDate
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueDebtRepaymentId
import com.example.myshopmanagerapp.core.Functions.roundDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AddDebtRepaymentContent(
    debtRepayment: DebtRepaymentEntity,
    isSavingDebtRepayment: Boolean,
    debtRepaymentSavingIsSuccessful: Boolean,
    debtRepaymentSavingMessage: String?,
    mapOfCustomers: Map<String, String>,
    addDebtRepaymentDate: (String) -> Unit,
    addCustomer: (String) -> Unit,
    addDebtRepaymentAmount: (String) -> Unit,
    addShortNotes: (String) -> Unit,
    addDebtRepayment: (DebtRepaymentEntity) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    var customerName by remember {
        mutableStateOf(emptyString)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var debtRepaymentValueIsError by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val dateString = debtRepayment.date.toDate().toDateString()
            DatePickerTextField(
                defaultDate = "${debtRepayment.dayOfWeek}, $dateString",
                context = context,
                onValueChange = {_dateString->
                    addDebtRepaymentDate(_dateString)
                },
                label = SelectDebtRepaymentDate
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            BasicTextField1(
                value = debtRepayment.dayOfWeek.toNotNull(),
                onValueChange = {},
                placeholder = emptyString,
                label = DebtRepaymentDayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // DebtRepayment Customer
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisUniqueCustomerId by remember {
                mutableStateOf(debtRepayment.uniqueCustomerId)
            }
            AutoCompleteTextField(
                label = SelectDebtRepaymentCustomer,
                placeholder = DebtRepaymentCustomerPlaceholder,
                readOnly = true,
                expandedIcon = R.drawable.ic_person_filled,
                unexpandedIcon = R.drawable.ic_person_outline,
                listItems = mapOfCustomers.keys.toList(),
                getSelectedItem = {
                    customerName = it
                    thisUniqueCustomerId = mapOfCustomers[it] ?: emptyString
                    addCustomer(thisUniqueCustomerId)
                }
            )
        }

        // DebtRepayment Amount
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var debtRepaymentAmount by remember {
                mutableStateOf(debtRepayment.debtRepaymentAmount.toString())
            }
            BasicTextFieldWithTrailingIconError(
                value = debtRepaymentAmount.trim(),
                onValueChange = {_amount->
                    debtRepaymentAmount = _amount
                    addDebtRepaymentAmount(_amount)
                    debtRepaymentValueIsError = amountIsNotValid(_amount)
                },
                isError = debtRepaymentValueIsError,
                readOnly = false,
                placeholder = DebtRepaymentAmountPlaceholder,
                label = EnterDebtRepaymentAmount,
                icon = R.drawable.ic_money_outline,
                keyboardType = KeyboardType.Number
            )
        }

        // Short Description
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var shortDescription by remember {
                mutableStateOf(debtRepayment.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = shortDescription,
                onValueChange = {
                    shortDescription = it
                    addShortNotes(shortDescription)
                },
                placeholder = emptyString,
                label = EnterShortDescription,
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
            BasicButton(buttonName = SaveDebtRepayment) {
                when(true) {
                    (debtRepaymentValueIsError) -> {
                        Toast.makeText(context, "Please enter valid debt repayment amount", Toast.LENGTH_LONG)
                            .show()
                    }
                    (debtRepayment.debtRepaymentAmount < 0.1) -> {
                        Toast.makeText(context, "Please enter valid debt repayment amount", Toast.LENGTH_LONG)
                            .show()
                    }
                    (debtRepayment.uniqueCustomerId.isEmpty()) -> {
                        Toast.makeText(context, "Please select customer", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        val dateString = debtRepayment.date.toDate().toDateString()
                        val uniqueDebtRepaymentId = generateUniqueDebtRepaymentId("$customerName-${roundDouble(debtRepayment.debtRepaymentAmount)}-$dateString")

                        val thisDebtRepayment = DebtRepaymentEntity(0,
                            uniqueDebtRepaymentId = uniqueDebtRepaymentId,
                            date = debtRepayment.date,
                            dayOfWeek = debtRepayment.dayOfWeek,
                            uniqueCustomerId = debtRepayment.uniqueCustomerId,
                            debtRepaymentAmount = debtRepayment.debtRepaymentAmount,
                            uniquePersonnelId = debtRepayment.uniquePersonnelId,
                            otherInfo = debtRepayment.otherInfo
                        )
                        addDebtRepayment(thisDebtRepayment)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingDebtRepayment,
        title = null,
        textContent = debtRepaymentSavingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (debtRepaymentSavingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
