package com.example.myshopmanagerapp.feature_app.data.remote.dto.debt

data class ListOfDebtResponseDto(
    val data: List<DebtInfoDto>?,
    val message: String?,
    val success: Boolean?
)