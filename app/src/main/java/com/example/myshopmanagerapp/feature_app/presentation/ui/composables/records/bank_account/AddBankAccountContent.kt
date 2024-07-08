package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account

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
import com.example.myshopmanagerapp.core.FormRelatedString.BankAccountNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.BankShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.BankShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterBankAccountName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterBankContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterBankLocation
import com.example.myshopmanagerapp.core.FormRelatedString.EnterBankName
import com.example.myshopmanagerapp.core.FormRelatedString.SaveBank
import com.example.myshopmanagerapp.core.Functions.nameIsValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueBankId
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AddBankAccountContent(
    addBankAccount: (BankAccountEntity) -> Unit,
    isSavingBank: Boolean,
    bankSavingMessage: String?,
    bankSavingIsSuccessful: Boolean,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var bankName by remember { mutableStateOf(emptyString) }
    var bankAccountName by remember { mutableStateOf(emptyString) }
    var bankContact by remember { mutableStateOf(emptyString) }
    var bankLocation by remember { mutableStateOf(emptyString) }
    var bankAccountNameIsInvalid by remember { mutableStateOf(false) }
    var bankNameIsInvalid by remember { mutableStateOf(false) }

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    var anyOtherInfo by remember {
        mutableStateOf(emptyString)
    }

    BasicScreenColumnWithoutBottomBar {
            // Bank name
            Box(modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                BasicTextFieldWithTrailingIconError(
                    value = bankAccountName,
                    onValueChange = {
                        bankAccountName = it
                        bankAccountNameIsInvalid = nameIsValid(it)
                    },
                    readOnly = false,
                    isError = bankAccountNameIsInvalid,
                    placeholder = BankAccountNamePlaceholder,
                    label = EnterBankAccountName,
                    icon = R.drawable.ic_bank,
                    keyboardType = KeyboardType.Text
                )
            }

            // Bank name
            Box(modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                BasicTextFieldWithTrailingIconError(
                    value = bankName,
                    onValueChange = {
                        bankName = it
                        bankNameIsInvalid = nameIsValid(it)
                    },
                    readOnly = false,
                    isError = bankNameIsInvalid,
                    placeholder = BankNamePlaceholder,
                    label = EnterBankName,
                    icon = R.drawable.ic_person_filled,
                    keyboardType = KeyboardType.Text
                )
            }

            //Bank contact
            Box(modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                BasicTextFieldWithTrailingIconError(
                    value = bankContact,
                    onValueChange = {
                        bankContact = it
                    },
                    readOnly = false,
                    isError = false,
                    placeholder = BankContactPlaceholder,
                    label = EnterBankContact,
                    icon = R.drawable.ic_contact,
                    keyboardType = KeyboardType.Phone
                )
            }

            // Bank location
            Box(modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                BasicTextFieldWithTrailingIconError(
                    value = bankLocation,
                    onValueChange = {
                        bankLocation = it
                    },
                    readOnly = false,
                    isError = false,
                    placeholder = BankLocationPlaceholder,
                    label = EnterBankLocation,
                    icon = R.drawable.ic_location,
                    keyboardType = KeyboardType.Text
                )
            }

            // Short notes/description
            Box(modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                DescriptionTextFieldWithTrailingIcon(
                    value = anyOtherInfo,
                    onValueChange = {
                        anyOtherInfo = it
                    },
                    placeholder = BankShortNotesPlaceholder,
                    label = BankShortNotes,
                    icon = R.drawable.ic_short_notes,
                    keyboardType = KeyboardType.Text
                )
            }

            // Save Button
            Box(modifier = Modifier.padding(
                vertical = LocalSpacing.current.smallMedium,
                horizontal = LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                BasicButton(buttonName = SaveBank) {
                    when(true){
                        bankNameIsInvalid->{
                            Toast.makeText(context, "Enter valid bank name!", Toast.LENGTH_LONG).show()
                        }
                        bankAccountNameIsInvalid->{
                            Toast.makeText(context, "Enter valid bank account name!", Toast.LENGTH_LONG).show()
                        }
                        else->{
                            val uniqueBankAccountId = generateUniqueBankId(bankAccountName)
                            val bankAccount = BankAccountEntity(
                                bankId = 0,
                                uniqueBankAccountId = uniqueBankAccountId,
                                bankAccountName = bankAccountName.trimEnd(),
                                bankName = bankName.trimEnd(),
                                bankContact = bankContact.trimEnd(),
                                bankLocation = bankLocation.ifEmpty { null },
                                otherInfo = anyOtherInfo.ifEmpty { null },
                                accountBalance = 0.0,
                            )
                            addBankAccount(bankAccount)
                            confirmationInfoDialog = !confirmationInfoDialog
                        }
                    }
                }
            }
        }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingBank,
        title = null,
        textContent = bankSavingMessage ?: emptyString,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (bankSavingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }

}
