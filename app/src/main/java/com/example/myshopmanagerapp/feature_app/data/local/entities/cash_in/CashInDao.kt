package com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in

import androidx.room.*
import com.example.myshopmanagerapp.core.CashInEntities
import com.example.myshopmanagerapp.core.Constants.CashIn_Table
import java.util.*

@Dao
interface CashInDao {

    @Query ("SELECT * FROM $CashIn_Table")
    suspend fun getAllCashIns(): CashInEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCashIn(cashIn: CashInEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCashIns(cashIns: List<CashInEntity>)

    @Query ("SELECT * FROM $CashIn_Table WHERE uniqueCashInId LIKE :uniqueCashInId")
    suspend fun getCashIn(uniqueCashInId: String): CashInEntity?

    @Query ("DELETE FROM $CashIn_Table")
    suspend fun deleteAllCashIns()

    @Query ("DELETE FROM $CashIn_Table WHERE uniqueCashInId LIKE :uniqueCashInId")
    suspend fun deleteCashIn(uniqueCashInId: String)

    @Update
    suspend fun updateCashIn(cashIn: CashInEntity)

}