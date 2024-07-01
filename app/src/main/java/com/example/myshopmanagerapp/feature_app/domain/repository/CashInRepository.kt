package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.CashInEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInEntity
import kotlinx.coroutines.flow.Flow


interface CashInRepository {

    fun getAllCashIns(): Flow<Resource<CashInEntities?>>

    suspend fun addCashIn(cashIn: CashInEntity): Flow<Resource<String?>>

    suspend fun addCashIns(cashIns: CashInEntities)

    suspend fun getCashIn(uniqueCashInId: String): CashInEntity?

    suspend fun updateCashIn(cashIn: CashInEntity): Flow<Resource<String?>>

    suspend fun deleteCashIn(uniqueCashInId: String): Flow<Resource<String?>>

    suspend fun deleteAllCashIns()

}
