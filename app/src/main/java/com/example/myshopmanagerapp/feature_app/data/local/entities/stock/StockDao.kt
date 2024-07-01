package com.example.myshopmanagerapp.feature_app.data.local.entities.stock

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.InventoryItem_Table
import com.example.myshopmanagerapp.core.Constants.Stock_Table
import com.example.myshopmanagerapp.core.StockEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import java.util.*


@Dao
interface StockDao {
    @Query ("SELECT * FROM $Stock_Table")
    suspend fun getAllStocks(): StockEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStock(stock: StockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStocks(stocks: StockEntities)

    @Query ("SELECT * FROM $Stock_Table WHERE uniqueInventoryItemId LIKE :uniqueInventoryItemId")
    suspend fun getItemStocks(uniqueInventoryItemId: String): StockEntities?

    @Query ("SELECT * FROM $Stock_Table WHERE uniqueStockId LIKE :uniqueStockId")
    suspend fun getStock(uniqueStockId: String): StockEntity?

    @Query ("SELECT * FROM $InventoryItem_Table WHERE uniqueInventoryItemId LIKE :uniqueInventoryItemId")
    suspend fun getInventoryItem(uniqueInventoryItemId: String): InventoryItemEntity?

    @Query ("DELETE FROM $Stock_Table")
    suspend fun deleteAllStocks()

    @Query ("DELETE FROM $Stock_Table WHERE uniqueStockId LIKE :uniqueStockId")
    suspend fun deleteStock(uniqueStockId: String)

    @Query ("DELETE FROM $Stock_Table WHERE stockId LIKE :stockId")
    suspend fun deleteStock(stockId: Int)

    @Query ("DELETE FROM $Stock_Table WHERE stockId NOT IN (SELECT MIN(stockId) FROM $Stock_Table GROUP BY uniqueStockId)")
    suspend fun deleteDuplicateStocks()

    @Update
    suspend fun updateStock(stock: StockEntity)

    @Update
    suspend fun updateInventoryItem(inventoryItem: InventoryItemEntity)

    @Transaction
    suspend fun addStockWithInventoryItemUpdate(stock: StockEntity, inventoryItem: InventoryItemEntity){
        addStock(stock)
        updateInventoryItem(inventoryItem)
    }

    @Transaction
    suspend fun updateStockWithInventoryItemUpdate(stock: StockEntity, inventoryItem: InventoryItemEntity){
        updateStock(stock)
        updateInventoryItem(inventoryItem)
    }

    @Transaction
    suspend fun deleteStockWithItemUpdate(uniqueStockId: String, inventoryItem: InventoryItemEntity){
        deleteStock(uniqueStockId)
        updateInventoryItem(inventoryItem)
    }
}