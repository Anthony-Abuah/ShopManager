package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.savings

import com.example.myshopmanagerapp.core.SavingsEntities

data class SavingsEntitiesState (
    val savingsEntities: SavingsEntities? = emptyList(),
    val isLoading: Boolean = false
)