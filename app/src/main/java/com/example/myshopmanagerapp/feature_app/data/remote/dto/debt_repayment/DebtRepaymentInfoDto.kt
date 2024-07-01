package com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment

import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity


data class DebtRepaymentInfoDto(
    val uniqueDebtRepaymentId: String,
    val uniqueCompanyId: String,
    val uniqueCustomerId: String,
    val uniquePersonnelId: String,
    val date: Long,
    val dayOfWeek: String?,
    val debtRepaymentAmount: Double,
    val otherInfo: String?
){
    fun toDebtRepaymentEntity(): DebtRepaymentEntity{
        return DebtRepaymentEntity(
            0,
            uniqueCustomerId = uniqueCustomerId,
            uniquePersonnelId = uniquePersonnelId,
            uniqueDebtRepaymentId = uniqueDebtRepaymentId,
            date = date,
            dayOfWeek = dayOfWeek,
            debtRepaymentAmount = debtRepaymentAmount,
            otherInfo = otherInfo
        )
    }
}
