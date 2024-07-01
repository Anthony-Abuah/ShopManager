package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.customer

import com.example.myshopmanagerapp.core.CustomerEntities

data class CustomerEntitiesState (
    val customerEntities: CustomerEntities? = emptyList(),
    val isLoading: Boolean = false
)