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
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.withdrawal.ViewWithdrawalContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BankAccountViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.WithdrawalViewModel
import java.util.*


@Composable
fun ViewWithdrawalScreen(
    withdrawalViewModel: WithdrawalViewModel,
    bankAccountViewModel: BankAccountViewModel = hiltViewModel(),
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniqueWithdrawalId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        withdrawalViewModel.getWithdrawal(uniqueWithdrawalId)
        personnelViewModel.getAllPersonnel()
        bankAccountViewModel.getAllBanks()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Withdrawal") {
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
            val bankAccountName = allBankAccounts.firstOrNull { it.uniqueBankAccountId == withdrawalViewModel.withdrawalInfo.uniqueBankAccountId }?.bankAccountName.toNotNull()

            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == withdrawalViewModel.withdrawalInfo.uniquePersonnelId }
            val personnelName = "${personnel?.firstName.toNotNull()} ${personnel?.lastName.toNotNull()} ${personnel?.otherNames.toNotNull()}"

            ViewWithdrawalContent(
                withdrawal = withdrawalViewModel.withdrawalInfo,
                currency = "GHS",
                personnelName = personnelName,
                bankAccountName = bankAccountName,
                isUpdatingWithdrawal = withdrawalViewModel.updateWithdrawalState.value.isLoading,
                withdrawalUpdateMessage = withdrawalViewModel.updateWithdrawalState.value.message,
                withdrawalUpdatingIsSuccessful = withdrawalViewModel.updateWithdrawalState.value.isSuccessful,
                getUpdatedWithdrawalDate = {_date->
                    val date = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    withdrawalViewModel.updateWithdrawalDate(date, dayOfWeek)
                },
                getUpdatedTransactionId = {id->
                    withdrawalViewModel.updateTransactionId(id)
                },
                getUpdatedWithdrawalAmount = {_amount->
                    withdrawalViewModel.updateWithdrawalAmount(_amount)
                },
                getUpdatedWithdrawalShortNotes = {_shortNotes->
                    withdrawalViewModel.updateOtherInfo(_shortNotes)
                },
                updateWithdrawal = {_withdrawal->
                    withdrawalViewModel.updateWithdrawal(_withdrawal)
                }
            ) {
                navigateBack()
            }
        }
    }
}
