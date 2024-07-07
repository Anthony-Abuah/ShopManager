package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*

class CustomerRepositoryImpl(
    private val appDatabase: AppDatabase,
): CustomerRepository{
    override fun getAllCustomers(): Flow<Resource<CustomerEntities?>> = flow{
        emit(Resource.Loading())
        val allCustomers: List<CustomerEntity>?
        try {
            allCustomers = appDatabase.customerDao.getAllCustomers()
            emit(Resource.Success(allCustomers))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Customers from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addCustomer(customer: CustomerEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val allCustomers = appDatabase.customerDao.getAllCustomers() ?: emptyList()
            val allCustomerNames = allCustomers.map { it.customerName.trim().lowercase(Locale.ROOT)}
            val name = customer.customerName.trim().lowercase(Locale.ROOT)
            
            when(true){
                (customer.customerName.isBlank() || customer.customerContact.isBlank())->{
                    emit(Resource.Error("Unable to add customer \nPlease ensure that the customer's route and contact are provided"))
                }
                (allCustomerNames.contains(name))->{
                    emit(Resource.Error("Unable to add customer \nCustomer with route, $name, already exists"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add customer." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add customer." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    appDatabase.customerDao.addCustomer(customer)
                    emit(Resource.Success("Customer successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add customer\nError Message: ${e.message}"))
        }
    }

    override suspend fun addCustomers(customers: CustomerEntities) {
        try {
            val allCustomers = appDatabase.customerDao.getAllCustomers() ?: emptyList()
            val allUniqueCustomerIds = allCustomers.map { it.uniqueCustomerId }
            val newCustomers = customers.filter { !allUniqueCustomerIds.contains(it.uniqueCustomerId) }
            appDatabase.customerDao.addCustomers(newCustomers)
        }catch (_: Exception){}
    }

    override suspend fun getCustomer(uniqueCustomerId: String): CustomerEntity? {
        return appDatabase.customerDao.getCustomer(uniqueCustomerId)
    }

    override suspend fun getCustomer(uniqueCustomerId: Int): CustomerEntity? {
        return appDatabase.customerDao.getCustomer(uniqueCustomerId)
    }

    override suspend fun getCustomerByName(customerName: String): CustomerEntities? {
        return appDatabase.customerDao.getCustomerByName(customerName)
    }

    override suspend fun updateCustomer(customer: CustomerEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val unUpdatedCustomer = appDatabase.customerDao.getCustomer(customer.uniqueCustomerId)
            val allCustomer = appDatabase.customerDao.getAllCustomers() ?: emptyList()
            val allCustomerNames = allCustomer.map { it.customerName.trim().lowercase(Locale.ROOT)}.minus(unUpdatedCustomer?.customerName?.trim()?.lowercase(Locale.ROOT)).filterNotNull()
            val name = customer.customerName.trim().lowercase(Locale.ROOT)

            when(true){
                (customer.customerName.isBlank() || customer.customerContact.isBlank())->{
                    emit(Resource.Error("Unable to add customer \nPlease ensure that the customer's route and contact are provided"))
                }
                (unUpdatedCustomer == null)->{
                    emit(Resource.Error("Unable to update customer \nCould not get the customer you want to update"))
                }
                (allCustomerNames.contains(name))->{
                    emit(Resource.Error("Unable to update customer \nCustomer with route: $name, already exists"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update customer." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update customer." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    appDatabase.customerDao.updateCustomer(customer)
                    val updatedCustomerIdsJson = UpdateEntityMarkers(context).getUpdatedCustomerId.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    UpdateEntityMarkers(context).saveUpdatedCustomerIds(updatedCustomerIds.toUniqueIdsJson())
                    emit(Resource.Success("Customer successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to update customer\nError Message: ${e.message}"))
        }
    }

    override suspend fun updateCustomerDebt(uniqueCustomerId: String, debtAmount: Double) {
        appDatabase.customerDao.updateCustomerDebt(uniqueCustomerId, debtAmount)
    }

    override suspend fun deleteCustomer(customerId: Int): Flow<Resource<String?>> = flow  {
        appDatabase.customerDao.deleteCustomer(customerId)
    }

    override suspend fun deleteCustomer(uniqueCustomerId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val customer = appDatabase.customerDao.getCustomer(uniqueCustomerId)
            val allDebts = appDatabase.debtDao.getAllDebt()?.map { it.uniqueCustomerId } ?: emptyList()
            val allDebtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment()?.map { it.uniqueCustomerId } ?: emptyList()

            when(true){
                (customer == null)->{
                    emit(Resource.Error("Unable to delete customer \nCould not get the customer you want to delete"))
                }
                (allDebts.contains(uniqueCustomerId) || allDebtRepayments.contains(uniqueCustomerId))->{
                    emit(Resource.Error("Could not delete customer \nThis customer is already associated with debts and debt repayments" +
                            "\nTo delete customer, you have to delete all the records this customer is associated with"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete customer." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete customer." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete customer." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else->{
                    appDatabase.customerDao.deleteCustomer(uniqueCustomerId)
                    val deletedCustomerIdsJson = DeleteEntityMarkers(context).getDeletedCustomerId.first().toNotNull()
                    val deletedCustomerIds = deletedCustomerIdsJson.toUniqueIds().plus(UniqueId(uniqueCustomerId)).toSet().toList()
                    DeleteEntityMarkers(context).saveDeletedCustomerIds(deletedCustomerIds.toUniqueIdsJson())
                    emit(Resource.Success("Customer successfully deleted"))
                }
            }
            
        }catch (e: Exception){
            emit(Resource.Error("Unable to delete customer\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllCustomers() {
        appDatabase.customerDao.deleteAllCustomers()
    }
}