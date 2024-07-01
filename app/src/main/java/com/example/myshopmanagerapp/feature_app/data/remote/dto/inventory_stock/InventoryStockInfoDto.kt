package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock

import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockEntity

data class InventoryStockInfoDto(
    val uniqueInventoryStockId: String,
    val uniqueInventoryId: String,
    val uniqueCompanyId: String,
    val uniqueStockId: String,
){
    fun toInventoryStock(): InventoryStockEntity{
        return InventoryStockEntity(
            0,
            uniqueInventoryStockId = uniqueInventoryStockId,
            uniqueInventoryId = uniqueInventoryId,
            uniqueStockId = uniqueStockId
        )
    }
}
