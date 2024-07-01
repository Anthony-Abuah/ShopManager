package com.example.myshopmanagerapp.feature_app.domain.model

import java.util.Date

data class InventoryDisplayCardValue(
    val uniqueInventoryId: String? = null,
    val inventoryItemName: String,
    val quantityCategorization: ItemQuantityCategorization?,
    val totalCost: Double?,
    val totalUnits: Int = quantityCategorization?.totalNumberOfUnits ?: 0,
    val unitCost: Double? = quantityCategorization?.totalNumberOfUnits?.let { totalCost?.div(it) }
){
    /*fun toPrice(date: Long): Price{
        return Price(
            date = date,

        )
    }*/
}
