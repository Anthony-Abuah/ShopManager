package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryDisplayCardValue
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityCategorization
import java.time.LocalDate

data class InventoryEntityInfoState (
    val dateString: String = LocalDate.now().toString(),
    val dayOfWeek: String = LocalDate.now().dayOfWeek.toString(),
    val otherInfo: String = emptyString,
    val uniqueInventoryItemId: String = emptyString,
    val costPrice: String = emptyString,
    val receiptId: String = emptyString,
    val inventoryDisplayValues: List<InventoryDisplayCardValue>? = null,
    val itemQuantityCategorization: ItemQuantityCategorization? = null,
)