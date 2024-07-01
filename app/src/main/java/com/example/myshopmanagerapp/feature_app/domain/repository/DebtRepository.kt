package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.DebtEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import kotlinx.coroutines.flow.Flow


interface DebtRepository {

    fun getAllDebt(): Flow<Resource<DebtEntities?>>

    suspend fun addDebt(debt: DebtEntity): Flow<Resource<String?>>

    suspend fun getDebt(uniqueDebtId: String): DebtEntity?

    suspend fun getDebtByCustomer(uniqueCustomerId: String): DebtEntities?

    suspend fun getCustomerDebtAmount(uniqueCustomerId: String): Double

    suspend fun getDebtToPersonnel(uniquePersonnelId: String): DebtEntities?

    suspend fun updateDebt(debt: DebtEntity): Flow<Resource<String?>>

    suspend fun deleteDebt(uniqueDebtId: String): Flow<Resource<String?>>

    suspend fun deleteAllDebt()

    suspend fun generateDebtList(context: Context, debts: DebtEntities, mapOfCustomers: Map<String, String>): Flow<Resource<String?>>

}
