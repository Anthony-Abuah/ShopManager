package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.generate_receipt.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.generate_receipt.GenerateReceiptContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel
import java.util.*


@Composable
fun GenerateReceiptScreen(
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
            val customer = customerViewModel.customerInfo
            val receiptInfo = companyViewModel.receiptInfo
            val date = receiptInfo.date
            val dateString = date.toDate().toDateString()
            val dayOfWeek = date.toDate().toLocalDate().dayOfWeek.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val receiptItems = receiptInfo.items
            val allCustomers = customerViewModel.customerEntitiesState.value.customerEntities ?: emptyList()
            val mapOfCustomers = mutableMapOf<String, String>()
            allCustomers.forEach {_customer->
                mapOfCustomers[_customer.customerName] = _customer.uniqueCustomerId
            }

            val allInventoryItems = inventoryItemViewModel.inventoryItemEntitiesState.value.inventoryItemEntities ?: emptyList()

            val shopInfo = shopInfoJson.toCompanyEntity()
            val shopName = shopInfo?.companyName ?: "My Shop"
            val shopContact = shopInfo?.companyContact ?: "My contact"
            val shopLocation = shopInfo?.companyLocation ?: "My Location"

            GenerateReceiptContent(
                dateString = dateString,
                dayOfWeek = dayOfWeek,
                receiptCreatedMessage = companyViewModel.addReceiptState.value.message.toNotNull(),
                receiptIsCreated = companyViewModel.addReceiptState.value.isSuccessful,
                receiptDisplayItems = receiptItems,
                inventoryItems = allInventoryItems.map { _item-> _item.inventoryItemName },
                mapOfCustomers = mapOfCustomers,
                addReceiptDate = {_dateString->
                    val longDate = _dateString.toLocalDate().toDate().time
                    companyViewModel.addReceiptDate(longDate)
                },
                getReceiptItems = {_item->
                    companyViewModel.addReceiptItems(_item)
                },
                getUniqueCustomerId = {_id-> customerViewModel.getCustomer(_id) },
                getCustomerName = {_name -> customerViewModel.updateCustomerName(_name) },
                createInventoryItem = { navigateToAddInventoryItemScreen() },
                addCustomer = { navigateToAddCustomerScreen() },
                saveReceipt = {
                    companyViewModel.addReceiptCustomer(customer.customerName, customer.customerContact)
                    companyViewModel.addReceiptShopInfo(shopName, shopContact, shopLocation)
                    companyViewModel.addReceiptPersonnel("Anthony Abuah", "Manager")
                    companyViewModel.generateReceipt()
                }
            ) {
                navigateBack()
            }
        }
    }
}
