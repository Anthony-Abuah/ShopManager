package com.example.myshopmanagerapp.feature_app.data.local.entities.inventory

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.InventoryStock_Table
import com.example.myshopmanagerapp.core.Constants.Inventory_Table
import com.example.myshopmanagerapp.core.Constants.Stock_Table
import com.example.myshopmanagerapp.core.InventoryEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import java.util.*

@Dao
interface InventoryDao {

    @Query ("SELECT * FROM $Inventory_Table")
    suspend fun getAllInventories(): InventoryEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStock(stock: StockEntity)

    @Update
    suspend fun updateStock(stock: StockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventory(inventory: InventoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventoryStock(inventoryStock: InventoryStockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventories(inventories: InventoryEntities)

    @Query ("SELECT * FROM $Inventory_Table WHERE uniqueInventoryId LIKE :uniqueInventoryId")
    suspend fun getInventory(uniqueInventoryId: String): InventoryEntity?

    @Query ("SELECT * FROM $Inventory_Table WHERE uniqueInventoryItemId LIKE :uniqueInventoryItemId")
    suspend fun getItemInventories(uniqueInventoryItemId: String): InventoryEntities?

    @Query ("DELETE FROM $Inventory_Table")
    suspend fun deleteAllInventories()

    @Query ("DELETE FROM $Inventory_Table WHERE uniqueInventoryId LIKE :uniqueInventoryId")
    suspend fun deleteInventory(uniqueInventoryId: String)

    @Query ("DELETE FROM $Stock_Table WHERE uniqueStockId LIKE :uniqueStockId")
    suspend fun deleteStock(uniqueStockId: String)

    @Query ("DELETE FROM $InventoryStock_Table WHERE uniqueInventoryStockId LIKE :uniqueInventoryStockId")
    suspend fun deleteInventoryStock(uniqueInventoryStockId: String)

    @Query ("DELETE FROM $Inventory_Table WHERE inventoryId LIKE :inventoryId")
    suspend fun deleteInventory(inventoryId: Int)

    @Query ("DELETE FROM $Inventory_Table WHERE inventoryId NOT IN (SELECT MIN(inventoryId) FROM $Inventory_Table GROUP BY uniqueInventoryId)")
    suspend fun deleteDuplicateInventories()

    @Update
    suspend fun updateInventory(inventory: InventoryEntity)

    @Update
    suspend fun updateInventoryItem(inventoryItem: InventoryItemEntity)

    @Transaction
    suspend fun addInventoryWithStockThatExists(stock: StockEntity, inventory: InventoryEntity, inventoryItem: InventoryItemEntity){
        updateStock(stock)
        updateInventoryItem(inventoryItem)
        addInventory(inventory)
    }

    @Transaction
    suspend fun addInventoryWithStock(stock: StockEntity, inventory: InventoryEntity, inventoryStock: InventoryStockEntity, inventoryItem: InventoryItemEntity){
        updateInventoryItem(inventoryItem)
        addStock(stock)
        addInventory(inventory)
        addInventoryStock(inventoryStock)
    }

    @Transaction
    suspend fun updateInventoryWithStock(stock: StockEntity, inventory: InventoryEntity, inventoryItem: InventoryItemEntity){
        updateInventoryItem(inventoryItem)
        updateStock(stock)
        updateInventory(inventory)
    }

    @Transaction
    suspend fun deleteInventoryWithStock(uniqueStockId: String, uniqueInventoryId: String, uniqueInventoryStockId: String, inventoryItem: InventoryItemEntity){
        deleteStock(uniqueStockId)
        deleteInventory(uniqueInventoryId)
        deleteInventoryStock(uniqueInventoryStockId)
        updateInventoryItem(inventoryItem)
    }

    @Transaction
    suspend fun deleteSomeInventoryFromStock(stock: StockEntity, uniqueInventoryId: String, inventoryItem: InventoryItemEntity){
        updateInventoryItem(inventoryItem)
        updateStock(stock)
        deleteInventory(uniqueInventoryId)
    }


}