package com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt

data class ListOfReceiptResponseDto(
    val data: List<ReceiptInfoDto>?,
    val message: String?,
    val success: Boolean?
)