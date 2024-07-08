package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.ExpenseEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ExpenseListContent(
    allExpenses: ExpenseEntities?,
    navigateToViewExpenseScreen: (String) -> Unit,
    onConfirmDelete: (String) -> Unit,
) {

    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueExpenseId by remember {
        mutableStateOf(emptyString)
    }

    if (allExpenses.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No expenses to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        BasicScreenColumnWithoutBottomBar {
            allExpenses.forEachIndexed { index,expense ->
                if (index == 0){ HorizontalDivider() }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    ExpenseCard(
                        expense = expense,
                        currency = "GHS",
                        number = index.plus(1).toString(),
                        onDelete = {
                            uniqueExpenseId = expense.uniqueExpenseId
                            openDeleteConfirmation = !openDeleteConfirmation
                        }
                    ) {
                        navigateToViewExpenseScreen(expense.uniqueExpenseId)
                    }
                }
                HorizontalDivider()
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Expense",
            textContent = "Are your sure you want to permanently delete this expense",
            unconfirmedDeletedToastText = "Expense not deleted",
            confirmedDeleteToastText = "Expense has been successfully removed",
            confirmDelete = {
                onConfirmDelete(uniqueExpenseId)
            }
        ) {
            openDeleteConfirmation = false
        }
    }

}
