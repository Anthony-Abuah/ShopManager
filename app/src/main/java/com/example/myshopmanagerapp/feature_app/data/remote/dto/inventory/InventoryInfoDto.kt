package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory

import com.example.myshopmanagerapp.core.Functions.toItemQuantities
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import java.util.*

data class InventoryInfoDto(
    val uniqueInventoryId: String,
    val uniqueCompanyId: String,
    val uniqueInventoryItemId: String,
    val date: Long,
    val dayOfWeek: String?,
    val quantityInfo: String?,
    val totalNumberOfUnits: Int?,
    val totalCostPrice: Double?,
    val prices: String?,
    val unitCostPrice: Double?,
    val receiptId: String?,
    val otherInfo: String?,
){
    fun toInventoryEntity(): InventoryEntity{
        return InventoryEntity(
            0,
            uniqueInventoryId = uniqueInventoryId,
            uniqueInventoryItemId = uniqueInventoryItemId,
            date = Date().time,
            dayOfWeek = dayOfWeek.toNotNull(),
            quantityInfo = quantityInfo.toItemQuantities(),
            totalCostPrice = totalCostPrice.toNotNull(),
            unitCostPrice = unitCostPrice.toNotNull(),
            totalNumberOfUnits = totalNumberOfUnits.toNotNull(),
            receiptId = receiptId,
            otherInfo = otherInfo
        )
    }
}
