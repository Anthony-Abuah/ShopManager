package com.example.myshopmanagerapp.feature_app.domain.model

data class ItemQuantityInfo(
    val itemName: String,
    val quantity: Double,
    val unitPrice: Double,
    val amount: Double = quantity.times(unitPrice)
)
