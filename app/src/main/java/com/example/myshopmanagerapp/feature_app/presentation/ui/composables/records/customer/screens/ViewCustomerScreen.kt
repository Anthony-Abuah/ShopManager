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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.ViewCustomerContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel


@Composable
fun ViewCustomerScreen(
    customerViewModel: CustomerViewModel,
    uniqueCustomerId: String,
    navigateToTakePhotoScreen: ()-> Unit,
    navigateBack: ()-> Unit
) {
    LaunchedEffect(Unit){
        customerViewModel.getCustomer(uniqueCustomerId)
        customerViewModel.getAllCustomers()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Customer") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewCustomerContent(
                customer = customerViewModel.customerInfo,
                currency = "GHS",
                customerUpdatingIsSuccessful = customerViewModel.updateCustomerState.value.isSuccessful,
                isUpdatingCustomer = customerViewModel.updateCustomerState.value.isLoading,
                customerUpdatingMessage = customerViewModel.updateCustomerState.value.message,
                updateCustomerName = {_customerName ->
                    customerViewModel.updateCustomerName(_customerName)
                },
                updateCustomerContact = {_customerContact->
                    customerViewModel.updateCustomerContact(_customerContact)
                },
                updateCustomerLocation = {_customerLocation->
                    customerViewModel.updateCustomerLocation(_customerLocation)
                },
                updateShortNotes = { _customerOtherInfo->
                    customerViewModel.updateCustomerOtherInfo(_customerOtherInfo)
                },
                updateCustomer = {
                    customerViewModel.customerInfo.let { _customer ->
                        customerViewModel.updateCustomer(_customer)
                    }
                },
                takePhoto = { navigateToTakePhotoScreen() }
            ) {
                navigateBack()
            }
        }
    }
}



