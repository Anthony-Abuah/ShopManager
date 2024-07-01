package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock

data class ListOfInventoryStockResponseDto(
    val data: List<InventoryStockInfoDto>?,
    val message: String?,
    val success: Boolean?
)