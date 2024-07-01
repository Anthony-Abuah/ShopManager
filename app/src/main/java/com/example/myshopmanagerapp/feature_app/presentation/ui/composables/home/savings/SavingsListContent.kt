package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.savings

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
import com.example.myshopmanagerapp.core.SavingsEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun SavingsListContent(
    allSavings: SavingsEntities,
    isDeletingSavings: Boolean,
    savingsDeletionMessage: String?,
    savingsDeletionIsSuccessful: Boolean,
    getBankAccountName: (String) -> String,
    reloadAllSavings: () -> Unit,
    onConfirmDelete: (String) -> Unit,
    navigateToViewSavingsScreen: (String) -> Unit
) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueSavingsId by remember {
        mutableStateOf(emptyString)
    }

    if (allSavings.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No savings to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        BasicScreenColumnWithoutBottomBar {
            allSavings.forEachIndexed {index, savings ->
                val bankAccountName = getBankAccountName(savings.uniqueBankAccountId)
                if (index == 0){
                    HorizontalDivider()
                }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    SavingsCard(
                        savings = savings,
                        number = index.plus(1).toString(),
                        accountName = bankAccountName,
                        currency = "GHS",
                        onDelete = {
                            uniqueSavingsId = savings.uniqueSavingsId
                            openDeleteConfirmation = !openDeleteConfirmation }) {
                        navigateToViewSavingsScreen(savings.uniqueSavingsId)
                    }
                }
                HorizontalDivider()
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Savings",
            textContent = "Are you sure you want to permanently delete this savings",
            unconfirmedDeletedToastText = "Savings not deleted",
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueSavingsId)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isDeletingSavings,
        title = null,
        textContent = savingsDeletionMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (savingsDeletionIsSuccessful){
            reloadAllSavings()
        }
        confirmationInfoDialog = false
    }
}
