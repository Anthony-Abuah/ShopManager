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
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt_repayment.ViewDebtRepaymentContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtRepaymentViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import java.time.LocalDate
import java.util.*


@Composable
fun ViewDebtRepaymentScreen(
    debtRepaymentViewModel: DebtRepaymentViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniqueDebtRepaymentId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        debtRepaymentViewModel.getDebtRepayment(uniqueDebtRepaymentId)
        personnelViewModel.getAllPersonnel()
        customerViewModel.getAllCustomers()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Debt Repayment") { navigateBack() }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
            val customer = allCustomers.firstOrNull{ it.uniqueCustomerId == debtRepaymentViewModel.debtRepaymentInfo.uniqueCustomerId }
            val customerName = customer?.customerName.toNotNull()
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == debtRepaymentViewModel.debtRepaymentInfo.uniquePersonnelId }
            val personnelName = "${personnel?.firstName.toNotNull()} ${personnel?.lastName.toNotNull()} ${personnel?.otherNames.toNotNull()}"

            ViewDebtRepaymentContent(
                debtRepayment = debtRepaymentViewModel.debtRepaymentInfo,
                currency = "GHS",
                debtRepaymentUpdatingIsSuccessful = debtRepaymentViewModel.updateDebtRepaymentState.value.isSuccessful,
                debtRepaymentUpdatingMessage = debtRepaymentViewModel.updateDebtRepaymentState.value.message,
                isUpdatingDebtRepayment = debtRepaymentViewModel.updateDebtRepaymentState.value.isLoading,
                customerName = customerName,
                personnelName = personnelName,
                getUpdatedDebtRepaymentDate = {_date->
                    val localDate = LocalDate.parse(_date, shortDateFormatter)
                    val dayOfWeek = localDate.dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                        else it.toString() }
                    val newDate = localDate.toDate().time
                    debtRepaymentViewModel.updateDebtRepaymentDate(newDate, dayOfWeek)
                },
                getUpdatedDebtRepaymentAmount = {_amount->
                    debtRepaymentViewModel.updateDebtRepaymentAmount(convertToDouble(_amount))
                },
                getUpdatedDebtRepaymentShortNotes = {_shortNotes->
                    debtRepaymentViewModel.updateDebtRepaymentShortNotes(_shortNotes)
                },
                updateDebtRepayment = {_debtRepayment->
                    debtRepaymentViewModel.updateDebtRepayment(_debtRepayment)
                },
            ) { navigateBack() }
        }
    }
}
