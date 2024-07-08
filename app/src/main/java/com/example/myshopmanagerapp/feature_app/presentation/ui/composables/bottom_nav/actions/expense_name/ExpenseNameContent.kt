package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.expense_name

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.Delete
import com.example.myshopmanagerapp.core.Constants.Edit
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseNames
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseNamesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseName
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch


@Composable
fun ExpenseNameContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val expenseNamesJson = userPreferences.getExpenseNames.collectAsState(initial = emptyString).value

    var openExpenseNames by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedExpenseName by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val expenseNames = expenseNamesJson.toExpenseNames()
            expenseNames.forEachIndexed { index, expenseName ->
                CategoryCard(number = "${index.plus(1)}",
                    name = expenseName.expenseName,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openExpenseNames = !openExpenseNames
                                selectedExpenseName = expenseName.expenseName
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedExpenseName = expenseName.expenseName
                            }
                            else -> {
                                selectedExpenseName = expenseName.expenseName
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openExpenseNames,
                    title = "Edit Expense Name",
                    textContent = emptyString,
                    placeholder = "Eg: Operational Expenses",
                    label = "Add expense route",
                    icon = R.drawable.ic_expense,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = "Expense route not edited",
                    confirmedUpdatedToastText = "Successfully changed",
                    getValue = { _expenseName ->
                        val editedExpenseName = ExpenseName(_expenseName.trim())
                        val mutableExpenseNames = mutableListOf<ExpenseName>()
                        mutableExpenseNames.addAll(expenseNames)
                        if (mutableExpenseNames.remove(ExpenseName(selectedExpenseName.trim()))) {
                            mutableExpenseNames.add(editedExpenseName)
                            val mutableExpenseNameJson =
                                mutableExpenseNames.sortedBy { it.expenseName.first() }.toSet().toList().toExpenseNamesJson()
                            coroutineScope.launch {
                                userPreferences.saveExpenseNames(mutableExpenseNameJson)
                            }
                            Toast.makeText(
                                context,
                                "Expense route successfully edited",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    openExpenseNames = false
                }
                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Expense Name",
                    textContent = "Are you sure you want to remove this expense route?",
                    unconfirmedDeletedToastText = "Did not remove expense route",
                    confirmedDeleteToastText = "Expense route deleted successfully",
                    confirmDelete = {
                        val thisExpenseName = ExpenseName(selectedExpenseName)
                        val mutableExpenseNames = mutableListOf<ExpenseName>()
                        mutableExpenseNames.addAll(expenseNames)
                        val deletedMutableExpenseNames = mutableExpenseNames.minus(thisExpenseName)
                        val mutableExpenseNamesJson =
                            deletedMutableExpenseNames.sortedBy { it.expenseName.first() }
                                .toExpenseNamesJson()
                        coroutineScope.launch {
                            userPreferences.saveExpenseNames(mutableExpenseNamesJson)
                        }
                        Toast.makeText(
                            context,
                            "Expense route successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
