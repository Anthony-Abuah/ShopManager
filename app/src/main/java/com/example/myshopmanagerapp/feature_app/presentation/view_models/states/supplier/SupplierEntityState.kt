package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.supplier

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity

data class SupplierEntityState (
    val supplierEntity: SupplierEntity? = SupplierEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString),
    val isLoading: Boolean = false
)