package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartInventories(
    val inventories: List<InventoryInfoDto>,
    val uniqueInventoryIds: List<UniqueId>
)