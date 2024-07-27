package com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt

import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.RevenueInfoDto

data class ListOfReceiptResponseDto(
    val data: List<RevenueInfoDto>?,
    val message: String?,
    val success: Boolean?
)