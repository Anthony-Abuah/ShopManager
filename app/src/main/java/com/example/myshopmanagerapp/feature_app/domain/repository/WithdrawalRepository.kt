package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.WithdrawalEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import kotlinx.coroutines.flow.Flow


interface WithdrawalRepository {

    fun getAllWithdrawals(): Flow<Resource<WithdrawalEntities?>>

    suspend fun addWithdrawal(withdrawal: WithdrawalEntity): Flow<Resource<String?>>

    suspend fun addWithdrawals(withdrawals: WithdrawalEntities)

    suspend fun getWithdrawal(uniqueWithdrawalId: String): WithdrawalEntity?

    suspend fun updateWithdrawal(withdrawal: WithdrawalEntity): Flow<Resource<String?>>

    suspend fun deleteWithdrawal(withdrawalId: Int)

    suspend fun deleteWithdrawal(uniqueWithdrawalId: String): Flow<Resource<String?>>

    suspend fun deleteAllWithdrawals()

    suspend fun generateWithdrawalsList(context: Context, withdrawals: WithdrawalEntities, mapOfBankAccounts: Map<String, String>): Flow<Resource<String?>>

    suspend fun getPeriodicWithdrawalAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>

}
