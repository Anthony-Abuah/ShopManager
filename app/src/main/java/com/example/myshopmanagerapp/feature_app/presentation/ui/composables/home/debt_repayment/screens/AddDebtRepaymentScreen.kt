package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt_repayment.screens

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
import com.example.myshopmanagerapp.core.Functions.shortDateFormatter
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt_repayment.AddDebtRepaymentContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtRepaymentViewModel
import java.time.LocalDate
import java.util.*


@Composable
fun AddDebtRepaymentScreen(
    debtRepaymentViewModel: DebtRepaymentViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        customerViewModel.getAllCustomers()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Debt Repayment") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
            val mapOfCustomers = mutableMapOf<String, String>()
            allCustomers.forEach {customer->
                mapOfCustomers[customer.customerName] = customer.uniqueCustomerId
            }

            AddDebtRepaymentContent(
                debtRepayment = debtRepaymentViewModel.addDebtRepaymentInfo,
                isSavingDebtRepayment = debtRepaymentViewModel.addDebtRepaymentState.value.isLoading,
                debtRepaymentSavingIsSuccessful = debtRepaymentViewModel.addDebtRepaymentState.value.isSuccessful,
                debtRepaymentSavingMessage = debtRepaymentViewModel.addDebtRepaymentState.value.message,
                mapOfCustomers = mapOfCustomers,
                addDebtRepaymentDate = {_dateString->
                    val localDate = LocalDate.parse(_dateString, shortDateFormatter)
                    val longDate = localDate.toDate().time
                    val dayOfWeek = localDate.dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                        else it.toString() }
                    debtRepaymentViewModel.addDebtRepaymentDate(longDate, dayOfWeek)
                },
                addCustomer = {value-> debtRepaymentViewModel.addDebtRepaymentCustomer(value)},
                addDebtRepaymentAmount = {value-> debtRepaymentViewModel.addDebtRepaymentAmount(convertToDouble(value))},
                addShortNotes = {value-> debtRepaymentViewModel.addDebtRepaymentShortNotes(value)},
                addDebtRepayment = {value-> debtRepaymentViewModel.addDebtRepayment(value)}
            ) { navigateBack() }
        }
    }
}
