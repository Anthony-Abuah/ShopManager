package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.DebtRepaymentEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


interface DebtRepaymentRepository {

    fun getAllDebtRepayment(): Flow<Resource<DebtRepaymentEntities?>>

    suspend fun addDebtRepayment(debtRepayment: DebtRepaymentEntity): Flow<Resource<String?>>

    suspend fun addDebtRepayments(debtRepayments: DebtRepaymentEntities)

    suspend fun getDebtRepayment(uniqueDebtRepaymentId: String): DebtRepaymentEntity?

    suspend fun getDebtRepaymentByCustomer(uniqueCustomerId: String): DebtRepaymentEntities?

    suspend fun getDebtRepaymentToPersonnel(uniquePersonnelId: String): DebtRepaymentEntities?

    suspend fun updateDebtRepayment(debtRepayment: DebtRepaymentEntity): Flow<Resource<String?>>

    suspend fun deleteDebtRepayment(uniqueDebtRepaymentId: String): Flow<Resource<String?>>

    suspend fun deleteAllDebtRepayment()

    suspend fun generateDebtRepaymentList(context: Context, debtRepayments: DebtRepaymentEntities, mapOfCustomers: Map<String, String>): Flow<Resource<String?>>

}
