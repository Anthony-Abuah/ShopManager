package com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment

data class ListOfDebtRepaymentResponseDto(
    val data: List<DebtRepaymentInfoDto>?,
    val message: String?,
    val success: Boolean?
)