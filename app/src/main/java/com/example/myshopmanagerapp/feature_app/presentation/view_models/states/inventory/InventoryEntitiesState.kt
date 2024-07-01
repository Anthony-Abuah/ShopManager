package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory

import com.example.myshopmanagerapp.core.InventoryEntities

data class InventoryEntitiesState (
    val inventoryEntities: InventoryEntities? = emptyList(),
    val isLoading: Boolean = false
)