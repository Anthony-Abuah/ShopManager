package com.example.myshopmanagerapp.feature_app.data.remote.dto.customer

data class ListOfCustomerResponseDto(
    val data: List<CustomerInfoDto>?,
    val message: String?,
    val success: Boolean?
)