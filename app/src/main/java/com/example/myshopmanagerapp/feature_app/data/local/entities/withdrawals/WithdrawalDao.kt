package com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.BankAccount_Table
import com.example.myshopmanagerapp.core.Constants.Withdrawal_Table
import com.example.myshopmanagerapp.core.WithdrawalEntities
import java.util.*

@Dao
interface WithdrawalDao {

    @Query ("SELECT * FROM $Withdrawal_Table")
    suspend fun getAllWithdrawals(): WithdrawalEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWithdrawal(withdrawal: WithdrawalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWithdrawals(withdrawals: WithdrawalEntities)

    @Query ("SELECT * FROM $Withdrawal_Table WHERE uniqueWithdrawalId LIKE :uniqueWithdrawalId")
    suspend fun getWithdrawal(uniqueWithdrawalId: String): WithdrawalEntity?

    @Query ("DELETE FROM $Withdrawal_Table")
    suspend fun deleteAllWithdrawals()

    @Query ("DELETE FROM $Withdrawal_Table WHERE uniqueWithdrawalId LIKE :uniqueWithdrawalId")
    suspend fun deleteWithdrawal(uniqueWithdrawalId: String)

    @Query ("DELETE FROM $Withdrawal_Table WHERE withdrawalId LIKE :withdrawalId")
    suspend fun deleteWithdrawal(withdrawalId: Int)

    @Query("UPDATE $BankAccount_Table SET accountBalance=:accountBalance WHERE uniqueBankAccountId LIKE :uniqueBankAccountId")
    suspend fun updateBankSavingsAmount(uniqueBankAccountId: String, accountBalance: Double)

    @Query ("DELETE FROM $Withdrawal_Table WHERE withdrawalId NOT IN (SELECT MIN(withdrawalId) FROM $Withdrawal_Table GROUP BY uniqueWithdrawalId)")
    suspend fun deleteDuplicateWithdrawals()

    @Update
    suspend fun updateWithdrawal(withdrawal: WithdrawalEntity)

    @Transaction
    suspend fun insertWithdrawal(withdrawal: WithdrawalEntity, uniqueBankId: String, accountBalance: Double){
        addWithdrawal(withdrawal)
        updateBankSavingsAmount(uniqueBankId, accountBalance)
    }

    @Transaction
    suspend fun updateWithdrawal(withdrawal: WithdrawalEntity, uniqueBankAccountId: String, accountBalance: Double){
        updateWithdrawal(withdrawal)
        updateBankSavingsAmount(uniqueBankAccountId, accountBalance)
    }

    @Transaction
    suspend fun deleteWithdrawal(uniqueWithdrawalId: String, uniqueBankAccountId: String, accountBalance: Double){
        deleteWithdrawal(uniqueWithdrawalId)
        updateBankSavingsAmount(uniqueBankAccountId, accountBalance)
    }



}