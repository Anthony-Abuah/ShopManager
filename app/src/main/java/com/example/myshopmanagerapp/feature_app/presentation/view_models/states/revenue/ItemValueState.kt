package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue

data class ItemValueState (
    val message: String? = null,
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val itemValue: ItemValue = ItemValue(emptyString, 0.0)
)