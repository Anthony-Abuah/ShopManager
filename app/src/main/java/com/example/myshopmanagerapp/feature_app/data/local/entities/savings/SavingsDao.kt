package com.example.myshopmanagerapp.feature_app.data.local.entities.savings

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.BankAccount_Table
import com.example.myshopmanagerapp.core.Constants.Savings_Table
import com.example.myshopmanagerapp.core.SavingsEntities
import java.util.*

@Dao
interface SavingsDao {

    @Query ("SELECT * FROM $Savings_Table")
    suspend fun getAllSavings(): SavingsEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSavings(savings: SavingsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSavings(savings: SavingsEntities)

    @Query ("SELECT * FROM $Savings_Table WHERE uniqueSavingsId LIKE :uniqueSavingsId")
    suspend fun getSavings(uniqueSavingsId: String): SavingsEntity?

    @Query ("DELETE FROM $Savings_Table")
    suspend fun deleteAllSavings()

    @Query ("DELETE FROM $Savings_Table WHERE uniqueSavingsId LIKE :uniqueSavingsId")
    suspend fun deleteSavings(uniqueSavingsId: String)

    @Query ("DELETE FROM $Savings_Table WHERE savingsId LIKE :savingsId")
    suspend fun deleteSavings(savingsId: Int)

    @Query ("DELETE FROM $Savings_Table WHERE savingsId NOT IN (SELECT MIN(savingsId) FROM $Savings_Table GROUP BY uniqueSavingsId)")
    suspend fun deleteDuplicateSavings()

    @Query("UPDATE $BankAccount_Table SET accountBalance=:accountBalance WHERE uniqueBankAccountId LIKE :uniqueBankAccountId")
    suspend fun updateBankSavingsAmount(uniqueBankAccountId: String, accountBalance: Double)

    @Update
    suspend fun updateSavings(savings: SavingsEntity)

    @Transaction
    suspend fun insertSavings(savings: SavingsEntity, uniqueBankId: String, accountBalance: Double){
        addSavings(savings)
        updateBankSavingsAmount(uniqueBankId, accountBalance)
    }

    @Transaction
    suspend fun updateSavings(savings: SavingsEntity, uniqueBankId: String, savingsAmount: Double){
        updateSavings(savings)
        updateBankSavingsAmount(uniqueBankId, savingsAmount)
    }


    @Transaction
    suspend fun deleteSavings(uniqueSavingsId: String, uniqueBankId: String, savingsAmount: Double){
        deleteSavings(uniqueSavingsId)
        updateBankSavingsAmount(uniqueBankId, savingsAmount)
    }


}