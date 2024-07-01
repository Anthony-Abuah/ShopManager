package com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.InventoryItem_Table
import com.example.myshopmanagerapp.core.InventoryItemEntities
import java.util.*

@Dao
interface InventoryItemDao {

    @Query ("SELECT * FROM $InventoryItem_Table")
    suspend fun getAllInventoryItems(): InventoryItemEntities?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventoryItem(inventoryItem: InventoryItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventoryItems(inventoryItems: InventoryItemEntities)

    @Query ("SELECT * FROM $InventoryItem_Table WHERE uniqueInventoryItemId LIKE :uniqueInventoryItemId")
    suspend fun getInventoryItem(uniqueInventoryItemId: String): InventoryItemEntity?

    @Query ("DELETE FROM $InventoryItem_Table")
    suspend fun deleteAllInventoryItems()

    @Query ("DELETE FROM $InventoryItem_Table WHERE uniqueInventoryItemId LIKE :uniqueInventoryItemId")
    suspend fun deleteInventoryItem(uniqueInventoryItemId: String)

    @Query ("DELETE FROM $InventoryItem_Table WHERE inventoryItemId NOT IN (SELECT MIN(inventoryItemId) FROM $InventoryItem_Table GROUP BY uniqueInventoryItemId)")
    suspend fun deleteDuplicateInventoryItems()

    @Update
    suspend fun updateInventoryItem(inventoryItem: InventoryItemEntity)

}