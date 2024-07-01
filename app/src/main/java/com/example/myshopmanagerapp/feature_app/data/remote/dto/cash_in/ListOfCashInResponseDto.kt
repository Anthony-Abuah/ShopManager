package com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in

data class ListOfCashInResponseDto(
    val data: List<CashInInfoDto>?,
    val message: String?,
    val success: Boolean?
)