package com.example.myshopmanagerapp.feature_app.domain.model

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity


data class InventoryQuantityDisplayValues(
    val uniqueInventoryId: String = emptyString,
    val inventoryItemEntity: InventoryItemEntity,
    val totalUnits: Int,
    val unitCost: Double,
    val totalCost: Double = unitCost.times(totalUnits),
)
