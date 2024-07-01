package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.savings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AddBankPersonnel
import com.example.myshopmanagerapp.core.FormRelatedString.AmountLabel
import com.example.myshopmanagerapp.core.FormRelatedString.AmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccount
import com.example.myshopmanagerapp.core.FormRelatedString.SavingsAmount
import com.example.myshopmanagerapp.core.FormRelatedString.SavingsInformation
import com.example.myshopmanagerapp.core.FormRelatedString.BankPersonnel
import com.example.myshopmanagerapp.core.FormRelatedString.BankPersonnelNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.SelectBankPersonnel
import com.example.myshopmanagerapp.core.FormRelatedString.BankPersonnelPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Personnel_Name
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueSavingsId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.TypeConverters.toBankPersonnelJson
import com.example.myshopmanagerapp.core.TypeConverters.toBankPersonnelList
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity
import com.example.myshopmanagerapp.feature_app.domain.model.BankPersonnel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun ViewSavingsContent(
    savings: SavingsEntity,
    bankAccountName: String,
    personnelName: String,
    currency: String,
    isUpdatingSavings: Boolean,
    savingsUpdatingIsSuccessful: Boolean,
    savingsUpdateMessage: String?,
    getUpdatedSavingsDate: (String) -> Unit,
    getUpdatedBankPersonnelName: (String) -> Unit,
    getUpdatedSavingsAmount: (String) -> Unit,
    getUpdatedShortNotes: (String) -> Unit,
    updateSavings: (SavingsEntity) -> Unit,
    navigateBack: () -> Unit,
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var bankPersonnel = UserPreferences(context).getBankPersonnel.collectAsState(initial = null).value

    var openBankPersonnelDialog by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        // Savings Photo
        ViewPhoto(icon = R.drawable.ic_money_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewInfo(info = SavingsInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Savings Id
        ViewTextValueRow(viewTitle = UniqueSavingsId, viewValue = savings.uniqueSavingsId)

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = Date,
            viewDateString = "${savings.dayOfWeek}, ${savings.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedSavingsDate(it) }
        )

        HorizontalDivider()

        // Day Of The Week
        ViewTextValueRow(viewTitle = DayOfTheWeek, viewValue = savings.dayOfWeek)

        HorizontalDivider()

        // Savings Amount
        ViewOrUpdateNumberValueRow(
            viewTitle = SavingsAmount,
            viewValue = "$currency ${savings.savingsAmount}",
            placeholder = AmountPlaceholder,
            label = AmountLabel,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = { getUpdatedSavingsAmount(it)}
        )

        HorizontalDivider()


        // Savings Personnel
        ViewTextValueRow(
            viewTitle = Personnel_Name,
            viewValue = personnelName
        )

        HorizontalDivider()

        // Savings Bank
        ViewTextValueRow(
            viewTitle = BankAccount,
            viewValue = bankAccountName
        )

        HorizontalDivider()

        // Susu Collector
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = BankPersonnel,
            viewValue = savings.bankPersonnel?.ifBlank { NotAvailable } ?: NotAvailable,
            placeholder = BankPersonnelPlaceholder,
            label = SelectBankPersonnel,
            readOnly = false,
            expandedIcon = R.drawable.ic_money_filled,
            unexpandedIcon = R.drawable.ic_money_outline,
            listItems = bankPersonnel.toBankPersonnelList().map { it.bankPersonnel },
            onClickAddButton = { openBankPersonnelDialog = !openBankPersonnelDialog },
            getUpdatedValue = { getUpdatedBankPersonnelName(it) }
        )

        HorizontalDivider()

        // Savings Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = savings.otherInfo?.ifBlank { NotAvailable } ?: NotAvailable,
            placeholder = ExpenseShortNotesPlaceholder,
            label = EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedShortNotes(it)}
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        
        Box(modifier = Modifier.padding(LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                val thisSavings = SavingsEntity(
                    savingsId = savings.savingsId,
                    uniqueSavingsId = savings.uniqueSavingsId,
                    date = savings.date,
                    dayOfWeek = savings.dayOfWeek,
                    uniqueBankAccountId = savings.uniqueBankAccountId,
                    savingsAmount = savings.savingsAmount,
                    uniquePersonnelId = savings.uniquePersonnelId,
                    bankPersonnel = savings.bankPersonnel,
                    otherInfo = savings.otherInfo
                )
                updateSavings(thisSavings)
                confirmationInfoDialog = !confirmationInfoDialog

            }
        }
        BasicTextFieldAlertDialog(
            openDialog = openBankPersonnelDialog,
            title = AddBankPersonnel,
            textContent = emptyString,
            placeholder = BankPersonnelPlaceholder,
            label = SelectBankPersonnel,
            icon = R.drawable.ic_person_outline,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = BankPersonnelNotAdded,
            confirmedUpdatedToastText = null,
            getValue = { _newBankPersonnel ->
                val newBankPersonnel = BankPersonnel(_newBankPersonnel)
                val bankPersonnelList = bankPersonnel.toBankPersonnelList()
                val mutableBankPersonnel = mutableListOf<BankPersonnel>()
                if (bankPersonnelList.map { it.bankPersonnel.trim().lowercase(Locale.ROOT) }.contains(_newBankPersonnel.trim().lowercase(Locale.ROOT))) {
                    Toast.makeText(context, "$_newBankPersonnel already exists", Toast.LENGTH_LONG).show()
                    openBankPersonnelDialog = false
                } else {
                    mutableBankPersonnel.addAll(bankPersonnelList)
                    mutableBankPersonnel.add(newBankPersonnel)
                    bankPersonnel = mutableBankPersonnel.toBankPersonnelJson()
                    coroutineScope.launch {
                        UserPreferences(context).saveBankPersonnel(bankPersonnel ?: emptyString)
                    }
                    Toast.makeText(context, "$_newBankPersonnel successfully added", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            openBankPersonnelDialog = false
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingSavings,
        title = null,
        textContent = savingsUpdateMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (savingsUpdatingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
