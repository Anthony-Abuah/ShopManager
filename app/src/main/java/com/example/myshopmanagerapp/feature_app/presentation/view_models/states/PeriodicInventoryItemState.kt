package com.example.myshopmanagerapp.feature_app.presentation.view_models.states

import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity

data class PeriodicInventoryItemState (
    val data: Map<InventoryItemEntity?, Int> = emptyMap(),
    val message: String? = null,
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false
)




