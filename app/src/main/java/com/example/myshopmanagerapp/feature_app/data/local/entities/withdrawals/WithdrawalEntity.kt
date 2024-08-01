package com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Withdrawal_Table
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal.WithdrawalInfoDto

@Entity(tableName = Withdrawal_Table)
data class WithdrawalEntity(
    @PrimaryKey(autoGenerate = true) val withdrawalId: Int,
    val uniqueWithdrawalId: String,
    val date: Long,
    val dayOfWeek: String,
    val transactionId: String?,
    val withdrawalAmount: Double,
    val uniquePersonnelId: String,
    val uniqueBankAccountId: String,
    val otherInfo: String?
){
    fun toWithdrawalInfoDto(uniqueCompanyId: String): WithdrawalInfoDto{
        return WithdrawalInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            uniqueBankAccountId = uniqueBankAccountId,
            uniquePersonnelId = uniquePersonnelId,
            uniqueWithdrawalId = uniqueWithdrawalId,
            date = date,
            dayOfWeek = dayOfWeek,
            transactionId = transactionId.toNotNull(),
            withdrawalAmount = withdrawalAmount,
            otherInfo = otherInfo.toNotNull()
        )
    }
}

