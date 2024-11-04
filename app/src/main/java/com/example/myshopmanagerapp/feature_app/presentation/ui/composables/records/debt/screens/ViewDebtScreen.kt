package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.shortDateFormatter
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewDebtScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.ViewDebtContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtRepaymentViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import java.time.LocalDate
import java.util.*


@Composable
fun ViewDebtScreen(
    debtViewModel: DebtViewModel,
    debtRepaymentViewModel: DebtRepaymentViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniqueDebtId: String,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value.toNotNull().ifBlank { GHS }
    LaunchedEffect(Unit) {
        debtViewModel.getAllDebt()
        debtRepaymentViewModel.getAllDebtRepayment()
        debtViewModel.getDebt(uniqueDebtId)
        personnelViewModel.getAllPersonnel()
        customerViewModel.getAllCustomers()
    }
    var openDeleteConfirmationDialog by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            ViewDebtScreenTopBar(topBarTitleText = "View Debt",
                deleteDebt = { openDeleteConfirmationDialog = !openDeleteConfirmationDialog }
            ) { navigateBack() }
        }
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allDebts = debtViewModel.debtEntitiesState.value.debtEntities ?: emptyList()
            val allDebtRepayments = debtRepaymentViewModel.debtRepaymentEntitiesState.value.debtRepaymentEntities ?: emptyList()
            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
            val customer = allCustomers.firstOrNull{ it.uniqueCustomerId == debtViewModel.debtInfo.uniqueCustomerId }
            val customerName = customer?.customerName.toNotNull()
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == debtViewModel.debtInfo.uniquePersonnelId }
            val personnelName = "${personnel?.firstName.toNotNull()} ${personnel?.lastName.toNotNull()} ${personnel?.otherNames.toNotNull()}"
            val customerDebts = allDebts.filter { it.uniqueCustomerId == debtViewModel.debtInfo.uniqueCustomerId }
            val customerDebtRepayments = allDebtRepayments.filter { it.uniqueCustomerId == debtViewModel.debtInfo.uniqueCustomerId }

            ViewDebtContent(
                allDebts = customerDebts,
                allDebtRepayments = customerDebtRepayments,
                debt = debtViewModel.debtInfo,
                currency = currency,
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
    DeleteConfirmationDialog(
        openDialog = openDeleteConfirmationDialog,
        title = "Delete Debt",
        textContent = "Are you sure you want to delete this debt?",
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            debtViewModel.deleteDebt(uniqueDebtId)
            confirmationInfoDialog = !confirmationInfoDialog
        }) {
        openDeleteConfirmationDialog = false
    }
    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = debtViewModel.deleteDebtState.value.isLoading,
        title = null,
        textContent = debtViewModel.deleteDebtState.value.message.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        confirmationInfoDialog = !confirmationInfoDialog
    }
}
