package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses.screens

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
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses.ViewExpenseContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.ExpenseViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import java.util.*


@Composable
fun ViewExpenseScreen(
    expenseViewModel: ExpenseViewModel,
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniqueExpenseId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect( Unit ){
        expenseViewModel.getExpense(uniqueExpenseId)
        personnelViewModel.getAllPersonnel()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Expense") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == expenseViewModel.expenseInfo.uniquePersonnelId }
            val personnelName = "${personnel?.firstName.toNotNull()} ${personnel?.lastName.toNotNull()} ${personnel?.otherNames.toNotNull()}"

            ViewExpenseContent(
                expense = expenseViewModel.expenseInfo,
                expenseUpdateMessage = expenseViewModel.updateExpenseState.value.message,
                expenseUpdatingIsSuccessful = expenseViewModel.updateExpenseState.value.isSuccessful,
                isUpdatingExpense = expenseViewModel.updateExpenseState.value.isLoading,
                personnelName = personnelName,
                currency = "GHS",
                getUpdatedExpenseDate = {_date->
                    val date = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    expenseViewModel.updateExpenseDate(date, dayOfWeek)
                },
                getUpdatedExpenseName = {_expenseName->
                    expenseViewModel.updateExpenseName(_expenseName)
                },
                getUpdatedExpenseType = {_expenseType->
                    expenseViewModel.updateExpenseType(_expenseType)
                },
                getUpdatedExpenseAmount = {_amount->
                    expenseViewModel.updateExpenseAmount(convertToDouble(_amount))
                },
                getUpdatedExpenseOtherInfo = {_otherInfo->
                    expenseViewModel.updateExpenseOtherInfo(_otherInfo)
                },
                updateExpense = {_expenseEntity->
                    expenseViewModel.updateExpense(_expenseEntity)
                }
            ) {
                navigateBack()
            }
        }
    }
}
