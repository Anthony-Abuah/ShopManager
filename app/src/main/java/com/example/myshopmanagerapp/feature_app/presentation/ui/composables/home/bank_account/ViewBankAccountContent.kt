package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.zero
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccountBalance
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccountInformation
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccountName
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccountNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankContact
import com.example.myshopmanagerapp.core.FormRelatedString.BankContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankLocation
import com.example.myshopmanagerapp.core.FormRelatedString.BankLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankName
import com.example.myshopmanagerapp.core.FormRelatedString.BankNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueBankAccountId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateBankAccountName
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateBankContact
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateBankLocation
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateBankName
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateShortNotes
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ViewBankAccountContent(
    bankInfo: BankAccountEntity,
    currency: String,
    bankUpdatingMessage: String?,
    bankUpdatingIsSuccessful: Boolean,
    isUpdatingBank: Boolean,
    getUpdatedBankName: (bankName: String) -> Unit,
    getUpdatedBankAccountName: (bankName: String) -> Unit,
    getUpdatedBankContact: (bankContact: String) -> Unit,
    getUpdatedBankLocation: (bankLocation: String) -> Unit,
    getUpdatedOtherInfo: (shortNotes: String) -> Unit,
    updateBank: () -> Unit,
    navigateBack: () -> Unit,
){

    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        // Bank Photo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.default),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .size(125.dp)
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(LocalSpacing.current.small),
                    painter = painterResource(id = R.drawable.ic_bank),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = emptyString
                )
            }
        }

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Bank Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = LocalSpacing.current.default,
                    horizontal = LocalSpacing.current.small
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = BankAccountInformation,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
        }

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Bank Account Id
        ViewTextValueRow(
            viewTitle = UniqueBankAccountId,
            viewValue = bankInfo.uniqueBankAccountId
        )

        HorizontalDivider()

        // Bank Account Name
        ViewOrUpdateTextValueRow(
            viewTitle = BankAccountName,
            viewValue = bankInfo.bankAccountName,
            placeholder = BankAccountNamePlaceholder,
            label = UpdateBankAccountName,
            icon = R.drawable.ic_bank,
            getUpdatedValue = { getUpdatedBankAccountName(it) }
        )

        HorizontalDivider()

        // Bank Name
        ViewOrUpdateTextValueRow(
            viewTitle = BankName,
            viewValue = bankInfo.bankName,
            placeholder = BankNamePlaceholder,
            label = UpdateBankName,
            icon = R.drawable.ic_bank,
            getUpdatedValue = { getUpdatedBankName(it) }
        )

        HorizontalDivider()

        // Bank Contact
        ViewOrUpdateNumberValueRow(
            viewTitle = BankContact,
            viewValue = bankInfo.bankContact,
            placeholder = BankContactPlaceholder,
            label = UpdateBankContact,
            icon = R.drawable.ic_contact,
            getUpdatedValue = { getUpdatedBankContact(it) }
        )

        HorizontalDivider()

        // Bank Location
        ViewOrUpdateNumberValueRow(
            viewTitle = BankLocation,
            viewValue = bankInfo.bankLocation.toNotNull(),
            placeholder = BankLocationPlaceholder,
            label = UpdateBankLocation,
            icon = R.drawable.ic_location,
            getUpdatedValue = { getUpdatedBankLocation(it) }
        )

        HorizontalDivider()

        // Bank AccountBalance
        ViewTextValueRow(
            viewTitle = BankAccountBalance,
            viewValue = "$currency ${bankInfo.accountBalance?.toString() ?: zero}"
        )

        HorizontalDivider()


        // Bank AccountBalance
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = bankInfo.otherInfo.toNotNull().ifBlank { NotAvailable },
            placeholder = BankShortNotesPlaceholder,
            label = UpdateShortNotes,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = { getUpdatedOtherInfo(it) }
        )

        HorizontalDivider()

        // Update Button
        Box(modifier = Modifier.padding(horizontal = LocalSpacing.current.default,
            vertical = LocalSpacing.current.medium)){
            BasicButton(buttonName = UpdateChanges) {
                if (bankInfo.bankAccountName.isEmpty()){
                    Toast.makeText(context, "Please enter bank account route", Toast.LENGTH_LONG).show()
                } else{
                    updateBank()
                    confirmationInfoDialog = !confirmationInfoDialog
                }
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingBank,
        title = null,
        textContent = bankUpdatingMessage ?: emptyString,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (bankUpdatingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}