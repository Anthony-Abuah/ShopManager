package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.screens

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
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.ViewDebtContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import java.time.LocalDate
import java.util.*


@Composable
fun ViewDebtScreen(
    debtViewModel: DebtViewModel,
    customerViewModel: CustomerViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniqueDebtId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        debtViewModel.getDebt(uniqueDebtId)
        personnelViewModel.getAllPersonnel()
        customerViewModel.getAllCustomers()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Debt") {
                navigateBack()
            }
        }
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
            val customer = allCustomers.firstOrNull{ it.uniqueCustomerId == debtViewModel.debtInfo.uniqueCustomerId }
            val customerName = customer?.customerName.toNotNull()
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == debtViewModel.debtInfo.uniquePersonnelId }
            val personnelName = "${personnel?.firstName.toNotNull()} ${personnel?.lastName.toNotNull()} ${personnel?.otherNames.toNotNull()}"

            ViewDebtContent(
                debt = debtViewModel.debtInfo,
                currency = "GHS",
                isUpdatingDebt = debtViewModel.updateDebtState.value.isLoading,
                debtUpdateMessage = debtViewModel.updateDebtState.value.message,
                debtUpdatingIsSuccessful = debtViewModel.updateDebtState.value.isSuccessful,
                customerName = customerName,
                personnelName = personnelName,
                getUpdatedDebtDate = {_date->
                    val localDate = LocalDate.parse(_date, shortDateFormatter)
                    val longDate = localDate.toDate().time
                    val dayOfWeek = localDate.dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    debtViewModel.updateDebtDate(longDate)
                    debtViewModel.updateDebtDayOfTheWeek(dayOfWeek)
                },
                getUpdatedDebtAmount = {_amount->
                    debtViewModel.updateDebtAmount(convertToDouble(_amount))
                },
                getUpdatedDebtShortNotes = {_shortNotes->
                    debtViewModel.updateDebtShortNotes(_shortNotes)
                },
                updateDebt = {_debt->
                    debtViewModel.updateDebt(_debt)
                }
            ) {
                navigateBack()
            }
        }
    }
}
