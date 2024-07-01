package com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal

data class ListOfWithdrawalResponseDto(
    val data: List<WithdrawalInfoDto>?,
    val message: String?,
    val success: Boolean?
)