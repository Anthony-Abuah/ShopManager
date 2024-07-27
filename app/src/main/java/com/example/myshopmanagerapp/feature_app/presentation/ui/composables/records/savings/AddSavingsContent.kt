package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.savings

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
import com.example.myshopmanagerapp.core.FormRelatedString.AddBankPersonnel
import com.example.myshopmanagerapp.core.FormRelatedString.BankPersonnelNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.BankPersonnelPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterSavingsAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.Save
import com.example.myshopmanagerapp.core.FormRelatedString.SavingsAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SavingsBankPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SelectBankPersonnel
import com.example.myshopmanagerapp.core.FormRelatedString.SelectSavingsBank
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueSavingsId
import com.example.myshopmanagerapp.core.Functions.roundDouble
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
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
fun AddSavingsContent(
    savings: SavingsEntity,
    isInsertingSavings: Boolean,
    savingsInsertingIsSuccessful: Boolean,
    savingsInsertingMessage: String?,
    mapOfBanks: Map<String, String>,
    addSavingsDate: (String) -> Unit,
    addSusuCollector: (String) -> Unit,
    addUniqueBankId: (String) -> Unit,
    addSavingsAmount: (String) -> Unit,
    addShortNotes: (String) -> Unit,
    addBank: () -> Unit,
    addSavings: (SavingsEntity) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var savingsValueIsError by remember {
        mutableStateOf(false)
    }
    var openBankPersonnelDialog by remember {
        mutableStateOf(false)
    }
    var bankPersonnel = UserPreferences(context).getBankPersonnel.collectAsState(initial = null).value

    BasicScreenColumnWithoutBottomBar {

        // Date
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            DatePickerTextField(
                defaultDate = "${savings.dayOfWeek}, ${savings.date.toDateString()}",
                context = context,
                onValueChange = {_dateString->
                    addSavingsDate(_dateString)
                },
                label = Date
            )
        }

        // Day of the week
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            BasicTextField1(
                value = savings.dayOfWeek,
                onValueChange = {},
                placeholder = emptyString,
                label = DayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // Savings Amount
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var savingsAmount by remember {
                mutableStateOf(savings.savingsAmount.toString())
            }
            BasicTextFieldWithTrailingIconError(
                value = savingsAmount.trim(),
                onValueChange = {_amount->
                    savingsAmount = _amount
                    addSavingsAmount(_amount)
                    savingsValueIsError = amountIsNotValid(_amount)
                },
                isError = savingsValueIsError,
                readOnly = false,
                placeholder = SavingsAmountPlaceholder,
                label = EnterSavingsAmount,
                icon = R.drawable.ic_money_outline,
                keyboardType = KeyboardType.Number
            )
        }

        // Savings Bank Account
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var uniqueBankId by remember {
                mutableStateOf(savings.uniqueBankAccountId)
            }
            AutoCompleteWithAddButton(
                label = SelectSavingsBank,
                placeholder = SavingsBankPlaceholder,
                readOnly = true,
                expandedIcon = R.drawable.ic_bank_account,
                unexpandedIcon = R.drawable.ic_bank_account,
                listItems = mapOfBanks.keys.toList(),
                onClickAddButton = { addBank() },
                getSelectedItem = {
                    uniqueBankId = mapOfBanks[it].toNotNull()
                    addUniqueBankId(uniqueBankId)
                }
            )
        }

        // Susu Collector
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val bankPersonnelList = bankPersonnel.toBankPersonnelList()
            AutoCompleteWithAddButton(
                label = SelectBankPersonnel,
                placeholder = BankPersonnelPlaceholder,
                listItems = bankPersonnelList.map { susuCollector -> susuCollector.bankPersonnel.lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } },
                readOnly = true,
                expandedIcon = R.drawable.ic_person_filled,
                unexpandedIcon = R.drawable.ic_person_outline,
                onClickAddButton = { openBankPersonnelDialog = !openBankPersonnelDialog},
                getSelectedItem = { addSusuCollector(it) }
            )
        }

        // Short Description
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var shortDescription by remember {
                mutableStateOf(savings.otherInfo.toNotNull())
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
                    savingsValueIsError->{
                        Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val bankName = mapOfBanks[savings.uniqueBankAccountId].toNotNull()
                        val uniqueSavingsId = generateUniqueSavingsId("$bankName ${roundDouble(savings.savingsAmount)} ${savings.date.toDateString()}")
                        val thisSavings = SavingsEntity(
                            0,
                            uniqueSavingsId = uniqueSavingsId,
                            date = savings.date,
                            dayOfWeek = savings.dayOfWeek,
                            uniqueBankAccountId = savings.uniqueBankAccountId,
                            savingsAmount = savings.savingsAmount,
                            uniquePersonnelId = savings.uniquePersonnelId,
                            bankPersonnel = savings.bankPersonnel,
                            otherInfo = savings.otherInfo
                        )
                        addSavings(thisSavings)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }
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
        isLoading = isInsertingSavings,
        title = null,
        textContent = savingsInsertingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (savingsInsertingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
