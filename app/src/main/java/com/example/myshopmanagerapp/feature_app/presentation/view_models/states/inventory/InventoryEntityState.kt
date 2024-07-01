package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import java.time.LocalDate

data class InventoryEntityState (
    val inventoryEntity: InventoryEntity? = InventoryEntity(0, emptyString, emptyString, emptyString, LocalDate.now().toTimestamp(), emptyString,
        emptyList(), 0, 0.0, 0.0, emptyString, emptyString),
    val isLoading: Boolean = false
)