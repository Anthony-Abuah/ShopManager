package com.example.myshopmanagerapp.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myshopmanagerapp.core.Constants.ChangesEntityMarkers
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


class ChangesEntityMarkers(private val context: Context) {
    // to make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(ChangesEntityMarkers)
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



    //get Changed Customer
    val getChangedCustomerIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CUSTOMER_ID] ?: emptyString }

    //save Changed Customer Ids
    suspend fun saveChangedCustomerIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CUSTOMER_ID] = value }
    }

    //get Changed Bank Account
    val getChangedBankAccountIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] ?: emptyString }

    //save Changed Bank Account Ids
    suspend fun saveChangedBankAccountIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_BANK_ACCOUNT_ID] = value }
    }

    //get Changed Cash In
    val getChangedCashInIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_CASH_IN_ID] ?: emptyString }

    //save Changed Cash In Ids
    suspend fun saveChangedCashInIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_CASH_IN_ID] = value }
    }

    //get Changed Debt
    val getChangedDebtIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_ID] ?: emptyString }

    //save Changed Debt Ids
    suspend fun saveChangedDebtIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_ID] = value }
    }

    //get Changed Debt Repayment
    val getChangedDebtRepaymentIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] ?: emptyString }

    //save Changed Debt Repayment Ids
    suspend fun saveChangedDebtRepaymentIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_DEBT_REPAYMENT_ID] = value }
    }

    //get Changed Expense
    val getChangedExpenseIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_EXPENSE_ID] ?: emptyString }

    //save Changed Expense Ids
    suspend fun saveChangedExpenseIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_EXPENSE_ID] = value }
    }

    //get Changed Inventory
    val getChangedInventoryIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ID] ?: emptyString }

    //save Changed Inventory Ids
    suspend fun saveChangedInventoryIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ID] = value }
    }

    //get Changed Personnel
    val getChangedPersonnelIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_PERSONNEL_ID] ?: emptyString }

    //save Changed Personnel Ids
    suspend fun saveChangedPersonnelIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_PERSONNEL_ID] = value }
    }

    //get Changed Receipt
    val getChangedReceiptIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_RECEIPT_ID] ?: emptyString }

    //save Changed Receipt Ids
    suspend fun saveChangedReceiptIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_RECEIPT_ID] = value }
    }

    //get Changed Revenue
    val getChangedRevenueIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_REVENUE_ID] ?: emptyString }

    //save Changed Revenue Ids
    suspend fun saveChangedRevenueIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_REVENUE_ID] = value }
    }

    //get Changed Savings
    val getChangedSavingsIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SAVINGS_ID] ?: emptyString }

    //save Changed Savings Ids
    suspend fun saveChangedSavingsIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SAVINGS_ID] = value }
    }

    //get Changed Stock
    val getChangedStockIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_STOCK_ID] ?: emptyString }

    //save Changed Stock Ids
    suspend fun saveChangedStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_STOCK_ID] = value }
    }

    //get Changed Withdrawal
    val getChangedWithdrawalIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] ?: emptyString }

    //save Changed Withdrawal Ids
    suspend fun saveChangedWithdrawalIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_WITHDRAWAL_ID] = value }
    }

    //get Changed Supplier
    val getChangedSupplierIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_SUPPLIER_ID] ?: emptyString }

    //save Changed Supplier Ids
    suspend fun saveChangedSupplierIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_SUPPLIER_ID] = value }
    }

    //get Changed Inventory Stock
    val getChangedInventoryStockIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] ?: emptyString }

    //save Changed Inventory Stock Ids
    suspend fun saveChangedInventoryStockIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_STOCK_ID] = value }
    }

    //get Changed Inventory Item
    val getChangedInventoryItemIds: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] ?: emptyString }

    //save Changed Inventory Item Ids
    suspend fun saveChangedInventoryItemIds(value: String) {
        context.dataStore.edit { preferences -> preferences[UNIQUE_INVENTORY_ITEM_ID] = value }
    }



}