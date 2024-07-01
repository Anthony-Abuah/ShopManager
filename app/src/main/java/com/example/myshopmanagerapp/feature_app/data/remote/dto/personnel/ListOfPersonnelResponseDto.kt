package com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel

data class ListOfPersonnelResponseDto(
    val data: List<PersonnelInfoDto>?,
    val message: String?,
    val success: Boolean?
)