package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt.UpdateReceiptContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel


@Composable
fun UpdateReceiptScreen(
    uniqueReceiptId: String,
    companyViewModel: CompanyViewModel,
    customerViewModel: CustomerViewModel = hiltViewModel(),
    inventoryItemViewModel: InventoryItemViewModel = hiltViewModel(),
    navigateToAddInventoryItemScreen: () -> Unit,
    navigateToAddCustomerScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val shopInfoJson = userPreferences.getShopInfo.collectAsState(initial = emptyString).value

    LaunchedEffect(Unit){
        companyViewModel.getReceipt(uniqueReceiptId)
        inventoryItemViewModel.getAllInventoryItems()
        customerViewModel.getAllCustomers()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Receipt") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val updatedReceiptInfo = companyViewModel.updateReceiptInfo
            val receiptItems = updatedReceiptInfo.items
            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()

            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()

            val shopInfo = shopInfoJson.toCompanyEntity()
            val shopName = shopInfo?.companyName ?: "My Shop"
            val shopContact = shopInfo?.companyContact ?: "My contact"
            val shopLocation = shopInfo?.companyLocation ?: "My Location"

            UpdateReceiptContent(
                receipt = updatedReceiptInfo,
                receiptCreatedMessage = companyViewModel.updateReceiptState.value.message.toNotNull(),
                receiptIsCreated = companyViewModel.updateReceiptState.value.isSuccessful,
                receiptDisplayItems = receiptItems,
                inventoryItems = allInventoryItems,
                allCustomers = allCustomers,
                updateReceiptDate = { _dateString->
                    val longDate = _dateString.toLocalDate().toDate().time
                    companyViewModel.updateReceiptDate(longDate)
                },
                updateReceiptItems = { _item->
                    companyViewModel.updateReceiptItems(_item)
                },
                updateCustomer = { _customer->
                    companyViewModel.updateReceiptCustomer(_customer?.customerName.toNotNull(), _customer?.customerContact.toNotNull())
                },
                createInventoryItem = { navigateToAddInventoryItemScreen() },
                addNewCustomer = { navigateToAddCustomerScreen() },
                updateReceipt = {
                    companyViewModel.updateReceiptShopInfo(shopName, shopContact, shopLocation)
                    companyViewModel.updateReceipt()
                }
            ) {
                navigateBack()
            }
        }
    }
}
