package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses

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
import com.example.myshopmanagerapp.core.FormRelatedString.AddExpenseName
import com.example.myshopmanagerapp.core.FormRelatedString.AddExpenseType
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterExpenseAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterExpenseName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseNameNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseTypeNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseTypePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SaveExpense
import com.example.myshopmanagerapp.core.FormRelatedString.SelectExpenseType
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueExpenseId
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseNames
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseNamesJson
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseTypes
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseTypesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseName
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseType
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun AddExpenseContent(
    expense: ExpenseEntity,
    isSavingExpense: Boolean,
    expenseIsSaved: Boolean,
    savingExpenseResponse: String?,
    addExpenseDate: (String) -> Unit,
    addExpenseName: (String) -> Unit,
    addExpenseType: (String) -> Unit,
    addExpenseAmount: (String) -> Unit,
    addExpenseOtherInfo: (String) -> Unit,
    addExpense: (ExpenseEntity) -> Unit,
    navigateBack: () -> Unit,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var expenseNamesJson = UserPreferences(context).getExpenseNames.collectAsState(initial = null).value
    var expenseTypesJson = UserPreferences(context).getExpenseTypes.collectAsState(initial = null).value

    var openExpenseNames by remember {
        mutableStateOf(false)
    }
    var openExpenseTypes by remember {
        mutableStateOf(false)
    }
    var expenseAmountIsWrong by remember {
        mutableStateOf(false)
    }
    var confirmationInfo by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        // Date
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            DatePickerTextField(
                defaultDate = "${expense.dayOfWeek}, ${expense.date.toDateString()}",
                context = context,
                onValueChange = {_dateString->
                    addExpenseDate(_dateString)
                },
                label = Date
            )
        }

        // Day
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            BasicTextField1(
                value = expense.dayOfWeek.toNotNull(),
                onValueChange = {},
                placeholder = emptyString,
                label = DayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }

        // Expense Name
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val thisExpenseNames = expenseNamesJson.toExpenseNames()
            AutoCompleteWithAddButton(
                label = EnterExpenseName,
                placeholder = ExpenseNamePlaceholder,
                listItems = thisExpenseNames.map { it.expenseName },
                readOnly = false,
                expandedIcon = R.drawable.ic_money_filled,
                unexpandedIcon = R.drawable.ic_money_outline,
                onClickAddButton = { openExpenseNames = !openExpenseNames },
                getSelectedItem = { addExpenseName(it) }
            )
        }

        // Expense Type
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val thisExpenseTypes = expenseTypesJson.toExpenseTypes()
            AutoCompleteWithAddButton(
                label = SelectExpenseType,
                placeholder = ExpenseTypePlaceholder,
                listItems = thisExpenseTypes.map { it.expenseType },
                readOnly = false,
                expandedIcon = R.drawable.ic_money_filled,
                unexpandedIcon = R.drawable.ic_money_outline,
                onClickAddButton = {
                    openExpenseTypes = !openExpenseTypes
                },
                getSelectedItem = { addExpenseType(it) }
            )
        }
        
        // Amount
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var expenseAmount by remember {
                mutableStateOf(expense.expenseAmount.toString())
            }
            BasicTextFieldWithTrailingIconError(
                value = expenseAmount,
                onValueChange = {_amount->
                    expenseAmount = _amount
                    addExpenseAmount(_amount)
                    expenseAmountIsWrong = amountIsNotValid(_amount)
                },
                isError = expenseAmountIsWrong,
                readOnly = false,
                placeholder = ExpenseAmountPlaceholder,
                label = EnterExpenseAmount,
                icon = R.drawable.ic_money_outline,
                keyboardType = KeyboardType.Number
            )
        }

        // Short notes
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var shortNotes by remember {
                mutableStateOf(expense.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = shortNotes,
                onValueChange = {
                    shortNotes = it
                    addExpenseOtherInfo(it)
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
            BasicButton(buttonName = SaveExpense) {
                val uniqueExpenseId = generateUniqueExpenseId("${expense.expenseName}-${expense.date.toDateString()}-Amt${expense.expenseAmount}")
                when(true){
                    (expense.expenseAmount < 0.1)->{
                        Toast.makeText(context, "Please enter valid expense amount", Toast.LENGTH_LONG).show()
                    }
                    (expenseAmountIsWrong)->{
                        Toast.makeText(context, "Please enter valid amount", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val expenseEntity = ExpenseEntity(
                            0,
                            uniqueExpenseId = uniqueExpenseId,
                            date = expense.date,
                            dayOfWeek = expense.dayOfWeek,
                            expenseName = expense.expenseName,
                            expenseAmount = expense.expenseAmount,
                            expenseType = expense.expenseType,
                            uniquePersonnelId = expense.uniquePersonnelId,
                            otherInfo = expense.otherInfo
                        )
                        addExpense(expenseEntity)
                        confirmationInfo = !confirmationInfo
                    }
                }
            }
        }

        BasicTextFieldAlertDialog(
            openDialog = openExpenseNames,
            title = AddExpenseName,
            textContent = emptyString,
            placeholder = ExpenseNamePlaceholder,
            label = EnterExpenseName,
            icon = R.drawable.ic_expense,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = ExpenseNameNotAdded,
            confirmedUpdatedToastText = null,
            getValue = { _expenseName ->
                val newExpenseName = ExpenseName(_expenseName.trim())
                val expenseNames = expenseNamesJson.toExpenseNames()
                val mutableExpenseNames = mutableListOf<ExpenseName>()
                if (_expenseName.isBlank()){
                    Toast.makeText(context, EnterExpenseName, Toast.LENGTH_LONG).show()
                    openExpenseNames = false
                }
                else if (expenseNames.map { it.expenseName.trim().lowercase(Locale.ROOT) }.contains(_expenseName.trim().lowercase(Locale.ROOT))) {
                    Toast.makeText(context, "Expense type: $_expenseName already exists", Toast.LENGTH_LONG).show()
                    openExpenseNames = false
                } else {
                    mutableExpenseNames.addAll(expenseNames)
                    mutableExpenseNames.add(newExpenseName)
                    expenseNamesJson = mutableExpenseNames.toSet().sortedBy { it.expenseName.first() }.toExpenseNamesJson()
                    coroutineScope.launch {
                        UserPreferences(context).saveExpenseNames(expenseNamesJson.toNotNull())
                    }
                    Toast.makeText(context, "Expense Name: $_expenseName successfully added", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            openExpenseTypes = false
        }


        BasicTextFieldAlertDialog(
            openDialog = openExpenseTypes,
            title = AddExpenseType,
            textContent = emptyString,
            placeholder = ExpenseTypePlaceholder,
            label = SelectExpenseType,
            icon = R.drawable.ic_expense_type,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = ExpenseTypeNotAdded,
            confirmedUpdatedToastText = null,
            getValue = { _expenseType ->
                val newExpenseType = ExpenseType(_expenseType.trim())
                val expenseTypes = expenseTypesJson.toExpenseTypes()
                val mutableExpenseTypes = mutableListOf<ExpenseType>()
                if (_expenseType.isBlank()){
                    Toast.makeText(context, SelectExpenseType, Toast.LENGTH_LONG).show()
                    openExpenseTypes = false
                }
                else if (expenseTypes.map { it.expenseType.trim().lowercase(Locale.ROOT) }.contains(_expenseType.trim().lowercase(Locale.ROOT))) {
                    Toast.makeText(context, "Expense type: $_expenseType already exists", Toast.LENGTH_LONG).show()
                    openExpenseTypes = false
                } else {
                    mutableExpenseTypes.addAll(expenseTypes)
                    mutableExpenseTypes.add(newExpenseType)
                    expenseTypesJson = mutableExpenseTypes.toSet().sortedBy { it.expenseType.first() }.toExpenseTypesJson()
                    coroutineScope.launch {
                        UserPreferences(context).saveExpenseTypes(expenseTypesJson.toNotNull())
                    }
                    Toast.makeText(context, "Expense Type: $_expenseType successfully added", Toast.LENGTH_LONG).show()
                    openExpenseTypes = false
                }
            }
        ) {
            openExpenseTypes = false
        }

    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfo,
        isLoading = isSavingExpense,
        title = null,
        textContent = savingExpenseResponse.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (expenseIsSaved){
            navigateBack()
        }
        confirmationInfo = false
    }
}
