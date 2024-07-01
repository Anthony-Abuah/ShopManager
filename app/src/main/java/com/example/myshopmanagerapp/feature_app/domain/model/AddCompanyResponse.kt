package com.example.myshopmanagerapp.feature_app.domain.model

data class AddCompanyResponse(
    val data: String?,
    val message: String,
    val success: Boolean
)

data class AddEntitiesResponse(
    val data: String,
    val message: String,
    val success: Boolean
)