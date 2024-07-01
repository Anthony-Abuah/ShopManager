package com.example.myshopmanagerapp.feature_app.domain.model

data class CompanyResponse(
    val data: List<Data>,
    val message: String,
    val success: Boolean
)