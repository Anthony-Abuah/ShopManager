package com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier

data class ListOfSupplierResponseDto(
    val data: List<SupplierInfoDto>?,
    val message: String?,
    val success: Boolean?
)