package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.expenses

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseAmount
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseAmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseInformation
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseName
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseNameNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseType
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseTypeNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.ExpenseTypePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Personnel_Name
import com.example.myshopmanagerapp.core.FormRelatedString.SelectExpenseType
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueExpenseId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.toDate
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
fun ViewExpenseContent(
    expense: ExpenseEntity,
    expenseUpdateMessage: String?,
    expenseUpdatingIsSuccessful: Boolean,
    isUpdatingExpense: Boolean,
    currency: String,
    personnelName: String,
    getUpdatedExpenseDate: (String) -> Unit,
    getUpdatedExpenseName: (String) -> Unit,
    getUpdatedExpenseType: (String) -> Unit,
    getUpdatedExpenseAmount: (String) -> Unit,
    getUpdatedExpenseOtherInfo: (String) -> Unit,
    updateExpense: (ExpenseEntity) -> Unit,
    navigateBack: () -> Unit,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var openExpenseNames by remember {
        mutableStateOf(false)
    }
    var openExpenseTypes by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        var expenseNamesJson = UserPreferences(context).getExpenseNames.collectAsState(initial = null).value
        var expenseTypesJson = UserPreferences(context).getExpenseTypes.collectAsState(initial = null).value

        // Expense Photo
        ViewPhoto(icon = R.drawable.ic_money_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewInfo(info = ExpenseInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewTextValueRow(viewTitle = UniqueExpenseId, viewValue = expense.uniqueExpenseId)

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = Date,
            viewDateString = "${expense.dayOfWeek}, ${expense.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedExpenseDate(it) }
        )

        HorizontalDivider()

        // Day Of The Week
        ViewTextValueRow(viewTitle = DayOfTheWeek, viewValue = expense.dayOfWeek.toNotNull())

        HorizontalDivider()

        // Expense Name
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = ExpenseName,
            viewValue = expense.expenseName,
            placeholder = ExpenseNamePlaceholder,
            label = EnterExpenseName,
            readOnly = false,
            expandedIcon = R.drawable.ic_money_filled,
            unexpandedIcon = R.drawable.ic_money_outline,
            listItems = expenseNamesJson.toExpenseNames().map { it.expenseName },
            onClickAddButton = { openExpenseNames = !openExpenseNames },
            getUpdatedValue = { getUpdatedExpenseName(it) }
        )

        HorizontalDivider()

        // Expense Type
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = ExpenseType,
            viewValue = expense.expenseType,
            placeholder = ExpenseTypePlaceholder,
            label = SelectExpenseType,
            expandedIcon = R.drawable.ic_money_filled,
            unexpandedIcon = R.drawable.ic_money_outline,
            listItems = expenseTypesJson.toExpenseTypes().map { it.expenseType },
            onClickAddButton = { openExpenseTypes = !openExpenseTypes },
            getUpdatedValue = { getUpdatedExpenseType(it) }
        )

        HorizontalDivider()

        // Expense Amount
        ViewOrUpdateNumberValueRow(
            viewTitle = ExpenseAmount,
            viewValue = "$currency ${expense.expenseAmount}",
            placeholder = ExpenseAmountPlaceholder,
            label = EnterExpenseAmount,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = { getUpdatedExpenseAmount(it)}
        )

        HorizontalDivider()

        // Expense Personnel
        ViewTextValueRow(
            viewTitle = Personnel_Name,
            viewValue = personnelName
        )

        HorizontalDivider()

        // Expense Short Notes
        ViewOrUpdateTextValueRow(
            viewTitle = ShortNotes,
            viewValue = expense.otherInfo.toNotNull(),
            placeholder = ExpenseShortNotesPlaceholder,
            label = EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedExpenseOtherInfo(it)}
        )

        HorizontalDivider()

        Box(modifier = Modifier.padding(
            horizontal = LocalSpacing.current.smallMedium,
            vertical = LocalSpacing.current.large)){
            BasicButton(buttonName = UpdateChanges) {
                when(true){
                    (expense.expenseAmount < 0.1)->{
                        Toast.makeText(context, "Please enter valid amount", Toast.LENGTH_LONG).show()
                    }
                    (expense.expenseName.isBlank())->{
                        Toast.makeText(context, "Please enter expense route", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val expenseEntity = ExpenseEntity(
                            expenseId = expense.expenseId,
                            uniqueExpenseId = expense.uniqueExpenseId,
                            date = expense.date,
                            dayOfWeek = expense.dayOfWeek,
                            expenseName = expense.expenseName,
                            expenseAmount = expense.expenseAmount,
                            expenseType = expense.expenseType,
                            uniquePersonnelId = expense.uniquePersonnelId,
                            otherInfo = expense.otherInfo
                        )
                        updateExpense(expenseEntity)
                        confirmationInfoDialog = !confirmationInfoDialog
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
                }
            }
        ) {
            openExpenseTypes = false
        }
    }


    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingExpense,
        title = null,
        textContent = expenseUpdateMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (expenseUpdatingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
