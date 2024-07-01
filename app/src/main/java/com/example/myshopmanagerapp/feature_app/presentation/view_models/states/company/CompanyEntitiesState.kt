package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company

import com.example.myshopmanagerapp.core.CompanyEntities
import com.example.myshopmanagerapp.core.ReceiptEntities

data class CompanyEntitiesState (
    val companyEntities: CompanyEntities? = emptyList(),
    val isLoading: Boolean = false
)

data class ReceiptEntitiesState (
    val receiptEntities: ReceiptEntities? = emptyList(),
    val isLoading: Boolean = false
)