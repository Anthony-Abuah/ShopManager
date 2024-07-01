package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.withdrawal

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
import com.example.myshopmanagerapp.core.FormRelatedString.AmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccountNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.EnterTransactionId
import com.example.myshopmanagerapp.core.FormRelatedString.Save
import com.example.myshopmanagerapp.core.FormRelatedString.SelectBankAccount
import com.example.myshopmanagerapp.core.FormRelatedString.WithdrawalTransactionIdPlaceholder
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueWithdrawalId
import com.example.myshopmanagerapp.core.Functions.roundDouble
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AddWithdrawalContent(
    withdrawal: WithdrawalEntity,
    isInsertingWithdrawal: Boolean,
    withdrawalInsertingIsSuccessful: Boolean,
    withdrawalInsertingMessage: String?,
    mapOfBanks: Map<String, String>,
    addWithdrawalDate: (String) -> Unit,
    addTransactionId: (String) -> Unit,
    addUniqueBankAccountId: (String) -> Unit,
    addWithdrawalAmount: (String) -> Unit,
    addShortNotes: (String) -> Unit,
    addBank: () -> Unit,
    addWithdrawal: (WithdrawalEntity) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var withdrawalValueIsError by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar {

        // Date
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            DatePickerTextField(
                defaultDate = "${withdrawal.dayOfWeek}, ${withdrawal.date.toDateString()}",
                context = context,
                onValueChange = {_dateString->
                    addWithdrawalDate(_dateString)
                },
                label = Date
            )
        }

        // Day of the week
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            BasicTextField1(
                value = withdrawal.dayOfWeek,
                onValueChange = {},
                placeholder = emptyString,
                label = DayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // Withdrawal Amount
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var withdrawalAmount by remember {
                mutableStateOf(withdrawal.withdrawalAmount.toString())
            }
            BasicTextFieldWithTrailingIconError(
                value = withdrawalAmount.trim(),
                onValueChange = {_amount->
                    withdrawalAmount = _amount
                    addWithdrawalAmount(_amount)
                    withdrawalValueIsError = amountIsNotValid(_amount)
                },
                isError = withdrawalValueIsError,
                readOnly = false,
                placeholder = AmountPlaceholder,
                label = AmountPlaceholder,
                icon = R.drawable.ic_money_outline,
                keyboardType = KeyboardType.Number
            )
        }

        // Withdrawal Bank Account
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var uniqueBankAccountId by remember {
                mutableStateOf(withdrawal.uniqueBankAccountId)
            }
            AutoCompleteWithAddButton(
                label = SelectBankAccount,
                placeholder = BankAccountNamePlaceholder,
                readOnly = true,
                expandedIcon = R.drawable.ic_bank,
                unexpandedIcon = R.drawable.ic_bank,
                listItems = mapOfBanks.keys.toList(),
                onClickAddButton = { addBank() },
                getSelectedItem = {
                    uniqueBankAccountId = mapOfBanks[it].toNotNull()
                    addUniqueBankAccountId(uniqueBankAccountId)
                }
            )
        }

        // Transaction Id
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var transactionId by remember {
                mutableStateOf(withdrawal.transactionId.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = transactionId.trim(),
                onValueChange = {_value->
                    transactionId = _value
                    addTransactionId(_value)
                },
                isError = false,
                readOnly = false,
                placeholder = WithdrawalTransactionIdPlaceholder,
                label = EnterTransactionId,
                icon = R.drawable.ic_money_outline,
                keyboardType = KeyboardType.Text
            )
        }

        // Short Description
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var shortDescription by remember {
                mutableStateOf(withdrawal.otherInfo.toNotNull())
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
            BasicButton(buttonName = Save) {
                when(true){
                    withdrawalValueIsError->{
                        Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val bankName = mapOfBanks[withdrawal.uniqueBankAccountId].toNotNull()
                        val uniqueWithdrawalId = generateUniqueWithdrawalId("$bankName-${roundDouble(withdrawal.withdrawalAmount)}-${withdrawal.date.toDateString()}")
                        val thisWithdrawal = WithdrawalEntity(
                            0,
                            uniqueWithdrawalId = uniqueWithdrawalId,
                            date = withdrawal.date,
                            dayOfWeek = withdrawal.dayOfWeek,
                            transactionId = withdrawal.transactionId,
                            uniqueBankAccountId = withdrawal.uniqueBankAccountId,
                            withdrawalAmount = withdrawal.withdrawalAmount,
                            uniquePersonnelId = withdrawal.uniquePersonnelId,
                            otherInfo = withdrawal.otherInfo
                        )
                        addWithdrawal(thisWithdrawal)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }

            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isInsertingWithdrawal,
        title = null,
        textContent = withdrawalInsertingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (withdrawalInsertingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
