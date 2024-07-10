package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.CustomerEntities
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.InventoryItemEntities
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.CustomerCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.InventoryItemReportCard

@Composable
fun OwingCustomersReportContent(
    customers: CustomerEntities
){
    val context = LocalContext.current
    val currency = UserPreferences(context).getCurrency.collectAsState(initial = emptyString).value ?: GHS
    BasicScreenColumnWithoutBottomBar{
        HorizontalDivider()
        customers.forEachIndexed { index, customer ->
            val debtAmount = customer.debtAmount?.toString() ?: NotAvailable
            CustomerCard(
                customerName = customer.customerName,
                customerContact = customer.customerContact,
                customerLocation = customer.customerLocation ?: NotAvailable,
                customerDebt = "$currency $debtAmount",
                number = index.plus(1).toString(),
                onOpenCustomer = { /*TODO*/ }) {}
            HorizontalDivider()
        }
    }


}