package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.savings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.savings.ViewSavingsContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SavingsViewModel
import java.util.*


@Composable
fun ViewSavingsScreen(
    savingsViewModel: SavingsViewModel,
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniqueSavingsId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        savingsViewModel.getSavings(uniqueSavingsId)
        personnelViewModel.getAllPersonnel()
        bankAccountViewModel.getAllBanks()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Savings") {
                navigateBack()
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allBankAccounts = bankAccountViewModel.bankAccountEntitiesState.value.bankAccountEntities ?: emptyList()
            val bankAccountName = allBankAccounts.firstOrNull { it.uniqueBankAccountId == savingsViewModel.savingsInfo.uniqueBankAccountId }?.bankAccountName.toNotNull()

            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == savingsViewModel.savingsInfo.uniquePersonnelId }
            val personnelName = "${personnel?.firstName.toNotNull()} ${personnel?.lastName.toNotNull()} ${personnel?.otherNames.toNotNull()}"


            ViewSavingsContent(
                savings = savingsViewModel.savingsInfo,
                personnelName = personnelName,
                bankAccountName = bankAccountName,
                currency = "GHS",
                isUpdatingSavings = savingsViewModel.updateSavingsState.value.isLoading,
                savingsUpdateMessage = savingsViewModel.updateSavingsState.value.message,
                savingsUpdatingIsSuccessful = savingsViewModel.updateSavingsState.value.isSuccessful,
                getUpdatedSavingsDate = {_date->
                    val date = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    savingsViewModel.updateSavingsDate(date, dayOfWeek)
                },
                getUpdatedBankPersonnelName = { name->
                    savingsViewModel.updateBankPersonnelName(name)
                },
                getUpdatedSavingsAmount = {_amount->
                    savingsViewModel.updateSavingsAmount(_amount)
                },
                getUpdatedShortNotes = { _shortNotes->
                    savingsViewModel.updateOtherInfo(_shortNotes)
                },
                updateSavings = {_savings->
                    savingsViewModel.updateSavings(_savings)
                }
            ) {
                navigateBack()
            }
        }
    }
}
