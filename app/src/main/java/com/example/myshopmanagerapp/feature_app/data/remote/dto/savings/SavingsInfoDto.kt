package com.example.myshopmanagerapp.feature_app.data.remote.dto.savings

import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity

data class SavingsInfoDto(
    val uniqueSavingsId: String,
    val uniqueCompanyId: String,
    val uniqueBankAccountId: String,
    val uniquePersonnelId: String,
    val savingsDate: Long,
    val savingsDayOfWeek: String,
    val savingsAmount: Double,
    val bankPersonnel: String,
    val otherInfo: String
){
    fun toSavingsEntity(): SavingsEntity{
        return SavingsEntity(
            0,
            uniquePersonnelId = uniquePersonnelId,
            uniqueBankAccountId = uniqueBankAccountId,
            uniqueSavingsId = uniqueSavingsId,
            date = savingsDate,
            dayOfWeek = savingsDayOfWeek.toNotNull(),
            savingsAmount = savingsAmount,
            bankPersonnel = bankPersonnel,
            otherInfo = otherInfo
        )
    }
}
