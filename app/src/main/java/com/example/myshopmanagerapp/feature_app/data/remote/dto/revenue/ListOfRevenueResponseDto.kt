package com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue

data class ListOfRevenueResponseDto(
    val data: List<RevenueInfoDto>?,
    val message: String?,
    val success: Boolean?
)