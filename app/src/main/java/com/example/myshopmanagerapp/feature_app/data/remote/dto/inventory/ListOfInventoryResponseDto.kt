package com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory

data class ListOfInventoryResponseDto(
    val data: List<InventoryInfoDto>?,
    val message: String?,
    val success: Boolean?
)