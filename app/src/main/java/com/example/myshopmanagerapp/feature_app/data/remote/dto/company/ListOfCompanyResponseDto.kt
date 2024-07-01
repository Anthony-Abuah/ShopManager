package com.example.myshopmanagerapp.feature_app.data.remote.dto.company

data class ListOfCompanyResponseDto(
    val data: List<CompanyInfoDto>?,
    val message: String?,
    val success: Boolean?
)