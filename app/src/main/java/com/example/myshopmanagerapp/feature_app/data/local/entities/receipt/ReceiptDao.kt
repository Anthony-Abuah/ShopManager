package com.example.myshopmanagerapp.feature_app.data.local.entities.receipt

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.Receipt_Table
import com.example.myshopmanagerapp.core.ReceiptEntities
import java.util.*


@Dao
interface ReceiptDao {

    @Query ("SELECT * FROM $Receipt_Table")
    suspend fun getAllReceipts(): ReceiptEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReceipt(receipt: ReceiptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReceipts(receipts: ReceiptEntities)

    @Query ("SELECT * FROM $Receipt_Table WHERE uniqueReceiptId LIKE :uniqueReceiptId")
    suspend fun getReceipt(uniqueReceiptId: String): ReceiptEntity?

    @Query ("DELETE FROM $Receipt_Table")
    suspend fun deleteAllReceipts()

    @Query ("DELETE FROM $Receipt_Table WHERE uniqueReceiptId LIKE :uniqueReceiptId")
    suspend fun deleteReceipt(uniqueReceiptId: String)

    @Query ("DELETE FROM $Receipt_Table WHERE receiptId LIKE :receiptId")
    suspend fun deleteReceipt(receiptId: Int)

    @Query ("DELETE FROM $Receipt_Table WHERE receiptId NOT IN (SELECT MIN(receiptId) FROM $Receipt_Table GROUP BY uniqueReceiptId)")
    suspend fun deleteDuplicateReceipts()

    @Update
    suspend fun updateReceipt(receipt: ReceiptEntity)

}