package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.InventoryEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityCategorization
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import kotlinx.coroutines.flow.Flow
import java.util.*


interface InventoryRepository {

    fun getAllInventories(): Flow<Resource<InventoryEntities?>>

    suspend fun addInventoryWithStock(inventory: InventoryEntity): Flow<Resource<String?>>

    suspend fun addInventory(inventory: InventoryEntity)

    suspend fun addInventories(inventories: InventoryEntities)

    suspend fun getInventory(uniqueInventoryId: String): InventoryEntity?

    suspend fun updateInventory(inventory: InventoryEntity)

    suspend fun updateInventoryWithStock(inventory: InventoryEntity): Flow<Resource<String?>>

    //suspend fun getStock(date: Date, uniqueItemId: String, stockQuantityInfo: ItemQuantityCategorization?): StockEntity?

    suspend fun deleteInventory(inventoryId: Int)

    suspend fun deleteInventory(uniqueInventoryId: String)

    suspend fun deleteInventoryWithStock(uniqueInventoryId: String): Flow<Resource<String?>>

    suspend fun deleteAllInventories()

    suspend fun getItemsAndTheirQuantities(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>

    suspend fun getTotalNumberOfItems(period: PeriodDropDownItem): Flow<Resource<ItemValue>>

    suspend fun getTotalCostValue(period: PeriodDropDownItem): Flow<Resource<ItemValue>>

    suspend fun getTotalExpectedSalesValue(period: PeriodDropDownItem): Flow<Resource<ItemValue>>

    suspend fun getMostAvailableItem(period: PeriodDropDownItem): Flow<Resource<ItemValue>>

    suspend fun getLeastAvailableItem(period: PeriodDropDownItem): Flow<Resource<ItemValue>>

}
