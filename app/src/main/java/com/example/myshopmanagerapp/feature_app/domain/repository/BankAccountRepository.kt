package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.BankAccountEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import kotlinx.coroutines.flow.Flow


interface BankAccountRepository {

    fun getAllBanks(): Flow<Resource<BankAccountEntities?>>

    suspend fun addBankAccount(bankAccount: BankAccountEntity): Flow<Resource<String?>>

    suspend fun addBankAccounts(bankAccounts: BankAccountEntities)

    suspend fun getBankAccount(uniqueBankAccountId: String): BankAccountEntity?

    suspend fun updateBankAccount(bankAccount: BankAccountEntity): Flow<Resource<String?>>

    suspend fun deleteBankAccount(uniqueBankAccountId: String): Flow<Resource<String?>>

}
