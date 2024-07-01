package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item

import com.example.myshopmanagerapp.core.InventoryItemEntities

data class InventoryItemEntitiesState (
    val inventoryItemEntities: InventoryItemEntities? = emptyList(),
    val isLoading: Boolean = false
)