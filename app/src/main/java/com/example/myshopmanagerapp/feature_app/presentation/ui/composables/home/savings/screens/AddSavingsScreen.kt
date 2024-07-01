package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.savings.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.savings.AddSavingsContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SavingsViewModel
import java.util.*


@Composable
fun AddSavingsScreen(
    savingsViewModel: SavingsViewModel,
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateToAddBankScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        bankAccountViewModel.getAllBanks()
        personnelViewModel.getAllPersonnel()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Savings") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allBanks = bankAccountViewModel.bankAccountEntitiesState.value.bankAccountEntities ?: emptyList()
            val mapOfBanks = mutableMapOf<String, String>()
            allBanks.forEach { bank->
                mapOfBanks[bank.bankAccountName] = bank.uniqueBankAccountId
            }
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val mapOfPersonnel = mutableMapOf<String, String>()
            allPersonnel.forEach {personnel->
                mapOfPersonnel["${personnel.lastName} ${personnel.otherNames.toNotNull()} ${personnel.firstName}"] = personnel.uniquePersonnelId
            }

            AddSavingsContent(
                savings = savingsViewModel.addSavingsInfo,
                isInsertingSavings = savingsViewModel.addSavingsState.value.isLoading,
                savingsInsertingIsSuccessful = savingsViewModel.addSavingsState.value.isSuccessful,
                savingsInsertingMessage = savingsViewModel.addSavingsState.value.message,
                mapOfBanks = mapOfBanks,
                addSavingsDate = {_dateString->
                    val date = _dateString.toLocalDate().toDate().time
                    val dayOfWeek = _dateString.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { _dayOfWeek -> if (_dayOfWeek.isLowerCase()) _dayOfWeek.titlecase(Locale.ROOT) else _dayOfWeek.toString() }
                    savingsViewModel.addSavingsDate(date, dayOfWeek) },
                addSusuCollector = { value -> savingsViewModel.addBankPersonnelName(value) },
                addUniqueBankId = { value -> savingsViewModel.addUniqueBankId(value) },
                addSavingsAmount = { value -> savingsViewModel.addSavingsAmount(value) },
                addShortNotes = {value -> savingsViewModel.addOtherInfo(value)},
                addBank = { navigateToAddBankScreen() },
                addSavings = {savingsEntity -> savingsViewModel.addSavings(savingsEntity) }
            ) {
                navigateBack()
            }
        }
    }
}
