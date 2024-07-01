package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun CustomerListContent(
    customers: List<CustomerEntity>,
    isDeletingCustomer: Boolean,
    customerDeletingMessage: String?,
    customerDeletionIsSuccessful: Boolean,
    reloadAllCustomers: () -> Unit,
    onConfirmDelete: (String) -> Unit,
    navigateToViewCustomerScreen: (String) -> Unit
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
    var customerName by remember {
        mutableStateOf(emptyString)
    }

    if (customers.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No customers to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    else {
        BasicScreenColumnWithoutBottomBar {
            customers.forEachIndexed { index, customer ->
                if (index == 0){ HorizontalDivider() }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    CustomerCard(
                        customerName = customer.customerName,
                        customerContact = customer.customerContact,
                        customerLocation = customer.customerLocation ?: emptyString,
                        customerDebt = customer.debtAmount?.toString() ?: "0.0",
                        number = index.plus(1).toString(),
                        onOpenCustomer = { navigateToViewCustomerScreen(customer.uniqueCustomerId) }
                    ) {
                        customerName = customer.customerName
                        uniqueCustomerId = customer.uniqueCustomerId
                        openDeleteConfirmation = !openDeleteConfirmation
                    }
                }
                HorizontalDivider()
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Customer",
            textContent = "Are your sure you want to permanently delete this customer",
            unconfirmedDeletedToastText = "Customer not deleted",
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueCustomerId)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isDeletingCustomer,
        title = null,
        textContent = customerDeletingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (customerDeletionIsSuccessful){
            reloadAllCustomers()
        }
        confirmationInfoDialog = false
    }

}
