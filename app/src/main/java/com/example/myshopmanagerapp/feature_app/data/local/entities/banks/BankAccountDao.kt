package com.example.myshopmanagerapp.feature_app.data.local.entities.banks

import androidx.room.*
import com.example.myshopmanagerapp.core.BankAccountEntities
import com.example.myshopmanagerapp.core.Constants.BankAccount_Table
import java.util.*

@Dao
interface BankAccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBank(bank: BankAccountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBankAccounts(banks: BankAccountEntities)


    @Query ("DELETE FROM $BankAccount_Table")
    suspend fun deleteAllBanks()


    @Query ("SELECT * FROM $BankAccount_Table")
    suspend fun getAllBankAccounts(): BankAccountEntities?

    @Query ("SELECT * FROM $BankAccount_Table WHERE uniqueBankAccountId LIKE :uniqueBankAccountId")
    suspend fun getBankAccount(uniqueBankAccountId: String): BankAccountEntity?

    @Query ("DELETE FROM $BankAccount_Table WHERE uniqueBankAccountId LIKE :uniqueBankAccountId")
    suspend fun deleteBank(uniqueBankAccountId: String)

    /*
    @Query ("DELETE FROM $BankAccount_Table WHERE bankId NOT IN (SELECT MIN(bankId) FROM $BankAccount_Table GROUP BY uniqueBankAccountId)")
    suspend fun deleteDuplicateBanks()
    */

    @Update
    suspend fun updateBank(bankAccount: BankAccountEntity)

}