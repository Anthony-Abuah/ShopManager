package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.withdrawal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.WithdrawalEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun WithdrawalListContent(
    allWithdrawal: WithdrawalEntities,
    isDeletingWithdrawal: Boolean,
    withdrawalDeletionMessage: String?,
    withdrawalDeletionIsSuccessful: Boolean,
    getBankName: (String) -> String,
    getPersonnelName: (String) -> String,
    reloadAllWithdrawal: () -> Unit,
    onConfirmDelete: (String) -> Unit,
    navigateToViewWithdrawalScreen: (String) -> Unit
) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueWithdrawalId by remember {
        mutableStateOf(emptyString)
    }

    if (allWithdrawal.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No withdrawals to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        BasicScreenColumnWithoutBottomBar {
            allWithdrawal.forEachIndexed { index, withdrawal ->
                val bankName = getBankName(withdrawal.uniqueBankAccountId)
                val personnelName = getPersonnelName(withdrawal.uniquePersonnelId)
                if (index == 0){
                    HorizontalDivider()
                }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    WithdrawalCard(
                        withdrawal = withdrawal,
                        number = index.plus(1).toString(),
                        accountName = bankName,
                        currency = "GHS",
                        onDelete = {
                            uniqueWithdrawalId = withdrawal.uniqueWithdrawalId
                            openDeleteConfirmation = !openDeleteConfirmation }) {
                        navigateToViewWithdrawalScreen(withdrawal.uniqueWithdrawalId)
                    }
                }
                HorizontalDivider()
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Withdrawal",
            textContent = "Are you sure you want to permanently delete this withdrawal",
            unconfirmedDeletedToastText = "Withdrawal not deleted",
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueWithdrawalId)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isDeletingWithdrawal,
        title = null,
        textContent = withdrawalDeletionMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (withdrawalDeletionIsSuccessful){
            reloadAllWithdrawal()
        }
        confirmationInfoDialog = false
    }
}
