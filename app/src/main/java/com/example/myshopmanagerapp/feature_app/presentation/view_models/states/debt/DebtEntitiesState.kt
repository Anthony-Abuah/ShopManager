package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.debt

import com.example.myshopmanagerapp.core.DebtEntities

data class DebtEntitiesState (
    val debtEntities: DebtEntities? = emptyList(),
    val isLoading: Boolean = false
)