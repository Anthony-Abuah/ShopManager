package com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.DebtRepayment_Table
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment.DebtRepaymentInfoDto
import java.util.*

@Entity(tableName = DebtRepayment_Table)
data class DebtRepaymentEntity(
    @PrimaryKey(autoGenerate = true) val debtRepaymentId: Int,
    val uniqueDebtRepaymentId: String,
    val date: Long,
    val dayOfWeek: String?,
    val uniqueCustomerId: String,
    val debtRepaymentAmount: Double,
    val uniquePersonnelId: String,
    val otherInfo: String?
){
    fun toDebtRepaymentInfoDto(uniqueCompanyId: String): DebtRepaymentInfoDto{
        return DebtRepaymentInfoDto(
            uniqueDebtRepaymentId,
            uniqueCompanyId,
            uniqueCustomerId,
            uniquePersonnelId,
            date,
            dayOfWeek.toNotNull(),
            debtRepaymentAmount,
            otherInfo.toNotNull()
        )
    }
}

