package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item

data class ListOfInventoryItemResponseDto(
    val data: List<InventoryItemInfoDto>?,
    val message: String?,
    val success: Boolean?
)