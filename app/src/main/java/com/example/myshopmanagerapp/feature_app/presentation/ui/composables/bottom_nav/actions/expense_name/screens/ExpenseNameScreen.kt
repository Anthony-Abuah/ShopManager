package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.expense_name.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseNames
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseNamesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseName
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.expense_name.ExpenseNameContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun ExpenseNameScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openExpenseNames by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Expense Name") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openExpenseNames = !openExpenseNames
            }
        }
    ){
        val expenseNamesJson = userPreferences.getExpenseNames.collectAsState(initial = emptyString).value
        val expenseNames = expenseNamesJson.toExpenseNames()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ExpenseNameContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openExpenseNames,
            title = "Add Expense Name",
            textContent = emptyString,
            placeholder = "Eg: Transportation",
            label = "Add expense route",
            icon = R.drawable.ic_expense,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Expense route not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _expenseName ->
                val mutableExpenseNames = mutableListOf<ExpenseName>()
                mutableExpenseNames.addAll(expenseNames)
                val newExpenseName = ExpenseName(_expenseName.trim())
                val newMutableExpenseName = mutableExpenseNames.plus(newExpenseName)
                val newMutableExpenseNameJson = newMutableExpenseName.sortedBy { it.expenseName.first() }.toSet().toList().toExpenseNamesJson()
                coroutineScope.launch {
                    userPreferences.saveExpenseNames(newMutableExpenseNameJson)
                }
                Toast.makeText(context,"Expense route successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openExpenseNames = false
        }
    }
}
