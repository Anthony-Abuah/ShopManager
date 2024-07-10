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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.InventoryItemsReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel

@Composable
fun InventoryItemsReportScreen(
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {

    LaunchedEffect(Unit) {
        inventoryItemViewModel.getAllInventoryItems()
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Inventory Items") {
                navigateBack()
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            InventoryItemsReportContent(
                inventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()
            )
        }
    }
}
