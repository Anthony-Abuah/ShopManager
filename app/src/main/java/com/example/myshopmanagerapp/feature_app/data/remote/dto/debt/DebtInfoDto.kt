package com.example.myshopmanagerapp.feature_app.data.remote.dto.debt

import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity

data class DebtInfoDto(
    val uniqueDebtId: String,
    val uniqueCompanyId: String,
    val uniqueCustomerId: String,
    val uniquePersonnelId: String,
    val date: Long,
    val dayOfWeek: String?,
    val debtAmount: Double,
    val otherInfo: String?
){
    fun toDebtEntity(): DebtEntity{
        return DebtEntity(
            0,
            uniqueDebtId = uniqueDebtId,
            date = date,
            dayOfWeek = dayOfWeek,
            debtAmount = debtAmount,
            uniquePersonnelId = uniquePersonnelId,
            uniqueCustomerId = uniqueCustomerId,
            otherInfo = otherInfo
        )
    }
}
