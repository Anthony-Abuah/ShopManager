package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.stock

import com.example.myshopmanagerapp.core.StockEntities

data class StockEntitiesState (
    val stockEntities: StockEntities? = emptyList(),
    val isLoading: Boolean = false
)