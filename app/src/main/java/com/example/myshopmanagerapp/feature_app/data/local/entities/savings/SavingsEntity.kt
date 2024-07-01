package com.example.myshopmanagerapp.feature_app.data.local.entities.savings

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Savings_Table
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.feature_app.data.remote.dto.savings.SavingsInfoDto

@Entity(tableName = Savings_Table)
data class SavingsEntity(
    @PrimaryKey(autoGenerate = true) val savingsId: Int,
    val uniqueSavingsId: String,
    val date: Long,
    val dayOfWeek: String,
    val savingsAmount: Double,
    val uniquePersonnelId: String,
    val bankPersonnel: String?,
    val uniqueBankAccountId: String,
    val otherInfo: String?
){
    fun toSavingsInfoDto(uniqueCompanyId: String): SavingsInfoDto{
        return SavingsInfoDto(
            uniquePersonnelId = uniquePersonnelId,
            uniqueCompanyId = uniqueCompanyId,
            uniqueSavingsId = uniqueSavingsId,
            uniqueBankAccountId = uniqueBankAccountId,
            savingsDate = date,
            savingsDayOfWeek = dayOfWeek,
            savingsAmount = savingsAmount,
            bankPersonnel = bankPersonnel,
            otherInfo = otherInfo
        )
    }

}

