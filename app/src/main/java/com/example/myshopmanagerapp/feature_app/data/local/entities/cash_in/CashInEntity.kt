package com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.CashIn_Table
import com.example.myshopmanagerapp.core.Constants.Loan
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in.CashInInfoDto

@Entity(tableName = CashIn_Table)
data class CashInEntity(
    @PrimaryKey(autoGenerate = true) val cashInId: Int,
    val uniqueCashInId: String,
    val date: Long,
    val dayOfWeek: String?,
    val cashSource: String?,
    val isLoan: Boolean,
    val cashInAmount: Double,
    val period: Int?,
    val interestAmount: Double?,
    val paymentAmount: Double?,
    val cashInType: String = if (isLoan) Loan else NotAvailable,
    val otherInfo: String?
){
    fun toCashInfoDto(uniqueCompanyId: String): CashInInfoDto{
        return CashInInfoDto(
            uniqueCashInId,
            uniqueCompanyId,
            date = date,
            dayOfWeek = dayOfWeek,
            cashSource = cashSource,
            isLoan = isLoan,
            cashInAmount = cashInAmount,
            period = period,
            interestAmount = interestAmount,
            paymentAmount = paymentAmount,
            cashInType = cashInType,
            otherInfo = otherInfo
        )
    }
}

