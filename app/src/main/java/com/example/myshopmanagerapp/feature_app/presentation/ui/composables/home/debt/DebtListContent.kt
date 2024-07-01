package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.ONE
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.DebtEntities
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun DebtListContent(
    allDebts: DebtEntities,
    isDeletingDebt: Boolean,
    debtDeletionMessage: String?,
    debtDeletionIsSuccessful: Boolean,
    getCustomerName: (String) -> String,
    reloadAllDebts: () -> Unit,
    onConfirmDelete: (String) -> Unit,
    navigateToViewDebtScreen: (String) -> Unit
) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueCustomerId by remember {
        mutableStateOf(emptyString)
    }
    var uniqueDebtId by remember {
        mutableStateOf(emptyString)
    }
    var debtAmount by remember {
        mutableStateOf(emptyString)
    }
    var customer by remember {
        mutableStateOf(emptyString)
    }

    if (allDebts.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No debts to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        BasicScreenColumnWithoutBottomBar {
            HorizontalDivider()
            /*
            allDebts.forEachIndexed { index, debt ->
                val dateString = debt.date.toDate().toLocalDate().toDateString()
                val dayOfWeek = debt.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                val debtAmount = debt.debtAmount
                val customerName = getCustomerName(debt.uniqueCustomerId)
                if (index == 0){ HorizontalDivider() }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    DebtCard(
                        date = "$dayOfWeek, $dateString",
                        debtAmount = debtAmount.toString(),
                        customerName = customerName,
                        currency = "GHS",
                        delete = {
                            uniqueCustomerId = debt.uniqueCustomerId
                            customer = customerName
                            uniqueDebtId = debt.uniqueDebtId
                            uniqueDebtAmount = debt.debtAmount.toString()
                            openDeleteConfirmation = !openDeleteConfirmation
                        },
                        number = index.plus(1).toString()
                    ) {
                        navigateToViewDebtScreen(debt.uniqueDebtId)
                    }
                }
                HorizontalDivider()
            }
            */
            val groupedDebts = allDebts.groupBy { getCustomerName(it.uniqueCustomerId) }
            groupedDebts.keys.forEach { key->
                Column(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    var showAllCustomerDebts by remember {
                        mutableStateOf(false)
                    }
                    val customerDebts = groupedDebts[key] ?: emptyList()
                    if (customerDebts.isNotEmpty()){
                        val latestDebt = customerDebts.maxBy { it.date }
                        val totalCustomerDebts = customerDebts.sumOf { it.debtAmount }
                        if (!showAllCustomerDebts) {
                            DebtCard(
                                date = "${latestDebt.dayOfWeek?.take(3)}, ${latestDebt.date.toDateString()}",
                                debtAmount = totalCustomerDebts.toString(),
                                customerName = getCustomerName(latestDebt.uniqueCustomerId),
                                showAllItems = showAllCustomerDebts,
                                currency = "GHS",
                                number = customerDebts.size.toString(),
                                delete = {},
                                showAll = { showAllCustomerDebts = !showAllCustomerDebts },
                                edit = { navigateToViewDebtScreen(latestDebt.uniqueDebtId) }
                            ) {
                                showAllCustomerDebts = !showAllCustomerDebts
                            }
                        }
                        AnimatedVisibility(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface),
                            visible = showAllCustomerDebts) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(LocalSpacing.current.small)
                            ) {
                                customerDebts.forEachIndexed { index, _debt ->
                                    DebtCard(
                                        date = "${_debt.dayOfWeek}, ${_debt.date.toDateString()}",
                                        debtAmount = _debt.debtAmount.toString(),
                                        customerName = getCustomerName(_debt.uniqueCustomerId),
                                        currency = "GHS",
                                        showAllItems = showAllCustomerDebts,
                                        number = index.plus(1).toString(),
                                        delete = {
                                            debtAmount = _debt.debtAmount.toString()
                                            uniqueDebtId = _debt.uniqueDebtId
                                            openDeleteConfirmation = !openDeleteConfirmation
                                        },
                                        showAll = { showAllCustomerDebts = !showAllCustomerDebts },
                                        edit = { navigateToViewDebtScreen(_debt.uniqueDebtId) }
                                    ) {
                                        navigateToViewDebtScreen(_debt.uniqueDebtId)
                                    }
                                }
                            }
                        }
                        if (showAllCustomerDebts) HorizontalDivider()
                    }
                }
            }
        }

        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Debt",
            textContent = "Are you sure you want to permanently delete this debt",
            unconfirmedDeletedToastText = "Debt not deleted",
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueDebtId)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isDeletingDebt,
        title = null,
        textContent = debtDeletionMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (debtDeletionIsSuccessful){
            reloadAllDebts()
        }
        confirmationInfoDialog = false
    }
}
