package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
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
import com.example.myshopmanagerapp.core.Functions.toDoubleDecimalPlaces
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
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

    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ){
        HorizontalDivider(modifier = Modifier.padding(bottom = LocalSpacing.current.small), thickness = LocalSpacing.current.borderStroke)

        BasicScreenColumnWithoutBottomBar {
            var expenseNamesJson =
                UserPreferences(context).getExpenseNames.collectAsState(initial = null).value
            var expenseTypesJson =
                UserPreferences(context).getExpenseTypes.collectAsState(initial = null).value

            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.expense,
                    firstText = UniqueExpenseId,
                    secondText = expense.uniqueExpenseId,
                    readOnly = true
                )
            }

            // Date
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.ic_date,
                    firstText = Date,
                    secondText = "${expense.dayOfWeek}, ${expense.date.toDate().toDateString()}",
                    readOnly = false,
                    isDate = true,
                    value = "${expense.dayOfWeek}, ${expense.date.toDate().toDateString()}",
                    getUpdatedValue = { getUpdatedExpenseDate(it) }
                )
            }

            // Day Of The Week
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.date,
                    firstText = DayOfTheWeek,
                    secondText = expense.dayOfWeek.toNotNull(),
                    readOnly = true,
                )
            }

            // Expense Name
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.ic_expense,
                    firstText = ExpenseName,
                    readOnly = false,
                    secondText = expense.expenseName,
                    value = expense.expenseName,
                    label = EnterExpenseName,
                    placeholder = ExpenseNamePlaceholder,
                    isAutoCompleteTextField = true,
                    listItems = expenseNamesJson.toExpenseNames().map { it.expenseName },
                    addNewItem = { openExpenseNames = !openExpenseNames },
                    expandedIcon = R.drawable.ic_arrow_up,
                    unexpandedIcon = R.drawable.ic_arrow_down,
                    getUpdatedValue = { getUpdatedExpenseName(it) }
                )
            }

            // Expense Type
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.expense_type,
                    firstText = ExpenseType,
                    secondText = expense.expenseType,
                    value = expense.expenseType,
                    label = SelectExpenseType,
                    placeholder = ExpenseTypePlaceholder,
                    listItems = expenseTypesJson.toExpenseTypes().map { it.expenseType },
                    addNewItem = { openExpenseTypes = !openExpenseTypes },
                    expandedIcon = R.drawable.ic_arrow_up,
                    unexpandedIcon = R.drawable.ic_arrow_down,
                    readOnly = false,
                    isAutoCompleteTextField = true,
                    getUpdatedValue = { getUpdatedExpenseType(it) }
                )
            }

            // Expense Amount
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.ic_money_filled,
                    firstText = ExpenseAmount,
                    secondText = "$currency ${expense.expenseAmount.toDoubleDecimalPlaces()}",
                    value = expense.expenseAmount.toTwoDecimalPlaces().toString(),
                    placeholder = ExpenseAmountPlaceholder,
                    label = EnterExpenseAmount,
                    textFieldIcon = R.drawable.ic_money_filled,
                    readOnly = false,
                    keyboardType = KeyboardType.Number,
                    getUpdatedValue = { getUpdatedExpenseAmount(it) }
                )
            }

            // Expense Personnel
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    firstText = Personnel_Name,
                    secondText = personnelName,
                    readOnly = true
                )
            }


            // Expense Short Notes
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                VerticalDisplayAndEditTextValues(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    innerHorizontalPadding = LocalSpacing.current.small,
                    innerVerticalPadding = LocalSpacing.current.small,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    elevation = LocalSpacing.current.default,
                    leadingIcon = R.drawable.ic_short_notes,
                    firstText = ShortNotes,
                    secondText = expense.otherInfo.toNotNull(),
                    value = expense.otherInfo.toNotNull(),
                    label = EnterShortDescription,
                    placeholder = ExpenseShortNotesPlaceholder,
                    textFieldIcon = R.drawable.ic_short_notes,
                    readOnly = false,
                    getUpdatedValue = { getUpdatedExpenseOtherInfo(it) }
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.small
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalSpacing.current.buttonHeight)
                        .padding(LocalSpacing.current.small)
                        .clickable {
                            when (true) {
                                (expense.expenseAmount < 0.1) -> {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please enter valid amount",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                (expense.expenseName.isBlank()) -> {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please enter expense route",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                else -> {
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
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.default),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = UpdateChanges,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

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
                    if (_expenseName.isBlank()) {
                        Toast.makeText(context, EnterExpenseName, Toast.LENGTH_LONG).show()
                        openExpenseNames = false
                    } else if (expenseNames.map { it.expenseName.trim().lowercase(Locale.ROOT) }
                            .contains(_expenseName.trim().lowercase(Locale.ROOT))) {
                        Toast.makeText(
                            context,
                            "Expense type: $_expenseName already exists",
                            Toast.LENGTH_LONG
                        ).show()
                        openExpenseNames = false
                    } else {
                        mutableExpenseNames.addAll(expenseNames)
                        mutableExpenseNames.add(newExpenseName)
                        expenseNamesJson =
                            mutableExpenseNames.toSet().sortedBy { it.expenseName.first() }
                                .toExpenseNamesJson()
                        coroutineScope.launch {
                            UserPreferences(context).saveExpenseNames(expenseNamesJson.toNotNull())
                        }
                        Toast.makeText(
                            context,
                            "Expense Name: $_expenseName successfully added",
                            Toast.LENGTH_LONG
                        ).show()
                        openExpenseNames = false
                    }
                }
            ) {
                openExpenseNames = false
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
                    if (_expenseType.isBlank()) {
                        Toast.makeText(context, SelectExpenseType, Toast.LENGTH_LONG).show()
                        openExpenseTypes = false
                    } else if (expenseTypes.map { it.expenseType.trim().lowercase(Locale.ROOT) }
                            .contains(_expenseType.trim().lowercase(Locale.ROOT))) {
                        Toast.makeText(
                            context,
                            "Expense type: $_expenseType already exists",
                            Toast.LENGTH_LONG
                        ).show()
                        openExpenseTypes = false
                    } else {
                        mutableExpenseTypes.addAll(expenseTypes)
                        mutableExpenseTypes.add(newExpenseType)
                        expenseTypesJson =
                            mutableExpenseTypes.toSet().sortedBy { it.expenseType.first() }
                                .toExpenseTypesJson()
                        coroutineScope.launch {
                            UserPreferences(context).saveExpenseTypes(expenseTypesJson.toNotNull())
                        }
                        Toast.makeText(
                            context,
                            "Expense Type: $_expenseType successfully added",
                            Toast.LENGTH_LONG
                        ).show()
                        openExpenseTypes = false
                    }
                }
            ) {
                openExpenseTypes = false
            }
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
