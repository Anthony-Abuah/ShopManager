package com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal

import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity


data class WithdrawalInfoDto(
    val uniqueWithdrawalId: String,
    val uniqueCompanyId: String,
    val uniqueBankAccountId: String,
    val uniquePersonnelId: String,
    val date: Long,
    val dayOfWeek: String?,
    val withdrawalAmount: Double,
    val transactionId: String,
    val otherInfo: String?
){
    fun toWithdrawalEntity(): WithdrawalEntity{
        return WithdrawalEntity(
            0,
            uniqueWithdrawalId = uniqueWithdrawalId,
            uniqueBankAccountId = uniqueBankAccountId,
            uniquePersonnelId = uniquePersonnelId,
            date = date,
            dayOfWeek = dayOfWeek.toNotNull(),
            withdrawalAmount = withdrawalAmount,
            transactionId = transactionId,
            otherInfo = otherInfo
        )
    }
}
