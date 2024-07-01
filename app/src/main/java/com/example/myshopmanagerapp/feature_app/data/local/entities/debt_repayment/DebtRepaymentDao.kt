package com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.DebtRepayment_Table
import com.example.myshopmanagerapp.core.DebtRepaymentEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import java.util.*

@Dao
interface DebtRepaymentDao {

    @Query ("SELECT * FROM $DebtRepayment_Table")
    suspend fun getAllDebtRepayment(): DebtRepaymentEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebtRepayment(debtRepayment: DebtRepaymentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebtRepayments(debtRepayments: DebtRepaymentEntities)

    @Query ("SELECT * FROM $DebtRepayment_Table WHERE uniqueDebtRepaymentId LIKE :uniqueDebtRepaymentId")
    suspend fun getDebtRepayment(uniqueDebtRepaymentId: String): DebtRepaymentEntity?

    @Query ("SELECT * FROM $DebtRepayment_Table WHERE uniqueCustomerId LIKE :uniqueCustomerId")
    suspend fun getCustomerDebtRepayment(uniqueCustomerId: String): DebtRepaymentEntities?

    @Query ("SELECT * FROM $DebtRepayment_Table WHERE uniquePersonnelId LIKE :uniquePersonnelId")
    suspend fun getPersonnelDebtRepayment(uniquePersonnelId: String): DebtRepaymentEntities?

    @Query ("DELETE FROM $DebtRepayment_Table")
    suspend fun deleteAllDebtRepayments()

    @Query ("DELETE FROM $DebtRepayment_Table WHERE uniqueDebtRepaymentId LIKE :uniqueDebtRepaymentId")
    suspend fun deleteDebtRepayment(uniqueDebtRepaymentId: String)

    @Query ("DELETE FROM $DebtRepayment_Table WHERE debtRepaymentId LIKE :debtRepaymentId")
    suspend fun deleteDebtRepayment(debtRepaymentId: Int)

    @Query ("DELETE FROM $DebtRepayment_Table WHERE debtRepaymentId NOT IN (SELECT MIN(debtRepaymentId) FROM $DebtRepayment_Table GROUP BY uniqueDebtRepaymentId)")
    suspend fun deleteDuplicateDebtRepayments()

    @Update
    suspend fun updateDebtRepayment(debtRepayment: DebtRepaymentEntity)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Transaction
    suspend fun insertDebtRepayment(debtRepayment: DebtRepaymentEntity, customer: CustomerEntity){
        addDebtRepayment(debtRepayment)
        updateCustomer(customer)
    }
    @Transaction
    suspend fun updateDebtRepayment(debtRepayment: DebtRepaymentEntity, customer: CustomerEntity){
        updateDebtRepayment(debtRepayment)
        updateCustomer(customer)
    }
    @Transaction
    suspend fun deleteDebtRepayment(uniqueDebtRepaymentId: String, customer: CustomerEntity){
        deleteDebtRepayment(uniqueDebtRepaymentId)
        updateCustomer(customer)
    }

}