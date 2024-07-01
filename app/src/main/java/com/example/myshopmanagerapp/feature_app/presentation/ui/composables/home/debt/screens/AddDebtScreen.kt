package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt.AddDebtContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtViewModel
import java.time.LocalDate
import java.util.*


@Composable
fun AddDebtScreen(
    debtViewModel: DebtViewModel,
    customerViewModel: CustomerViewModel = hiltViewModel(),
    navigateToAddCustomerScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        customerViewModel.getAllCustomers()
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Debt") {
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

            AddDebtContent(
                debt = debtViewModel.addDebtInfo,
                mapOfCustomers = mapOfCustomers,
                isSavingDebt = debtViewModel.addDebtState.value.isLoading,
                debtSavingIsSuccessful = debtViewModel.addDebtState.value.isSuccessful,
                debtSavingMessage = debtViewModel.addDebtState.value.message,
                addCustomer = { navigateToAddCustomerScreen() },
                addDebtAmount = {_debtAmount->
                    debtViewModel.addDebtAmount(convertToDouble(_debtAmount))
                },
                addUniqueCustomerId = {_customerId->
                    debtViewModel.addDebtCustomer(_customerId)
                },
                addDateString = {_dateString->
                    val localDate = LocalDate.parse(_dateString, shortDateFormatter)
                    val newDate = localDate.toDate().time
                    debtViewModel.addDebtDate(newDate)
                    val day = localDate.dayOfWeek.toString().lowercase().replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    debtViewModel.addDebtDayOfTheWeek(day)
                },
                addShortDescription = {_shortDescription->
                    debtViewModel.addDebtShortNotes(_shortDescription)
                },
                addDebt = {_debt->
                    debtViewModel.addDebt(_debt)
                }
            ) {
                navigateBack()
            }
        }
    }
}
