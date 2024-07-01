package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.expenses.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.expenses.AddExpenseContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.ExpenseViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import java.util.*


@Composable
fun AddExpenseScreen(
    expenseViewModel: ExpenseViewModel,
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    LaunchedEffect( Unit ){
        personnelViewModel.getAllPersonnel()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Expense") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val expense = expenseViewModel.addExpenseInfo
            AddExpenseContent(
                expense = expense,
                isSavingExpense = expenseViewModel.addExpenseState.value.isLoading,
                expenseIsSaved = expenseViewModel.addExpenseState.value.isSuccessful,
                savingExpenseResponse = expenseViewModel.addExpenseState.value.message,
                addExpenseDate = {_date->
                    val newDate = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    expenseViewModel.addExpenseDate(newDate,dayOfWeek)
                },
                addExpenseName = { _expenseName->
                    expenseViewModel.addExpenseName(_expenseName)
                },
                addExpenseType = { _expenseType->
                    expenseViewModel.addExpenseType(_expenseType)
                },
                addExpenseAmount = {_amount->
                    expenseViewModel.addExpenseAmount(convertToDouble(_amount) )
                },
                addExpenseOtherInfo = {_shortNotes->
                    expenseViewModel.addExpenseOtherInfo(_shortNotes)
                },
                addExpense = {expenseEntity->
                    expenseViewModel.addExpense(expenseEntity)
                }
            ) {
                navigateBack()
            }
        }
    }
}
