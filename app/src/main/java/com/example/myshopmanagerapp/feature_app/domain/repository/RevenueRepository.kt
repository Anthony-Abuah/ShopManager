package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.RevenueEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import kotlinx.coroutines.flow.Flow


interface RevenueRepository {

    fun getAllRevenues(): Flow<Resource<RevenueEntities?>>

    suspend fun addRevenue(revenue: RevenueEntity): Flow<Resource<String?>>

    suspend fun addRevenues(revenues: RevenueEntities)

    suspend fun getRevenue(uniqueRevenueId: String): RevenueEntity?

    suspend fun updateRevenue(revenue: RevenueEntity): Flow<Resource<String?>>

    suspend fun deleteRevenue(revenueId: Int)

    suspend fun deleteRevenue(uniqueRevenueId: String): Flow<Resource<String?>>

    suspend fun deleteAllRevenues()

    suspend fun generateRevenueList(context: Context, revenues: RevenueEntities): Flow<Resource<String?>>


    suspend fun getRevenueAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getExpenseAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getDebtAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getDebtRepaymentAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getRevenueDays(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getRevenueHours(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getMaximumRevenueDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getMinimumRevenueDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getMaximumExpenseDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getMinimumExpenseDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getCostOfInventory(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>

}
