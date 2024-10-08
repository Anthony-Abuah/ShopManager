package com.example.myshopmanagerapp.feature_app.data.local.entities.stock

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Stock_Table
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toItemQuantitiesJson
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
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
    val unitCostPrice: Double? = null,
    val totalCostPrice: Double? = null,
    val isInventoryStock: Boolean = false,
    val otherInfo: String?
){
    fun toStockInfoDto(uniqueCompanyId: String): StockInfoDto{
        return StockInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            uniqueItemId = uniqueInventoryItemId,
            uniqueStockId = uniqueStockId,
            stockDate = date.toLocalDate().toTimestamp(),
            stockDayOfWeek = dayOfWeek.toNotNull(),
            stockQuantityInfo = stockQuantityInfo.toItemQuantitiesJson(),
            totalNumberOfUnits = totalNumberOfUnits,
            lastStockDate = dateOfLastStock?.toLocalDate().toTimestamp(),
            changeInNumberOfUnits = changeInNumberOfUnits.toNotNull(),
            isInventoryStock = isInventoryStock,
            otherInfo = otherInfo.toNotNull(),
            uniquePersonnelId = uniquePersonnelId,
            unitCostPrice = unitCostPrice.toNotNull(),
            totalCostPrice = totalCostPrice.toNotNull()
        )
    }
}

