package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.expense_type

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
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseTypes
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseTypesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseName
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseType
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch


@Composable
fun ExpenseTypeContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val expenseTypesJson = userPreferences.getExpenseTypes.collectAsState(initial = emptyString).value

    var openExpenseTypes by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedExpenseType by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val expenseTypes = expenseTypesJson.toExpenseTypes()
            expenseTypes.forEachIndexed { index, expenseType ->
                CategoryCard(number = "${index.plus(1)}",
                    name = expenseType.expenseType,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openExpenseTypes = !openExpenseTypes
                                selectedExpenseType = expenseType.expenseType
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedExpenseType = expenseType.expenseType
                            }
                            else -> {
                                selectedExpenseType = expenseType.expenseType
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openExpenseTypes,
                    title = "Edit Expense Name",
                    textContent = emptyString,
                    placeholder = "Eg: Operational Expenses",
                    label = "Add expense type",
                    icon = R.drawable.ic_expense_type,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = "Expense type not edited",
                    confirmedUpdatedToastText = "Successfully changed",
                    getValue = { _expenseType ->
                        val editedExpenseType = ExpenseType(_expenseType.trim())
                        val mutableExpenseTypes = mutableListOf<ExpenseType>()
                        mutableExpenseTypes.addAll(expenseTypes)
                        if (mutableExpenseTypes.remove(ExpenseType(selectedExpenseType.trim()))) {
                            mutableExpenseTypes.add(editedExpenseType)
                            val mutableExpenseTypeJson =
                                mutableExpenseTypes.sortedBy { it.expenseType }.toSet().toList().toExpenseTypesJson()
                            coroutineScope.launch {
                                userPreferences.saveExpenseTypes(mutableExpenseTypeJson)
                            }
                            Toast.makeText(
                                context,
                                "Expense type successfully edited",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    openExpenseTypes = false
                }
                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Expense Type",
                    textContent = "Are you sure you want to remove this expense type?",
                    unconfirmedDeletedToastText = "Did not remove expense type",
                    confirmedDeleteToastText = "Expense type deleted successfully",
                    confirmDelete = {
                        val thisExpenseType = ExpenseType(selectedExpenseType)
                        val mutableExpenseTypes = mutableListOf<ExpenseType>()
                        mutableExpenseTypes.addAll(expenseTypes)
                        val deletedMutableExpenseTypes = mutableExpenseTypes.minus(thisExpenseType)
                        val mutableExpenseTypesJson =
                            deletedMutableExpenseTypes.sortedBy { it.expenseType }
                                .toExpenseTypesJson()
                        coroutineScope.launch {
                            userPreferences.saveExpenseTypes(mutableExpenseTypesJson)
                        }
                        Toast.makeText(
                            context,
                            "Expense type successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
