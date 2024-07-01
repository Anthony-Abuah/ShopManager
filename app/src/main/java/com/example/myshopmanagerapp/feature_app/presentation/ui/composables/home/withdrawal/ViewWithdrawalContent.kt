package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.withdrawal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.AmountLabel
import com.example.myshopmanagerapp.core.FormRelatedString.AmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccount
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.EnterTransactionId
import com.example.myshopmanagerapp.core.FormRelatedString.Personnel_Name
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.TransactionId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueWithdrawalId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.FormRelatedString.WithdrawalAmount
import com.example.myshopmanagerapp.core.FormRelatedString.WithdrawalInformation
import com.example.myshopmanagerapp.core.FormRelatedString.WithdrawalShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.WithdrawalTransactionIdPlaceholder
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ViewWithdrawalContent(
    withdrawal: WithdrawalEntity,
    currency: String,
    personnelName: String,
    bankAccountName: String,
    isUpdatingWithdrawal: Boolean,
    withdrawalUpdatingIsSuccessful: Boolean,
    withdrawalUpdateMessage: String?,
    getUpdatedWithdrawalDate: (String) -> Unit,
    getUpdatedTransactionId: (String) -> Unit,
    getUpdatedWithdrawalAmount: (String) -> Unit,
    getUpdatedWithdrawalShortNotes: (String) -> Unit,
    updateWithdrawal: (WithdrawalEntity) -> Unit,
    navigateBack: () -> Unit,
) {

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar {
        // Withdrawal Photo
        ViewPhoto(icon = R.drawable.ic_money_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewInfo(info = WithdrawalInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Withdrawal Personnel
        ViewTextValueRow(
            viewTitle = UniqueWithdrawalId,
            viewValue = withdrawal.uniqueWithdrawalId
        )

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = FormRelatedString.Date,
            viewDateString = "${withdrawal.dayOfWeek}, ${withdrawal.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedWithdrawalDate(it) }
        )

        HorizontalDivider()

        // Day Of The Week
        ViewTextValueRow(viewTitle = DayOfTheWeek, viewValue = withdrawal.dayOfWeek)

        HorizontalDivider()

        // Withdrawal Amount
        ViewOrUpdateNumberValueRow(
            viewTitle = WithdrawalAmount,
            viewValue = "$currency ${withdrawal.withdrawalAmount}",
            placeholder = AmountPlaceholder,
            label = AmountLabel,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = { getUpdatedWithdrawalAmount(it)}
        )

        HorizontalDivider()

        // Withdrawal Personnel
        ViewTextValueRow(
            viewTitle = Personnel_Name,
            viewValue = personnelName
        )

        HorizontalDivider()

        // Withdrawal Bank
        ViewTextValueRow(
            viewTitle = BankAccount,
            viewValue = bankAccountName
        )

        HorizontalDivider()

        // Transaction Id
        ViewOrUpdateTextValueRow(
            viewTitle = TransactionId,
            viewValue = withdrawal.transactionId.toNotNull().ifBlank { NotAvailable },
            placeholder = WithdrawalTransactionIdPlaceholder,
            label = EnterTransactionId,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = {getUpdatedTransactionId(it)}
        )

        HorizontalDivider()

        // Withdrawal Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = withdrawal.otherInfo.toNotNull().ifBlank { NotAvailable },
            placeholder = WithdrawalShortNotesPlaceholder,
            label = EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedWithdrawalShortNotes(it)}
        )

        HorizontalDivider()

        Box(modifier = Modifier.padding(LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                updateWithdrawal(withdrawal)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingWithdrawal,
        title = null,
        textContent = withdrawalUpdateMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (withdrawalUpdatingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
