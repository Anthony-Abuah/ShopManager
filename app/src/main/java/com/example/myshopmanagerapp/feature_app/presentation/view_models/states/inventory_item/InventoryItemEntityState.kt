package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity

data class InventoryItemEntityState (
    val inventoryItemEntity: InventoryItemEntity? = InventoryItemEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyList(), null, emptyList(),       .0, null, null, null, null, emptyString),
    val isLoading: Boolean = false
)