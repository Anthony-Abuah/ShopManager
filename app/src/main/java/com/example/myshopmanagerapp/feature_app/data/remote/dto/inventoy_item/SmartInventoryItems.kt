package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartInventoryItems(
    val inventoryItems: List<InventoryItemInfoDto>,
    val uniqueInventoryItemIds: List<UniqueId>
)