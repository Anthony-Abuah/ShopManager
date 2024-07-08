package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt_repayment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.DebtRepaymentEntities
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun DebtRepaymentListContent(
    allDebtRepayments: DebtRepaymentEntities,
    isDeletingDebtRepayment: Boolean,
    debtRepaymentDeletionIsSuccessful: Boolean,
    debtRepaymentDeletionMessage: String?,
    getCustomerName: (String) -> String,
    reloadAllDebtRepayments: () -> Unit,
    onConfirmDelete: (String) -> Unit,
    navigateToViewDebtRepaymentScreen: (String) -> Unit
) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueDebtRepaymentId by remember {
        mutableStateOf(emptyString)
    }

    if (allDebtRepayments.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No debt repayments to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        BasicScreenColumnWithoutBottomBar {
            /*
            allDebtRepayments.forEachIndexed { index,debtRepayment ->
                val dateString = debtRepayment.date.toDate().toDateString()
                val dayOfWeek = debtRepayment.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                val debtRepaymentAmount = debtRepayment.debtRepaymentAmount
                val customerName = getCustomerName(debtRepayment.uniqueCustomerId)

                if (index == 0){ HorizontalDivider() }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ){
                    DebtCard(
                        date = "$dayOfWeek, $dateString",
                        debtAmount = debtRepaymentAmount.toString(),
                        customerName = customerName,
                        currency = "GHS",
                        delete = {
                            uniqueCustomerId = debtRepayment.uniqueCustomerId
                            customer = customerName
                            uniqueDebtRepaymentId = debtRepayment.uniqueDebtRepaymentId
                            openDeleteConfirmation = !openDeleteConfirmation
                        },
                        number = index.plus(1).toString()
                    ) {
                        navigateToViewDebtRepaymentScreen(debtRepayment.uniqueDebtRepaymentId)
                    }
                }
                HorizontalDivider()
            }
            */
            HorizontalDivider()
            val groupedDebtRepayments = allDebtRepayments.groupBy { getCustomerName(it.uniqueCustomerId) }
            groupedDebtRepayments.keys.forEach { key->
                Column(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    var showAllCustomerDebtRepayments by remember {
                        mutableStateOf(false)
                    }
                    val customerDebtRepayments = groupedDebtRepayments[key] ?: emptyList()
                    if (customerDebtRepayments.isNotEmpty()){
                        val latestDebtRepayment = customerDebtRepayments.maxBy { it.date }
                        val totalCustomerDebtRepayments = customerDebtRepayments.sumOf { it.debtRepaymentAmount }
                        if (!showAllCustomerDebtRepayments) {
                            DebtRepaymentCard(
                                date = "${latestDebtRepayment.dayOfWeek?.take(3)}, ${latestDebtRepayment.date.toDateString()}",
                                debtRepaymentAmount = totalCustomerDebtRepayments.toString(),
                                customerName = getCustomerName(latestDebtRepayment.uniqueCustomerId),
                                showAllItems = showAllCustomerDebtRepayments,
                                currency = "GHS",
                                number = customerDebtRepayments.size.toString(),
                                delete = {},
                                showAll = { showAllCustomerDebtRepayments = !showAllCustomerDebtRepayments },
                                edit = { navigateToViewDebtRepaymentScreen(latestDebtRepayment.uniqueDebtRepaymentId) }
                            ) {
                                showAllCustomerDebtRepayments = !showAllCustomerDebtRepayments
                            }
                        }
                        AnimatedVisibility(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface),
                            visible = showAllCustomerDebtRepayments) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(LocalSpacing.current.small)
                            ) {
                                customerDebtRepayments.forEachIndexed { index, _debtRepayment ->
                                    DebtRepaymentCard(
                                        date = "${_debtRepayment.dayOfWeek}, ${_debtRepayment.date.toDateString()}",
                                        debtRepaymentAmount = _debtRepayment.debtRepaymentAmount.toString(),
                                        customerName = getCustomerName(_debtRepayment.uniqueCustomerId),
                                        currency = "GHS",
                                        showAllItems = showAllCustomerDebtRepayments,
                                        number = index.plus(1).toString(),
                                        delete = {
                                            uniqueDebtRepaymentId = _debtRepayment.uniqueDebtRepaymentId
                                            openDeleteConfirmation = !openDeleteConfirmation
                                        },
                                        showAll = { showAllCustomerDebtRepayments = !showAllCustomerDebtRepayments },
                                        edit = { navigateToViewDebtRepaymentScreen(_debtRepayment.uniqueDebtRepaymentId) }
                                    ) {
                                        navigateToViewDebtRepaymentScreen(_debtRepayment.uniqueDebtRepaymentId)
                                    }
                                }
                            }
                        }
                        if (showAllCustomerDebtRepayments) HorizontalDivider()
                    }
                }
            }

        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Debt Repayment",
            textContent = "Are your sure you want to permanently delete this debt repayment?",
            unconfirmedDeletedToastText = "Debt repayment not deleted",
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueDebtRepaymentId)
            }
        ) {
            openDeleteConfirmation = false
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isDeletingDebtRepayment,
        title = null,
        textContent = debtRepaymentDeletionMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (debtRepaymentDeletionIsSuccessful){
            reloadAllDebtRepayments()
        }
        confirmationInfoDialog = false
    }
}
