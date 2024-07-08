package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.R
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
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueDebtId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ViewDebtContent(
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

    BasicScreenColumnWithoutBottomBar {
        // Debt Photo
        ViewPhoto(icon = R.drawable.ic_money_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        //Debt Info
        ViewInfo(DebtInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Debt Id
        ViewTextValueRow(
            viewTitle = UniqueDebtId,
            viewValue = debt.uniqueDebtId
        )

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = Date,
            viewDateString = "${debt.dayOfWeek}, ${debt.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedDebtDate(it) }
        )

        HorizontalDivider()

        // Day Of The Week
        ViewTextValueRow(viewTitle = DayOfTheWeek, viewValue = debt.dayOfWeek.toNotNull())

        HorizontalDivider()

        // Debt Customer
        ViewTextValueRow(
            viewTitle = DebtCustomer,
            viewValue = customerName
        )

        HorizontalDivider()

        // Debt Amount
        ViewOrUpdateNumberValueRow(
            viewTitle = DebtAmount,
            viewValue = "$currency ${debt.debtAmount}",
            placeholder = DebtAmountPlaceholder,
            label = EnterDebtAmount,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = {getUpdatedDebtAmount(it)}
        )

        HorizontalDivider()

        // Debt Personnel
        ViewTextValueRow(
            viewTitle = Personnel_Name,
            viewValue = personnelName
        )

        HorizontalDivider()

        // Debt Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = debt.otherInfo.toNotNull(),
            placeholder = DebtShortNotesPlaceholder,
            label = EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedDebtShortNotes(it)}
        )

        HorizontalDivider()

        
        Box(modifier = Modifier.padding(LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                when(true){
                    (debt.debtAmount < 0.1) ->{
                        Toast.makeText(context, "Please enter valid debt amount", Toast.LENGTH_LONG).show()
                    }
                    else ->{
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
            }
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
        if (debtUpdatingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
