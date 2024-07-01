package com.example.myshopmanagerapp.feature_app.data.local.entities.debt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Debt_Table
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt.DebtInfoDto

@Entity(tableName = Debt_Table)
data class DebtEntity(
    @PrimaryKey(autoGenerate = true) val debtId: Int,
    val uniqueDebtId: String,
    val date: Long,
    val dayOfWeek: String?,
    val uniqueCustomerId: String,
    val debtAmount: Double,
    val uniquePersonnelId: String,
    val otherInfo: String?
){
    fun toDebtInfoDto(uniqueCompanyId: String): DebtInfoDto{
        return DebtInfoDto(
            uniqueDebtId = uniqueDebtId,
            uniqueCompanyId = uniqueCompanyId,
            uniqueCustomerId = uniqueCustomerId,
            uniquePersonnelId = uniquePersonnelId,
            date = date,
            dayOfWeek = dayOfWeek,
            debtAmount = debtAmount,
            otherInfo = otherInfo
        )
    }
}

