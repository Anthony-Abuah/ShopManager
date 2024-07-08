package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.supplier

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
import com.example.myshopmanagerapp.core.SupplierEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun SupplierListContent(
    allSuppliers: SupplierEntities?,
    isDeletingSupplier: Boolean,
    supplierDeletionIsSuccessful: Boolean,
    supplierDeletingMessage: String?,
    reloadAllSupplier: ()-> Unit,
    onConfirmDelete: (String)-> Unit,
    navigateToViewSupplierScreen: (String)-> Unit
) {
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var uniqueSupplierId by remember {
        mutableStateOf(emptyString)
    }
    var supplierName by remember {
        mutableStateOf(emptyString)
    }

    if (allSuppliers.isNullOrEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No suppliers to show",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    else{
        BasicScreenColumnWithoutBottomBar {
            allSuppliers.forEachIndexed {index, supplier ->
                if (index == 0) {
                    HorizontalDivider()
                }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    SupplierCard(
                        supplier = supplier,
                        number = index.plus(1).toString(),
                        onDelete = {
                            supplierName = supplier.supplierName
                            uniqueSupplierId = supplier.uniqueSupplierId
                            openDeleteConfirmation = !openDeleteConfirmation
                        },
                    ) {
                        navigateToViewSupplierScreen(supplier.uniqueSupplierId)
                    }
                }
                HorizontalDivider()
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Supplier",
            textContent = "Are your sure you want to permanently remove $supplierName",
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueSupplierId)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }
        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = isDeletingSupplier,
            title = null,
            textContent = supplierDeletingMessage.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            if (supplierDeletionIsSuccessful){
                reloadAllSupplier()
            }
            confirmationInfoDialog = false
        }
    }

}
