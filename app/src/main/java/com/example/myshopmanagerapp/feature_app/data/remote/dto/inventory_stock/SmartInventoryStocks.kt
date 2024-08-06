package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartInventoryStocks(
    val inventoryStocks: List<InventoryStockInfoDto>,
    val uniqueInventoryStockIds: List<UniqueId>
)