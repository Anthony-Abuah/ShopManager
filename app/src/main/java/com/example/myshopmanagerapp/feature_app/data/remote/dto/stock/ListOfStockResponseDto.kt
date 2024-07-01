package com.example.myshopmanagerapp.feature_app.data.remote.dto.stock

data class ListOfStockResponseDto(
    val data: List<StockInfoDto>?,
    val message: String?,
    val success: Boolean?
)