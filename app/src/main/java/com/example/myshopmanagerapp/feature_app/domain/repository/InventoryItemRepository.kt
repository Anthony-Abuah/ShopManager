package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.InventoryItemEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import kotlinx.coroutines.flow.Flow


interface InventoryItemRepository {

    fun getAllInventoryItems(): Flow<Resource<InventoryItemEntities?>>

    fun getShopItemCostValues(): Flow<Resource<List<ItemValue>>>
    fun getShopItemCostValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>

    fun getShopItemSellingValues(): Flow<Resource<List<ItemValue>>>
    fun getShopItemSellingValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>

    fun getShopItemProfitValues(): Flow<Resource<List<ItemValue>>>
    fun getShopItemProfitValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>

    fun getShopItemProfitPercentageValues(): Flow<Resource<List<ItemValue>>>
    fun getShopItemProfitPercentageValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>

    fun getMaximumInventoryItem(): Flow<Resource<InventoryItemEntity>>
    fun getMaximumInventoryItem(period: PeriodDropDownItem): Flow<Resource<InventoryItemEntity>>

    fun getMinimumInventoryItem(): Flow<Resource<InventoryItemEntity>>
    fun getMinimumInventoryItem(period: PeriodDropDownItem): Flow<Resource<InventoryItemEntity>>

    suspend fun addInventoryItem(inventoryItem: InventoryItemEntity): Flow<Resource<String>>

    suspend fun addInventoryItems(inventoryItems: InventoryItemEntities)

    suspend fun getInventoryItem(uniqueInventoryItemId: String): InventoryItemEntity?

    suspend fun updateInventoryItem(inventoryItem: InventoryItemEntity): Flow<Resource<String>>

    suspend fun deleteInventoryItem(uniqueInventoryItemId: String): Flow<Resource<String>>

    suspend fun deleteAllInventoryItems()

}
