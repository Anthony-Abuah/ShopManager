package com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.InventoryItem_Table
import com.example.myshopmanagerapp.core.Constants.defaultQuantityCategorizations
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toItemQuantitiesJson
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toPricesJson
import com.example.myshopmanagerapp.core.Functions.toStockEntitiesJson
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.core.Prices
import com.example.myshopmanagerapp.core.QuantityCategorizations
import com.example.myshopmanagerapp.core.StockEntities
import com.example.myshopmanagerapp.core.TypeConverters.toQuantityCategorizationJson
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item.InventoryItemInfoDto


@Entity(tableName = InventoryItem_Table)
data class InventoryItemEntity(
    @PrimaryKey(autoGenerate = true) val inventoryItemId: Int,
    val uniqueInventoryItemId: String,
    val inventoryItemName: String,
    val manufacturerName: String?,
    val inventoryItemPhoto: String?,
    val itemCategory: String = emptyString,
    val quantityCategorizations: QuantityCategorizations = defaultQuantityCategorizations,
    val currentSellingPrice: Double?,
    val sellingPrices: Prices?,
    val currentCostPrice: Double?,
    val costPrices: Prices?,
    val stockInfo: StockEntities?,
    val itemQuantityInfo: ItemQuantities?,
    val totalNumberOfUnits: Int?,
    val otherInfo: String?
){
    fun toInventoryItemInfoDto(uniqueCompanyId: String): InventoryItemInfoDto{
        return InventoryItemInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            uniqueInventoryItemId = uniqueInventoryItemId,
            manufacturerName = manufacturerName.toNotNull(),
            inventoryItemName = inventoryItemName,
            inventoryItemPhoto = inventoryItemPhoto.toNotNull(),
            inventoryItemCategory = itemCategory,
            currentSellingPrice = currentSellingPrice.toNotNull(),
            sellingPrices = sellingPrices.toPricesJson(),
            costPrices = costPrices.toPricesJson(),
            currentCostPrice = currentCostPrice.toNotNull(),
            quantityCategorizations = quantityCategorizations.toQuantityCategorizationJson(),
            stockInfo = stockInfo.toStockEntitiesJson(),
            itemQuantity = itemQuantityInfo.toItemQuantitiesJson(),
            totalNumberOfUnits = totalNumberOfUnits.toNotNull(),
            otherInfo = otherInfo.toNotNull()
        )
    }
}

