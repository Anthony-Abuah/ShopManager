package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt.ReceiptListContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun ReceiptListScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    navigateToAddReceiptScreen: () -> Unit,
    navigateToUpdateReceipt: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect( Unit ){
        companyViewModel.getAllReceipts()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Receipt List") {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddReceiptScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allReceipts = companyViewModel.receiptEntitiesState.value.receiptEntities ?: emptyList()
            val isLoading = companyViewModel.receiptEntitiesState.value.isLoading

            ReceiptListContent(
                allReceipts = allReceipts,
                isLoading = isLoading,
                savePDFConfirmationMessage = companyViewModel.generateInvoiceState.value.data,
                navigateToUpdateReceipt = { navigateToUpdateReceipt(it) }
            ){_receiptEntity->
                companyViewModel.generateReceipt(context, _receiptEntity.toReceiptInfo() )
            }
        }
    }
}
