package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.InventoryItemsReportContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.OwingCustomersReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel

@Composable
fun OwingCustomersReportScreen(
    customerViewModel: CustomerViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {

    LaunchedEffect(Unit) {
        customerViewModel.getAllCustomers()
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Owing Customers") {
                navigateBack()
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
            val owingCustomers = allCustomers.filter { it.debtAmount.toNotNull() > 0.0 }.sortedByDescending { it.debtAmount.toNotNull() }
            OwingCustomersReportContent(customers = owingCustomers)
        }
    }
}
