package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.expense_type.screen

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
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseTypes
import com.example.myshopmanagerapp.core.TypeConverters.toExpenseTypesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseName
import com.example.myshopmanagerapp.feature_app.domain.model.ExpenseType
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.expense_type.ExpenseTypeContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun ExpenseTypeScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openExpenseTypes by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Expense Type") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openExpenseTypes = !openExpenseTypes
            }
        }
    ){
        val expenseTypesJson = userPreferences.getExpenseTypes.collectAsState(initial = emptyString).value
        val expenseTypes = expenseTypesJson.toExpenseTypes()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ExpenseTypeContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openExpenseTypes,
            title = "Add Expense Type",
            textContent = emptyString,
            placeholder = "Eg: Operational Expenses",
            label = "Add expense type",
            icon = R.drawable.ic_expense_type,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Expense type not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _expenseType ->
                val mutableExpenseTypes = mutableListOf<ExpenseType>()
                mutableExpenseTypes.addAll(expenseTypes)
                val newExpenseType = ExpenseType(_expenseType.trim())
                val newMutableExpenseType = mutableExpenseTypes.plus(newExpenseType)
                val newMutableExpenseTypeJson = newMutableExpenseType.sortedBy { it.expenseType.first() }.toSet().toList().toExpenseTypesJson()
                coroutineScope.launch {
                    userPreferences.saveExpenseTypes(newMutableExpenseTypeJson)
                }
                Toast.makeText(context,"Expense type successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openExpenseTypes = false
        }
    }
}
