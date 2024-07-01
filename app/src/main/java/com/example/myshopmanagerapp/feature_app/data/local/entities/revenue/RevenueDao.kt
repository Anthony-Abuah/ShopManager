package com.example.myshopmanagerapp.feature_app.data.local.entities.revenue

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.Revenue_Table
import com.example.myshopmanagerapp.core.RevenueEntities
import java.util.*


@Dao
interface RevenueDao {

    @Query ("SELECT * FROM $Revenue_Table")
    suspend fun getAllRevenues(): RevenueEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRevenue(revenue: RevenueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRevenues(revenues: RevenueEntities)

    @Query ("SELECT * FROM $Revenue_Table WHERE uniqueRevenueId LIKE :uniqueRevenueId")
    suspend fun getRevenue(uniqueRevenueId: String): RevenueEntity?

    @Query ("DELETE FROM $Revenue_Table")
    suspend fun deleteAllRevenues()

    @Query ("DELETE FROM $Revenue_Table WHERE uniqueRevenueId LIKE :uniqueRevenueId")
    suspend fun deleteRevenue(uniqueRevenueId: String)

    @Query ("DELETE FROM $Revenue_Table WHERE revenueId LIKE :revenueId")
    suspend fun deleteRevenue(revenueId: Int)

    @Query ("DELETE FROM $Revenue_Table WHERE revenueId NOT IN (SELECT MIN(revenueId) FROM $Revenue_Table GROUP BY uniqueRevenueId)")
    suspend fun deleteDuplicateRevenues()

    @Update
    suspend fun updateRevenue(revenue: RevenueEntity)

}