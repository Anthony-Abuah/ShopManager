package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.supplier

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.SupplierEntities

data class SupplierEntitiesState (
    val supplierEntities: SupplierEntities? = emptyList(),
    val isSuccessful: Boolean = false,
    val message: String? = emptyString,
    val isLoading: Boolean = false,
)