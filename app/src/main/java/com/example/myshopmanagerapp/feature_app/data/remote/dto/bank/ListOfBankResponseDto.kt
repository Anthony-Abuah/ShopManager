package com.example.myshopmanagerapp.feature_app.data.remote.dto.bank

data class ListOfBankResponseDto(
    val data: List<BankAccountInfoDto>?,
    val message: String?,
    val success: Boolean?
)