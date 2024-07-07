package com.example.myshopmanagerapp.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myshopmanagerapp.core.Constants.DeleteEntityMarkers
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


class DeleteEntityMarkers(private val context: Context) {
    // to make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DeleteEntityMarkers)
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



    //get Deleted Customer
    val getDeletedCustomerId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CUSTOMER_ID] ?: emptyString }

    //save Deleted Customer Ids
    suspend fun saveDeletedCustomerIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CUSTOMER_ID] = value }
    }

    //get Deleted Bank Account
    val getDeletedBankAccountId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] ?: emptyString }

    //save Deleted Bank Account Ids
    suspend fun saveDeletedBankAccountIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] = value }
    }

    //get Deleted Cash In
    val getDeletedCashInId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CASH_IN_ID] ?: emptyString }

    //save Deleted Cash In Ids
    suspend fun saveDeletedCashInIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CASH_IN_ID] = value }
    }

    //get Deleted Debt
    val getDeletedDebtId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_ID] ?: emptyString }

    //save Deleted Debt Ids
    suspend fun saveDeletedDebtIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_ID] = value }
    }

    //get Deleted Debt Repayment
    val getDeletedDebtRepaymentId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] ?: emptyString }

    //save Deleted Debt Repayment Ids
    suspend fun saveDeletedDebtRepaymentIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] = value }
    }

    //get Deleted Expense
    val getDeletedExpenseId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_EXPENSE_ID] ?: emptyString }

    //save Deleted Expense Ids
    suspend fun saveDeletedExpenseIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_EXPENSE_ID] = value }
    }

    //get Deleted Inventory
    val getDeletedInventoryId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ID] ?: emptyString }

    //save Deleted Inventory Ids
    suspend fun saveDeletedInventoryIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ID] = value }
    }

    //get Deleted Personnel
    val getDeletedPersonnelId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_PERSONNEL_ID] ?: emptyString }

    //save Deleted Personnel Ids
    suspend fun saveDeletedPersonnelIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_PERSONNEL_ID] = value }
    }

    //get Deleted Receipt
    val getDeletedReceiptId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_RECEIPT_ID] ?: emptyString }

    //save Deleted Receipt Ids
    suspend fun saveDeletedReceiptIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_RECEIPT_ID] = value }
    }

    //get Deleted Revenue
    val getDeletedRevenueId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_REVENUE_ID] ?: emptyString }

    //save Deleted Revenue Ids
    suspend fun saveDeletedRevenueIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_REVENUE_ID] = value }
    }

    //get Deleted Savings
    val getDeletedSavingsId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SAVINGS_ID] ?: emptyString }

    //save Deleted Savings Ids
    suspend fun saveDeletedSavingsIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SAVINGS_ID] = value }
    }

    //get Deleted Stock
    val getDeletedStockId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_STOCK_ID] ?: emptyString }

    //save Deleted Stock Ids
    suspend fun saveDeletedStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_STOCK_ID] = value }
    }

    //get Deleted Withdrawal
    val getDeletedWithdrawalId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] ?: emptyString }

    //save Deleted Withdrawal Ids
    suspend fun saveDeletedWithdrawalIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] = value }
    }

    //get Deleted Supplier
    val getDeletedSupplierId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SUPPLIER_ID] ?: emptyString }

    //save Deleted Supplier Ids
    suspend fun saveDeletedSupplierIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SUPPLIER_ID] = value }
    }

    //get Deleted Inventory Stock
    val getDeletedInventoryStockId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] ?: emptyString }

    //save Deleted Inventory Stock Ids
    suspend fun saveDeletedInventoryStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] = value }
    }

    //get Deleted Inventory Item
    val getDeletedInventoryItemId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] ?: emptyString }

    //save Deleted Inventory Item Ids
    suspend fun saveDeletedInventoryItemIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] = value }
    }



}