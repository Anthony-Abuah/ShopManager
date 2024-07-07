package com.example.myshopmanagerapp.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myshopmanagerapp.core.Constants.UpdateEntityMarkers
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


class UpdateEntityMarkers(private val context: Context) {
    // to make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(UpdateEntityMarkers)
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



    //get Updated Customer
    val getUpdatedCustomerId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CUSTOMER_ID] ?: emptyString }

    //save Updated Customer Ids
    suspend fun saveUpdatedCustomerIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CUSTOMER_ID] = value }
    }

    //get Updated Bank Account
    val getUpdatedBankAccountId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] ?: emptyString }

    //save Updated Bank Account Ids
    suspend fun saveUpdatedBankAccountIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] = value }
    }

    //get Updated Cash In
    val getUpdatedCashInId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CASH_IN_ID] ?: emptyString }

    //save Updated Cash In Ids
    suspend fun saveUpdatedCashInIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CASH_IN_ID] = value }
    }

    //get Updated Debt
    val getUpdatedDebtId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_ID] ?: emptyString }

    //save Updated Debt Ids
    suspend fun saveUpdatedDebtIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_ID] = value }
    }

    //get Updated Debt Repayment
    val getUpdatedDebtRepaymentId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] ?: emptyString }

    //save Updated Debt Repayment Ids
    suspend fun saveUpdatedDebtRepaymentIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] = value }
    }

    //get Updated Expense
    val getUpdatedExpenseId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_EXPENSE_ID] ?: emptyString }

    //save Updated Expense Ids
    suspend fun saveUpdatedExpenseIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_EXPENSE_ID] = value }
    }

    //get Updated Inventory
    val getUpdatedInventoryId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ID] ?: emptyString }

    //save Updated Inventory Ids
    suspend fun saveUpdatedInventoryIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ID] = value }
    }

    //get Updated Personnel
    val getUpdatedPersonnelId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_PERSONNEL_ID] ?: emptyString }

    //save Updated Personnel Ids
    suspend fun saveUpdatedPersonnelIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_PERSONNEL_ID] = value }
    }

    //get Updated Receipt
    val getUpdatedReceiptId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_RECEIPT_ID] ?: emptyString }

    //save Updated Receipt Ids
    suspend fun saveUpdatedReceiptIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_RECEIPT_ID] = value }
    }

    //get Updated Revenue
    val getUpdatedRevenueId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_REVENUE_ID] ?: emptyString }

    //save Updated Revenue Ids
    suspend fun saveUpdatedRevenueIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_REVENUE_ID] = value }
    }

    //get Updated Savings
    val getUpdatedSavingsId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SAVINGS_ID] ?: emptyString }

    //save Updated Savings Ids
    suspend fun saveUpdatedSavingsIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SAVINGS_ID] = value }
    }

    //get Updated Stock
    val getUpdatedStockId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_STOCK_ID] ?: emptyString }

    //save Updated Stock Ids
    suspend fun saveUpdatedStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_STOCK_ID] = value }
    }

    //get Updated Withdrawal
    val getUpdatedWithdrawalId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] ?: emptyString }

    //save Updated Withdrawal Ids
    suspend fun saveUpdatedWithdrawalIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] = value }
    }

    //get Updated Supplier
    val getUpdatedSupplierId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SUPPLIER_ID] ?: emptyString }

    //save Updated Supplier Ids
    suspend fun saveUpdatedSupplierIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SUPPLIER_ID] = value }
    }

    //get Updated Inventory Stock
    val getUpdatedInventoryStockId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] ?: emptyString }

    //save Updated Inventory Stock Ids
    suspend fun saveUpdatedInventoryStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] = value }
    }

    //get Updated Inventory Item
    val getUpdatedInventoryItemId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] ?: emptyString }

    //save Updated Inventory Item Ids
    suspend fun saveUpdatedInventoryItemIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] = value }
    }



}