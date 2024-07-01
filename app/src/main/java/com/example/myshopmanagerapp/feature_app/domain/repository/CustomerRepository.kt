package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.CustomerEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import kotlinx.coroutines.flow.Flow


interface CustomerRepository {

    fun getAllCustomers(): Flow<Resource<CustomerEntities?>>

    suspend fun addCustomer(customer: CustomerEntity): Flow<Resource<String?>>

    suspend fun addCustomers(customers: CustomerEntities)

    suspend fun getCustomer(uniqueCustomerId: String): CustomerEntity?

    suspend fun getCustomer(uniqueCustomerId: Int): CustomerEntity?

    suspend fun getCustomerByName(customerName: String): CustomerEntities?

    suspend fun updateCustomer(customer: CustomerEntity): Flow<Resource<String?>>

    suspend fun updateCustomerDebt(uniqueCustomerId: String, debtAmount: Double)

    suspend fun deleteCustomer(customerId: Int): Flow<Resource<String?>>

    suspend fun deleteCustomer(uniqueCustomerId: String): Flow<Resource<String?>>

    suspend fun deleteAllCustomers()



}
