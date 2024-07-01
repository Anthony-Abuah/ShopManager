package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.customer

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity

data class CustomerEntityState (
    val customerEntity: CustomerEntity? = CustomerEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, 500.0),
    val isLoading: Boolean = false
)