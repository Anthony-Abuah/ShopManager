package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.supplier.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.supplier.AddSupplierContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SupplierViewModel


@Composable
fun AddSupplierScreen(
    supplierViewModel: SupplierViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Supplier") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddSupplierContent(
                supplier = supplierViewModel.addSupplierInfo,
                isSavingSupplier = supplierViewModel.addSupplierState.value.isLoading,
                supplierSavingIsSuccessful = supplierViewModel.addSupplierState.value.isSuccessful,
                supplierSavingMessage = supplierViewModel.addSupplierState.value.message,
                addSupplierName = {
                    supplierViewModel.addSupplierName(it)
                },
                addSupplierContact = {
                    supplierViewModel.addSupplierContact(it)
                },
                addSupplierLocation = {
                    supplierViewModel.addSupplierLocation(it)
                },
                addSupplierOtherInfo = {
                    supplierViewModel.addSupplierOtherInfo(it)
                },
                addSupplierRole = {
                    supplierViewModel.addSupplierRole(it)
                },
                addSupplier = {_supplier->
                    supplierViewModel.addSupplier(_supplier)
                }
            ) {
                navigateBack()
            }
        }
    }
}
