package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.AddCustomerContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel


@Composable
fun AddCustomerScreen(
    customerViewModel: CustomerViewModel,
    navigateToTakePhotoScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        customerViewModel.getAllCustomers()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Customer") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddCustomerContent(
                customer = customerViewModel.addCustomerInfo,
                isSavingCustomer = customerViewModel.addCustomerState.value.isLoading,
                customerSavingMessage = customerViewModel.addCustomerState.value.message,
                customerSavingIsSuccessful = customerViewModel.addCustomerState.value.isSuccessful,
                addCustomerName = { value-> customerViewModel.addCustomerName(value) },
                addCustomerContact = { value-> customerViewModel.addCustomerContact(value) },
                addCustomerLocation = { value-> customerViewModel.addCustomerLocation(value) },
                addAnyOtherInfo = { value-> customerViewModel.addAnyOtherInfo(value) },
                addCustomer = {_customer-> customerViewModel.addCustomer(_customer) }
            ) {
                navigateBack()
            }
        }
    }
}
