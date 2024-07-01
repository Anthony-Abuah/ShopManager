package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item

import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue

data class ItemValuesState (
    val message: String? = null,
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val itemValues: List<ItemValue> = emptyList()
)




