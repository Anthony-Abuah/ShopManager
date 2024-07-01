package com.example.myshopmanagerapp.feature_app.domain.model

import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.core.Prices
import com.example.myshopmanagerapp.core.StockEntities

data class UpdateInventoryItem(
    val uniqueInventoryItemId: String,
    val stockInfo: StockEntities?,
    val numberOfUnitsRemaining: Int?,
    val itemQuantityInfo: ItemQuantities?,
    val costPrices: Prices?,
    val currentUnitCostPrice: Double?
)
