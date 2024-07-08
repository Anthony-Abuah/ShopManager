package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt_repayment

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.Customer_Name
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.DebtRepaymentAmount
import com.example.myshopmanagerapp.core.FormRelatedString.DebtRepaymentAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.DebtRepaymentInformation
import com.example.myshopmanagerapp.core.FormRelatedString.EnterDebtRepaymentAmount
import com.example.myshopmanagerapp.core.FormRelatedString.Personnel_Name
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueDebtRepaymentId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ViewDebtRepaymentContent(
    debtRepayment: DebtRepaymentEntity,
    currency: String,
    debtRepaymentUpdatingMessage: String?,
    debtRepaymentUpdatingIsSuccessful: Boolean,
    isUpdatingDebtRepayment: Boolean,
    customerName: String,
    personnelName: String,
    getUpdatedDebtRepaymentDate: (String) -> Unit,
    getUpdatedDebtRepaymentAmount: (String) -> Unit,
    getUpdatedDebtRepaymentShortNotes: (String) -> Unit,
    updateDebtRepayment: (DebtRepaymentEntity) -> Unit,
    navigateBack: ()-> Unit,
) {
    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }


    BasicScreenColumnWithoutBottomBar {
        // DebtRepayment Photo
        ViewPhoto(icon = R.drawable.ic_money_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        //Debt Info
        ViewInfo(info = DebtRepaymentInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Debt Id
        ViewTextValueRow(
            viewTitle = UniqueDebtRepaymentId,
            viewValue = debtRepayment.uniqueDebtRepaymentId
        )

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = Date,
            viewDateString = "${debtRepayment.dayOfWeek}, ${debtRepayment.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedDebtRepaymentDate(it) }
        )

        HorizontalDivider()

        // Day Of The Week
        ViewTextValueRow(viewTitle = DayOfTheWeek, viewValue = debtRepayment.dayOfWeek.toNotNull())

        HorizontalDivider()

        // Debt Customer
        ViewTextValueRow(
            viewTitle = Customer_Name,
            viewValue = customerName
        )

        HorizontalDivider()

        // Debt Repayment Amount
        ViewOrUpdateNumberValueRow(
            viewTitle = DebtRepaymentAmount,
            viewValue = "$currency ${debtRepayment.debtRepaymentAmount}",
            placeholder = DebtRepaymentAmountPlaceholder,
            label = EnterDebtRepaymentAmount,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = {getUpdatedDebtRepaymentAmount(it)}
        )

        HorizontalDivider()

        // Debt Repayment Personnel
        ViewTextValueRow(
            viewTitle = Personnel_Name,
            viewValue = personnelName
        )

        HorizontalDivider()

        // Debt Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = debtRepayment.otherInfo.toNotNull(),
            placeholder = FormRelatedString.DebtShortNotesPlaceholder,
            label = FormRelatedString.EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedDebtRepaymentShortNotes(it)}
        )

        HorizontalDivider()

        Box(modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                when(true) {
                    (debtRepayment.debtRepaymentAmount < 0.1) -> {
                        Toast.makeText(context, "Please enter valid debt repayment amount", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        val thisDebtRepayment = DebtRepaymentEntity(
                            debtRepaymentId = debtRepayment.debtRepaymentId,
                            uniqueDebtRepaymentId = debtRepayment.uniqueDebtRepaymentId,
                            date = debtRepayment.date,
                            dayOfWeek = debtRepayment.dayOfWeek,
                            uniqueCustomerId = debtRepayment.uniqueCustomerId,
                            debtRepaymentAmount = debtRepayment.debtRepaymentAmount,
                            uniquePersonnelId = debtRepayment.uniquePersonnelId,
                            otherInfo = debtRepayment.otherInfo
                        )
                        updateDebtRepayment(thisDebtRepayment)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingDebtRepayment,
        title = null,
        textContent = debtRepaymentUpdatingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (debtRepaymentUpdatingIsSuccessful){ navigateBack() }
        confirmationInfoDialog = false
    }
}
