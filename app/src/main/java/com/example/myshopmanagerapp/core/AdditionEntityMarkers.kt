package com.example.myshopmanagerapp.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myshopmanagerapp.core.Constants.AdditionEntityMarkers
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueBankAccountId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueCashInId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueCustomerId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueDebtId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueDebtRepaymentId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueExpenseId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueInventoryId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueInventoryItemId
import com.example.myshopmanagerapp.core.FormRelatedString.UniquePersonnelId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueReceiptId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueRevenueId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueSavingsId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueStockId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueSupplierId
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueWithdrawalId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AdditionEntityMarkers(private val context: Context) {
    // to make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(AdditionEntityMarkers)
        val UNIQUE_CUSTOMER_ID = stringPreferencesKey(UniqueCustomerId)
        val UNIQUE_BANK_ACCOUNT_ID = stringPreferencesKey(UniqueBankAccountId)
        val UNIQUE_CASH_IN_ID = stringPreferencesKey(UniqueCashInId)
        val UNIQUE_DEBT_ID = stringPreferencesKey(UniqueDebtId)
        val UNIQUE_DEBT_REPAYMENT_ID = stringPreferencesKey(UniqueDebtRepaymentId)
        val UNIQUE_EXPENSE_ID = stringPreferencesKey(UniqueExpenseId)
        val UNIQUE_INVENTORY_ID = stringPreferencesKey(UniqueInventoryId)
        val UNIQUE_INVENTORY_ITEM_ID = stringPreferencesKey(UniqueInventoryItemId)
        val UNIQUE_INVENTORY_STOCK_ID = stringPreferencesKey(UniqueStockId)
        val UNIQUE_PERSONNEL_ID = stringPreferencesKey(UniquePersonnelId)
        val UNIQUE_RECEIPT_ID = stringPreferencesKey(UniqueReceiptId)
        val UNIQUE_REVENUE_ID = stringPreferencesKey(UniqueRevenueId)
        val UNIQUE_SAVINGS_ID = stringPreferencesKey(UniqueSavingsId)
        val UNIQUE_STOCK_ID = stringPreferencesKey(UniqueStockId)
        val UNIQUE_SUPPLIER_ID = stringPreferencesKey(UniqueSupplierId)
        val UNIQUE_WITHDRAWAL_ID = stringPreferencesKey(UniqueWithdrawalId)
    }



    //get Added Customer
    val getAddedCustomerIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CUSTOMER_ID] ?: emptyString }

    //save Added Customer Ids
    suspend fun saveAddedCustomerIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CUSTOMER_ID] = value }
    }

    //get Added Bank Account
    val getAddedBankAccountIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] ?: emptyString }

    //save Added Bank Account Ids
    suspend fun saveAddedBankAccountIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] = value }
    }

    //get Added Cash In
    val getAddedCashInIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CASH_IN_ID] ?: emptyString }

    //save Added Cash In Ids
    suspend fun saveAddedCashInIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CASH_IN_ID] = value }
    }

    //get Added Debt
    val getAddedDebtIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_ID] ?: emptyString }

    //save Added Debt Ids
    suspend fun saveAddedDebtIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_ID] = value }
    }

    //get Added Debt Repayment
    val getAddedDebtRepaymentIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] ?: emptyString }

    //save Added Debt Repayment Ids
    suspend fun saveAddedDebtRepaymentIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] = value }
    }

    //get Added Expense
    val getAddedExpenseIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_EXPENSE_ID] ?: emptyString }

    //save Added Expense Ids
    suspend fun saveAddedExpenseIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_EXPENSE_ID] = value }
    }

    //get Added Inventory
    val getAddedInventoryIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ID] ?: emptyString }

    //save Added Inventory Ids
    suspend fun saveAddedInventoryIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ID] = value }
    }

    //get Added Personnel
    val getAddedPersonnelIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_PERSONNEL_ID] ?: emptyString }

    //save Added Personnel Ids
    suspend fun saveAddedPersonnelIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_PERSONNEL_ID] = value }
    }

    //get Added Receipt
    val getAddedReceiptIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_RECEIPT_ID] ?: emptyString }

    //save Added Receipt Ids
    suspend fun saveAddedReceiptIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_RECEIPT_ID] = value }
    }

    //get Added Revenue
    val getAddedRevenueIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_REVENUE_ID] ?: emptyString }

    //save Added Revenue Ids
    suspend fun saveAddedRevenueIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_REVENUE_ID] = value }
    }

    //get Added Savings
    val getAddedSavingsIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SAVINGS_ID] ?: emptyString }

    //save Added Savings Ids
    suspend fun saveAddedSavingsIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SAVINGS_ID] = value }
    }

    //get Added Stock
    val getAddedStockIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_STOCK_ID] ?: emptyString }

    //save Added Stock Ids
    suspend fun saveAddedStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_STOCK_ID] = value }
    }

    //get Added Withdrawal
    val getAddedWithdrawalIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] ?: emptyString }

    //save Added Withdrawal Ids
    suspend fun saveAddedWithdrawalIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] = value }
    }

    //get Added Supplier
    val getAddedSupplierIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SUPPLIER_ID] ?: emptyString }

    //save Added Supplier Ids
    suspend fun saveAddedSupplierIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SUPPLIER_ID] = value }
    }

    //get Added Inventory Stock
    val getAddedInventoryStockIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] ?: emptyString }

    //save Added Inventory Stock Ids
    suspend fun saveAddedInventoryStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] = value }
    }

    //get Added Inventory Item
    val getAddedInventoryItemIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] ?: emptyString }

    //save Added Inventory Item Ids
    suspend fun saveAddedInventoryItemIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] = value }
    }



}