package com.example.myshopmanagerapp.feature_app.domain.model

import com.example.myshopmanagerapp.core.Constants.Unit

data class Price(
    val date: Long,
    val sizeName: String = Unit,
    val price: Double
)
