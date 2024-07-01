package com.example.myshopmanagerapp.feature_app.data.remote.dto.expense

data class ListOfExpenseResponseDto(
    val data: List<ExpenseInfoDto>?,
    val message: String?,
    val success: Boolean?
)