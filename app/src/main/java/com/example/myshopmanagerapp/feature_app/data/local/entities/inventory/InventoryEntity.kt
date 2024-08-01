package com.example.myshopmanagerapp.feature_app.data.local.entities.inventory

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Inventory_Table
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toItemQuantitiesJson
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory.InventoryInfoDto
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues

@Entity(tableName = Inventory_Table)
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true) val inventoryId: Int,
    val uniqueInventoryId: String,
    val uniqueInventoryItemId: String,
    val uniquePersonnelId: String = emptyString,
    val date: Long,
    val dayOfWeek: String,
    val quantityInfo: ItemQuantities,
    val totalNumberOfUnits: Int = quantityInfo.getTotalNumberOfUnits(),
    val unitCostPrice: Double,
    val totalCostPrice: Double,
    val receiptId: String?,
    val otherInfo: String?
){
    fun toInventoryQuantityDisplayValues(inventoryItem: InventoryItemEntity): InventoryQuantityDisplayValues{
        return InventoryQuantityDisplayValues(
            uniqueInventoryId = uniqueInventoryId,
            inventoryItemEntity = inventoryItem,
            totalUnits = totalNumberOfUnits,
            unitCost = unitCostPrice,
            totalCost = totalCostPrice
        )
    }

    fun toInventoryInfoDto(uniqueCompanyId: String): InventoryInfoDto{
        return InventoryInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            uniqueInventoryItemId = uniqueInventoryItemId,
            uniqueInventoryId = uniqueInventoryId,
            unitCostPrice = unitCostPrice,
            date = date.toLocalDate().toTimestamp(),
            dayOfWeek = dayOfWeek,
            quantityInfo = quantityInfo.toItemQuantitiesJson(),
            totalNumberOfUnits = totalNumberOfUnits,
            totalCostPrice = totalCostPrice,
            prices = emptyString,
            receiptId = receiptId.toNotNull(),
            otherInfo = otherInfo.toNotNull()
        )
    }

}





