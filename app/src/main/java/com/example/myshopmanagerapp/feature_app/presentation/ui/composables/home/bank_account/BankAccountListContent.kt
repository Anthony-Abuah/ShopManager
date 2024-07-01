package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.BankAccountEntities
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun BankAccountListContent(
    allBanks: BankAccountEntities?,
    isDeletingBank: Boolean,
    bankDeletingMessage: String?,
    bankDeletingIsSuccessful: Boolean,
    reloadBankInfo: () -> Unit,
    onConfirmDelete: (String) -> Unit,
    navigateToViewBankScreen: (String)-> Unit
) {

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueBankAccountId by remember {
        mutableStateOf(emptyString)
    }
    var bankAccountName by remember {
        mutableStateOf(emptyString)
    }
    
    if (allBanks.isNullOrEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No banks accounts to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    else{
        BasicScreenColumnWithoutBottomBar {
            allBanks.forEachIndexed { index, bankAccount ->
                if (index == 0){ HorizontalDivider() }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    BankAccountCard(
                        bankAccount = bankAccount,
                        currency = "GHS",
                        number = index.plus(1).toString(),
                        onDelete = {
                            bankAccountName = bankAccount.bankAccountName
                            uniqueBankAccountId = bankAccount.uniqueBankAccountId
                            openDeleteConfirmation = !openDeleteConfirmation
                        },
                    ) {
                        navigateToViewBankScreen(bankAccount.uniqueBankAccountId)
                    }
                }
                HorizontalDivider()
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Bank Account",
            textContent = "Are you sure you want to permanently remove account $bankAccountName?",
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueBankAccountId)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }

        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = isDeletingBank,
            title = null,
            textContent = bankDeletingMessage ?: emptyString,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            if (bankDeletingIsSuccessful){
                reloadBankInfo()
            }
            confirmationInfoDialog = false
        }
    }
    

}
