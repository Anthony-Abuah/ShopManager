package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.withdrawal.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.FormRelatedString.AddWithdrawal
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.withdrawal.AddWithdrawalContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.WithdrawalViewModel
import java.util.*


@Composable
fun AddWithdrawalScreen(
    withdrawalViewModel: WithdrawalViewModel,
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateToAddBankScreen: () -> Unit,
    navigateToAddPersonnelScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        bankAccountViewModel.getAllBanks()
        personnelViewModel.getAllPersonnel()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = AddWithdrawal) {
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

            AddWithdrawalContent(
                withdrawal = withdrawalViewModel.addWithdrawalInfo,
                isInsertingWithdrawal = withdrawalViewModel.addWithdrawalState.value.isLoading,
                withdrawalInsertingIsSuccessful = withdrawalViewModel.addWithdrawalState.value.isSuccessful,
                withdrawalInsertingMessage = withdrawalViewModel.addWithdrawalState.value.message,
                mapOfBanks = mapOfBanks,
                addWithdrawalDate = {_dateString->
                    val date = _dateString.toLocalDate().toDate().time
                    val dayOfWeek = _dateString.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    withdrawalViewModel.addWithdrawalDate(date, dayOfWeek)
                },
                addTransactionId = { value -> withdrawalViewModel.addTransactionId(value) },
                addUniqueBankAccountId = { value -> withdrawalViewModel.addUniqueBankId(value) },
                addWithdrawalAmount = { value -> withdrawalViewModel.addWithdrawalAmount(value) },
                addShortNotes = {value -> withdrawalViewModel.addOtherInfo(value)},
                addBank = { navigateToAddBankScreen() },
                addWithdrawal = {withdrawalEntity -> withdrawalViewModel.addWithdrawal(withdrawalEntity) }
            ) {
                navigateBack()
            }
        }
    }
}
