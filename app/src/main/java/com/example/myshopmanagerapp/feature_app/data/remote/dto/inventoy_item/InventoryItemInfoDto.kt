package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item

import com.example.myshopmanagerapp.core.Functions.toItemQuantities
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toPrices
import com.example.myshopmanagerapp.core.Functions.toStockEntities
import com.example.myshopmanagerapp.core.TypeConverters.toQuantityCategorizations
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity

data class InventoryItemInfoDto(
    val uniqueInventoryItemId: String,
    val uniqueCompanyId: String,
    val inventoryItemName: String,
    val manufacturerName: String,
    val inventoryItemPhoto: String,
    val inventoryItemCategory: String,
    val quantityCategorizations: String,
    val currentSellingPrice: Double,
    val sellingPrices: String,
    val currentCostPrice: Double,
    val costPrices: String,
    val stockInfo: String,
    val itemQuantity: String,
    val totalNumberOfUnits: Int,
    val otherInfo: String,
    ){
    fun toInventoryItemEntity(): InventoryItemEntity{
        return InventoryItemEntity(
            0,
            uniqueInventoryItemId = uniqueInventoryItemId,
            inventoryItemName = inventoryItemName,
            manufacturerName = manufacturerName,
            inventoryItemPhoto = inventoryItemPhoto,
            itemCategory = inventoryItemCategory.toNotNull(),
            quantityCategorizations = quantityCategorizations.toQuantityCategorizations(),
            currentSellingPrice = currentSellingPrice,
            sellingPrices = sellingPrices.toPrices(),
            currentCostPrice = currentCostPrice,
            costPrices = costPrices.toPrices(),
            stockInfo = stockInfo.toStockEntities(),
            itemQuantityInfo = itemQuantity.toItemQuantities(),
            totalNumberOfUnits = totalNumberOfUnits,
            otherInfo = otherInfo
        )
    }
}
