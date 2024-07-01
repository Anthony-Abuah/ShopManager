package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.supplier.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.supplier.ViewSupplierContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SupplierViewModel


@Composable
fun ViewSupplierScreen(
    supplierViewModel: SupplierViewModel,
    uniqueSupplierId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        supplierViewModel.getSupplier(uniqueSupplierId)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Supplier") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewSupplierContent(
                supplier = supplierViewModel.supplierInfo,
                isUpdatingSupplier = supplierViewModel.updateSupplierState.value.isLoading,
                supplierUpdateMessage = supplierViewModel.updateSupplierState.value.message,
                supplierUpdatingIsSuccessful = supplierViewModel.updateSupplierState.value.isSuccessful,
                getUpdatedSupplierName = {_name->
                    supplierViewModel.updateSupplierName(_name)
                },
                getUpdatedSupplierContact = {_contact->
                    supplierViewModel.updateSupplierContact(_contact)
                },
                getUpdatedSupplierRole = {_role->
                    supplierViewModel.updateSupplierRole(_role)
                },
                getUpdatedSupplierLocation = {_location->
                    supplierViewModel.updateSupplierLocation(_location)
                },
                getUpdatedSupplierOtherInfo = {_otherInfo->
                    supplierViewModel.updateSupplierOtherInfo(_otherInfo)
                },
                updateSupplier = {_supplier->
                    supplierViewModel.updateSupplier(_supplier)
                },
                navigateBack = {
                    navigateBack()
                }
            )
        }
    }
}
