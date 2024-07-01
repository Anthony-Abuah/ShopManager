package com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in

import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInEntity

data class CashInInfoDto(
    val uniqueCashInId: String,
    val uniqueCompanyId: String,
    val date: Long,
    val dayOfWeek: String?,
    val cashSource: String?,
    val isLoan: Boolean,
    val cashInAmount: Double,
    val period: Int?,
    val interestAmount: Double?,
    val paymentAmount: Double?,
    val cashInType: String,
    val otherInfo: String?
){
    fun toCashInEntity(): CashInEntity {
        return CashInEntity(
            0,
            uniqueCashInId,
            date,
            dayOfWeek,
            cashSource,
            isLoan,
            cashInAmount,
            period,
            interestAmount,
            paymentAmount,
            cashInType,
            otherInfo
        )
    }
}
