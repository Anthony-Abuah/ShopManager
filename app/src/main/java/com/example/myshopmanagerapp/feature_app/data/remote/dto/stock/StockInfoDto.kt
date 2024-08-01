package com.example.myshopmanagerapp.feature_app.data.remote.dto.stock

import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Functions.toItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity

data class StockInfoDto(
    val uniqueStockId: String,
    val uniqueCompanyId: String,
    val uniqueItemId: String,
    val uniquePersonnelId: String,
    val stockDate: Long,
    val stockDayOfWeek: String,
    val stockQuantityInfo: String,
    val totalNumberOfUnits: Int,
    val lastStockDate: Long,
    val changeInNumberOfUnits: Int,
    val isInventoryStock: Boolean,
    val unitCostPrice: Double = 0.0,
    val totalCostPrice: Double = 0.0,
    val otherInfo: String,
){
    fun toStockEntity(): StockEntity{
        return StockEntity(
            0,
            uniqueStockId = uniqueStockId,
            uniqueInventoryItemId = uniqueItemId,
            date = stockDate,
            dayOfWeek = stockDayOfWeek,
            stockQuantityInfo = stockQuantityInfo.toItemQuantities(),
            totalNumberOfUnits = totalNumberOfUnits,
            changeInNumberOfUnits = changeInNumberOfUnits,
            dateOfLastStock = lastStockDate,
            isInventoryStock = isInventoryStock,
            otherInfo = otherInfo,
            unitCostPrice = unitCostPrice,
            totalCostPrice = totalCostPrice,
            uniquePersonnelId = uniquePersonnelId
        )
    }
}
