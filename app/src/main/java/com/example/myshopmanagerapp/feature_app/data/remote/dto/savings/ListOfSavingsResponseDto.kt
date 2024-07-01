package com.example.myshopmanagerapp.feature_app.data.remote.dto.savings

data class ListOfSavingsResponseDto(
    val data: List<SavingsInfoDto>?,
    val message: String?,
    val success: Boolean?
)