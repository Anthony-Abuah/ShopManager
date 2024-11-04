package com.example.myshopmanagerapp.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.myshopmanagerapp.core.Constants.DoubleValue
import com.example.myshopmanagerapp.core.Constants.ExpenseNames
import com.example.myshopmanagerapp.core.Constants.ExpenseTypes
import com.example.myshopmanagerapp.core.Constants.IntValue
import com.example.myshopmanagerapp.core.Constants.IsLoggedIn
import com.example.myshopmanagerapp.core.Constants.IsRegistered
import com.example.myshopmanagerapp.core.Constants.ItemCategory
import com.example.myshopmanagerapp.core.Constants.ManufacturerName
import com.example.myshopmanagerapp.core.Constants.PersonnelRoles
import com.example.myshopmanagerapp.core.Constants.RegisterMessage
import com.example.myshopmanagerapp.core.Constants.ShopInfo
import com.example.myshopmanagerapp.core.Constants.StringValue
import com.example.myshopmanagerapp.core.Constants.BankPersonnel
import com.example.myshopmanagerapp.core.Constants.Currency
import com.example.myshopmanagerapp.core.Constants.IOException_HttpException
import com.example.myshopmanagerapp.core.Constants.ExceptionOrErrorMessage
import com.example.myshopmanagerapp.core.Constants.PersonnelEntityInfo
import com.example.myshopmanagerapp.core.Constants.Personnel_IsLoggedIn
import com.example.myshopmanagerapp.core.Constants.RevenueTypes
import com.example.myshopmanagerapp.core.Constants.ListOfShopLoginInfo
import com.example.myshopmanagerapp.core.Constants.PaymentMethod
import com.example.myshopmanagerapp.core.Constants.SupplierRole
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(private val context: Context) {
    // to make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            Constants.UserPreferences
        )
        val IS_LOGGED_IN = booleanPreferencesKey(IsLoggedIn)
        val PERSONNEL_IS_LOGGED_IN = booleanPreferencesKey(Personnel_IsLoggedIn)
        val IS_REGISTERED = booleanPreferencesKey(IsRegistered)
        val THERE_IS_IO_EXCEPTION_HTTP_EXCEPTION = booleanPreferencesKey(IOException_HttpException)
        val EXCEPTION_OR_ERROR_MESSAGE = stringPreferencesKey(ExceptionOrErrorMessage)
        val LIST_OF_SHOP_LOGIN_INFO = stringPreferencesKey(ListOfShopLoginInfo)
        val PERSONNEL_ENTITY_INFO = stringPreferencesKey(PersonnelEntityInfo)
        val API_RESPONSE_MESSAGE = stringPreferencesKey(RegisterMessage)
        val SHOP_INFO = stringPreferencesKey(ShopInfo)
        val CURRENCY = stringPreferencesKey(Currency)
        val MANUFACTURER_NAME = stringPreferencesKey(ManufacturerName)
        val BANK_PERSONNEL = stringPreferencesKey(BankPersonnel)
        val PERSONNEL_ROLES = stringPreferencesKey(PersonnelRoles)
        val PAYMENT_METHOD = stringPreferencesKey(PaymentMethod)
        val EXPENSE_NAMES = stringPreferencesKey(ExpenseNames)
        val EXPENSE_TYPES = stringPreferencesKey(ExpenseTypes)
        val REVENUE_TYPES = stringPreferencesKey(RevenueTypes)
        val ITEM_CATEGORY = stringPreferencesKey(ItemCategory)
        val SUPPLIER_ROLE = stringPreferencesKey(SupplierRole)
        val STRING_VALUE = stringPreferencesKey(StringValue)
        val DOUBLE_VALUE = doublePreferencesKey(DoubleValue)
        val INT_VALUE = intPreferencesKey(IntValue)
    }

    //get the Log in Status
    val getListOfShopLogInInfo: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[LIST_OF_SHOP_LOGIN_INFO] ?: emptyString }

    //save the Log in status
    suspend fun saveListOfShopLogInInfo(value: String) {
        context.dataStore.edit { preferences -> preferences[LIST_OF_SHOP_LOGIN_INFO] = value }
    }

    //get the Log in Status
    val getLoggedInState: Flow<Boolean?> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    //save the Log in status
    suspend fun savePersonnelLoggedInState(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[PERSONNEL_IS_LOGGED_IN] = value }
    }

    //get the Personnel Log in Status
    val getPersonnelLoggedInState: Flow<Boolean?> = context.dataStore.data
        .map { preferences -> preferences[PERSONNEL_IS_LOGGED_IN] ?: false }

    //save the Personnel Log in status
    suspend fun saveLoggedInState(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[IS_LOGGED_IN] = value }
    }

    //get the Log in Status
    val getIOExceptionOrHttpExceptionState: Flow<Boolean?> = context.dataStore.data
        .map { preferences -> preferences[THERE_IS_IO_EXCEPTION_HTTP_EXCEPTION] ?: false }

    //save the Log in status
    suspend fun saveIOExceptionOrHttpExceptionState(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[THERE_IS_IO_EXCEPTION_HTTP_EXCEPTION] = value }
    }

    //get the Log in Status
    val getRepositoryJobSuccessState: Flow<Boolean?> = context.dataStore.data
        .map { preferences -> preferences[IS_REGISTERED] ?: false }

    //save the Log in status
    suspend fun saveRepositoryJobSuccessValue(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[IS_REGISTERED] = value }
    }

    //get the shop info
    val getShopInfo: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[SHOP_INFO] ?: emptyString }

    //save Shop info
    suspend fun saveShopInfo(value: String) {
        context.dataStore.edit { preferences -> preferences[SHOP_INFO] = value }
    }

    //get the personnel Info
    val getPersonnelInfo: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PERSONNEL_ENTITY_INFO] ?: emptyString }

    //save Personnel info
    suspend fun savePersonnelInfo(value: String) {
        context.dataStore.edit { preferences -> preferences[PERSONNEL_ENTITY_INFO] = value }
    }

    //get the currency
    val getCurrency: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[CURRENCY] ?: emptyString }

    //save the currency
    suspend fun saveCurrency(value: String) {
        context.dataStore.edit { preferences -> preferences[CURRENCY] = value }
    }

    //get Supplier Role
    val getSupplierRole: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[SUPPLIER_ROLE] ?: emptyString }

    //save Supplier Role
    suspend fun saveSupplierRole(value: String) {
        context.dataStore.edit { preferences -> preferences[SUPPLIER_ROLE] = value }
    }

    //get the Payment Method
    val getPaymentMethod: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PAYMENT_METHOD] ?: emptyString }

    //save the Payment Method
    suspend fun savePaymentMethod(value: String) {
        context.dataStore.edit { preferences -> preferences[PAYMENT_METHOD] = value }
    }

    //get the Manufacturer
    val getManufacturers: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[MANUFACTURER_NAME] ?: emptyString }

    //save the Manufacturer
    suspend fun saveManufacturers(value: String) {
        context.dataStore.edit { preferences -> preferences[MANUFACTURER_NAME] = value }
    }

    //get the API response
    val getRepositoryJobMessage: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[API_RESPONSE_MESSAGE].toNotNull() }

    //save the API response
    suspend fun saveRepositoryJobMessage(value: String) {
        context.dataStore.edit { preferences -> preferences[API_RESPONSE_MESSAGE] = value }
    }

    //get the API response
    val getExceptionOrErrorMessage: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[EXCEPTION_OR_ERROR_MESSAGE].toNotNull() }

    //save the API response
    suspend fun saveExceptionOrErrorMessage(value: String) {
        context.dataStore.edit { preferences -> preferences[EXCEPTION_OR_ERROR_MESSAGE] = value }
    }

    //get the Bank Personnel
    val getBankPersonnel: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[BANK_PERSONNEL] ?: emptyString }

    //save the Bank Personnel
    suspend fun saveBankPersonnel(value: String) {
        context.dataStore.edit { preferences -> preferences[BANK_PERSONNEL] = value }
    }

    //get the Categories
    val getItemCategories: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[ITEM_CATEGORY] ?: emptyString }

    //save the Item Categories
    suspend fun saveItemCategories(value: String) {
        context.dataStore.edit { preferences -> preferences[ITEM_CATEGORY] = value }
    }


    //get the Expense Names
    val getExpenseNames: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[EXPENSE_NAMES] ?: emptyString }

    //save the Expense Names
    suspend fun saveExpenseNames(value: String) {
        context.dataStore.edit { preferences -> preferences[EXPENSE_NAMES] = value }
    }

    //get the Expense Types
    val getExpenseTypes: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[EXPENSE_TYPES] ?: emptyString }

    //save the Expense Types
    suspend fun saveExpenseTypes(value: String) {
        context.dataStore.edit { preferences -> preferences[EXPENSE_TYPES] = value }
    }

    //get the Revenue Types
    val getRevenueTypes: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[REVENUE_TYPES] ?: emptyString }

    //save the Revenue Types
    suspend fun saveRevenueTypes(value: String) {
        context.dataStore.edit { preferences -> preferences[REVENUE_TYPES] = value }
    }

    //get the Personnel Roles
    val getPersonnelRoles: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PERSONNEL_ROLES] ?: emptyString }

    //save the Personnel Roles
    suspend fun savePersonnelRoles(value: String) {
        context.dataStore.edit { preferences -> preferences[PERSONNEL_ROLES] = value }
    }

    //get the String Value
    val getStringValue: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[STRING_VALUE] ?: emptyString }

    //save the String value
    suspend fun saveStringValue(value: String) {
        context.dataStore.edit { preferences -> preferences[STRING_VALUE] = value }
    }



    //get the Double Value
    val getDoubleValue: Flow<Double?> = context.dataStore.data
        .map { preferences -> preferences[DOUBLE_VALUE] ?: 0.0 }

    //save the Double value
    suspend fun saveDoubleValue(value: Double) {
        context.dataStore.edit { preferences -> preferences[DOUBLE_VALUE] = value }
    }

    //get the Int Value
    val getIntValue: Flow<Int?> = context.dataStore.data
        .map { preferences -> preferences[INT_VALUE] ?: 0 }

    //save the Int value
    suspend fun saveIntValue(number: Int) {
        context.dataStore.edit { preferences -> preferences[INT_VALUE] = number }
    }


}