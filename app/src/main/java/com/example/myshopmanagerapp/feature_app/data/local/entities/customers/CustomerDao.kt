package com.example.myshopmanagerapp.feature_app.data.local.entities.customers

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.Customer_Table
import com.example.myshopmanagerapp.core.CustomerEntities
import java.util.*

@Dao
interface CustomerDao {

    @Query ("SELECT * FROM $Customer_Table")
    suspend fun getAllCustomers(): CustomerEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomer(customer: CustomerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomers(customers: CustomerEntities)

    @Query ("DELETE FROM $Customer_Table")
    suspend fun deleteAllCustomers()

    @Query ("DELETE FROM $Customer_Table WHERE uniqueCustomerId LIKE :uniqueCustomerId")
    suspend fun deleteCustomer(uniqueCustomerId: String)

    @Query ("DELETE FROM $Customer_Table WHERE customerId LIKE :customerId")
    suspend fun deleteCustomer(customerId: Int)

    @Query ("DELETE FROM $Customer_Table WHERE customerId NOT IN (SELECT MIN(customerId) FROM $Customer_Table GROUP BY uniqueCustomerId)")
    suspend fun deleteDuplicateCustomers()

    @Query ("SELECT * FROM $Customer_Table WHERE uniqueCustomerId LIKE :uniqueCustomerId")
    suspend fun getCustomer(uniqueCustomerId: String): CustomerEntity?

    @Query ("SELECT * FROM $Customer_Table WHERE customerId LIKE :customerId")
    suspend fun getCustomer(customerId: Int): CustomerEntity?

    @Query ("SELECT * FROM $Customer_Table WHERE customerName LIKE :customerName")
    suspend fun getCustomerByName(customerName: String): List<CustomerEntity>?

    @Query("UPDATE $Customer_Table SET debtAmount=:debtAmount WHERE uniqueCustomerId LIKE :uniqueCustomerId")
    suspend fun updateCustomerDebt(uniqueCustomerId: String, debtAmount: Double)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)


}