package com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.InventoryStock_Table
import com.example.myshopmanagerapp.core.InventoryStockEntities
import java.util.*

@Dao
interface InventoryStockDao {

    @Query ("SELECT * FROM $InventoryStock_Table")
    suspend fun getAllInventoryStock(): InventoryStockEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventoryStock(inventoryStock: InventoryStockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventoryStock(inventoryStock: InventoryStockEntities)

    @Query ("SELECT * FROM $InventoryStock_Table WHERE uniqueStockId LIKE :uniqueStockId")
    suspend fun getInventoryStockByStockId(uniqueStockId: String): InventoryStockEntity?

    @Query ("SELECT * FROM $InventoryStock_Table WHERE uniqueInventoryId LIKE :uniqueInventoryId")
    suspend fun getInventoryStockByInventoryId(uniqueInventoryId: String): InventoryStockEntity?

    @Query ("SELECT * FROM $InventoryStock_Table WHERE uniqueInventoryStockId LIKE :uniqueInventoryStockId")
    suspend fun getInventoryStock(uniqueInventoryStockId: String): InventoryStockEntity?

    @Query ("DELETE FROM $InventoryStock_Table")
    suspend fun deleteAllInventoryStock()

    @Query ("DELETE FROM $InventoryStock_Table WHERE uniqueInventoryStockId LIKE :uniqueInventoryStockId")
    suspend fun deleteInventoryStock(uniqueInventoryStockId: String)

    @Update
    suspend fun updateInventoryStock(inventoryStock: InventoryStockEntity)


}