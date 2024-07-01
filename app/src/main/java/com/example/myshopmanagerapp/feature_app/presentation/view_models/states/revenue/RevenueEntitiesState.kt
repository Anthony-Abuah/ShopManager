package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue

import com.example.myshopmanagerapp.core.RevenueEntities

data class RevenueEntitiesState (
    val revenueEntities: RevenueEntities? = emptyList(),
    val isLoading: Boolean = false
)