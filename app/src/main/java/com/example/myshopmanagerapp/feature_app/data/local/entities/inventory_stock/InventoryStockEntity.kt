package com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.InventoryStock_Table
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock.InventoryStockInfoDto

@Entity(tableName = InventoryStock_Table)
data class InventoryStockEntity(
    @PrimaryKey(autoGenerate = true) val inventoryStockId: Int,
    val uniqueInventoryStockId: String,
    val uniqueInventoryId: String,
    val uniqueStockId: String,
){
    fun toInventoryStockInfoDto(uniqueCompanyId: String): InventoryStockInfoDto{
        return InventoryStockInfoDto(
            uniqueInventoryStockId, uniqueInventoryId, uniqueCompanyId, uniqueStockId
        )
    }
}

