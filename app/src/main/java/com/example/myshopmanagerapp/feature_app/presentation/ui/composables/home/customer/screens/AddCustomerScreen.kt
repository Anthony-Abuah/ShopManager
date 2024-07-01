package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.AddCustomerContent
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
                customerSavingIsSuccessful = customerViewModel.addCustomerState.value.isSuccessful,
                customerSavingMessage = customerViewModel.addCustomerState.value.message,
                isSavingCustomer = customerViewModel.addCustomerState.value.isLoading,
                onTakePhoto = {
                    val customer = customerViewModel.addCustomerInfo
                    customerViewModel.addCustomerName(customer.customerName)
                    customerViewModel.addCustomerContact(customer.customerContact)
                    customerViewModel.addCustomerLocation(customer.customerLocation.toNotNull())
                    customerViewModel.addAnyOtherInfo(customer.otherInfo.toNotNull())
                    navigateToTakePhotoScreen()
                },
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
