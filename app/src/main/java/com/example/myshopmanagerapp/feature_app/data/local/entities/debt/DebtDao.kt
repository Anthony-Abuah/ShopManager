package com.example.myshopmanagerapp.feature_app.data.local.entities.debt

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.Debt_Table
import com.example.myshopmanagerapp.core.DebtEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.domain.model.UpdateInventoryItem
import java.util.*
import kotlin.collections.List

@Dao
interface DebtDao {

    @Query ("SELECT * FROM $Debt_Table")
    suspend fun getAllDebt(): DebtEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebt(debt: DebtEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebts(debts: List<DebtEntity>)

    @Query ("SELECT * FROM $Debt_Table WHERE uniqueDebtId LIKE :uniqueDebtId")
    suspend fun getDebt(uniqueDebtId: String): DebtEntity?

    @Query ("SELECT * FROM $Debt_Table WHERE uniqueCustomerId LIKE :uniqueCustomerId")
    suspend fun getDebtByCustomer(uniqueCustomerId: String): DebtEntities?

    @Query ("SELECT * FROM $Debt_Table WHERE uniquePersonnelId LIKE :uniquePersonnelId")
    suspend fun getDebtToPersonnel(uniquePersonnelId: String): DebtEntities?

    @Query ("DELETE FROM $Debt_Table")
    suspend fun deleteAllDebts()

    @Query ("DELETE FROM $Debt_Table WHERE uniqueDebtId LIKE :uniqueDebtId")
    suspend fun deleteDebt(uniqueDebtId: String)

    @Query ("DELETE FROM $Debt_Table WHERE debtId LIKE :debtId")
    suspend fun deleteDebt(debtId: Int)

    @Query ("DELETE FROM $Debt_Table WHERE debtId NOT IN (SELECT MIN(debtId) FROM $Debt_Table GROUP BY uniqueDebtId)")
    suspend fun deleteDuplicateDebts()
    
    @Update
    suspend fun updateDebt(debt: DebtEntity)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)


    @Transaction
    suspend fun insertDebt(debt: DebtEntity, customer: CustomerEntity){
        updateCustomer(customer)
        addDebt(debt)
    }

    @Transaction
    suspend fun updateDebt(debt: DebtEntity, customer: CustomerEntity){
        updateCustomer(customer)
        updateDebt(debt)
    }

    @Transaction
    suspend fun deleteDebt(uniqueDebtId: String, customer: CustomerEntity){
        updateCustomer(customer)
        deleteDebt(uniqueDebtId)
    }


}