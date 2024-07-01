package com.example.myshopmanagerapp.feature_app.data.local.entities.stock

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Stock_Table
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toItemQuantitiesJson
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.remote.dto.stock.StockInfoDto

@Entity(tableName = Stock_Table)
data class StockEntity(
    @PrimaryKey(autoGenerate = true) val stockId: Int,
    val uniqueStockId: String,
    val date: Long,
    val dayOfWeek: String?,
    val uniqueInventoryItemId: String,
    val uniquePersonnelId: String = emptyString,
    val stockQuantityInfo: ItemQuantities,
    val totalNumberOfUnits: Int,
    val dateOfLastStock: Long?,
    val changeInNumberOfUnits: Int?,
    val isInventoryStock: Boolean,
    val otherInfo: String?
){
    fun toStockInfoDto(uniqueCompanyId: String): StockInfoDto{
        return StockInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            uniqueItemId = uniqueInventoryItemId,
            uniqueStockId = uniqueStockId,
            stockDate = date.toLocalDate().toTimestamp(),
            stockDayOfWeek = dayOfWeek,
            stockQuantityInfo = stockQuantityInfo.toItemQuantitiesJson(),
            totalNumberOfUnits = totalNumberOfUnits,
            lastStockDate = dateOfLastStock?.toLocalDate().toTimestamp(),
            changeInNumberOfUnits = changeInNumberOfUnits,
            isInventoryStock = isInventoryStock,
            otherInfo = otherInfo
        )
    }
}

