package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.withdrawal

import com.example.myshopmanagerapp.core.WithdrawalEntities

data class WithdrawalEntitiesState (
    val withdrawalEntities: WithdrawalEntities? = emptyList(),
    val isLoading: Boolean = false
)