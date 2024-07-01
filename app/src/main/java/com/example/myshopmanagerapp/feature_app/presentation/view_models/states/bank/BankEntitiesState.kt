package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.bank

import com.example.myshopmanagerapp.core.BankAccountEntities

data class BankEntitiesState (
    val bankAccountEntities: BankAccountEntities? = emptyList(),
    val isLoading: Boolean = false
)