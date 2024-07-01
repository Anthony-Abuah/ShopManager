package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.SavingsEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity
import kotlinx.coroutines.flow.Flow


interface SavingsRepository {

    fun getAllSavings(): Flow<Resource<SavingsEntities?>>

    suspend fun addSavings(savings: SavingsEntity): Flow<Resource<String?>>

    suspend fun addSavings(savings: SavingsEntities)

    suspend fun getSavings(uniqueSavingsId: String): SavingsEntity?

    suspend fun updateSavings(savings: SavingsEntity): Flow<Resource<String?>>

    suspend fun deleteSavings(savingsId: Int)

    suspend fun deleteSavings(uniqueSavingsId: String): Flow<Resource<String?>>

    suspend fun deleteAllSavings()

    suspend fun generateSavingsList(context: Context, savings: SavingsEntities, mapOfBankAccounts: Map<String, String>): Flow<Resource<String?>>

}
