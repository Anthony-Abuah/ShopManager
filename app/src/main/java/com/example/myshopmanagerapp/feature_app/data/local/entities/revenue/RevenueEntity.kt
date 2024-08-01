package com.example.myshopmanagerapp.feature_app.data.local.entities.revenue

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Revenue_Table
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.RevenueInfoDto
import java.util.*

@Entity(tableName = Revenue_Table)
data class RevenueEntity(
    @PrimaryKey(autoGenerate = true) val revenueId: Int,
    val uniqueRevenueId: String,
    val date: Long,
    val dayOfWeek: String?,
    val numberOfHours: Int?,
    val revenueType: String? = "Sales",
    val revenueAmount: Double,
    val uniquePersonnelId: String,
    val otherInfo: String?
){
    fun toRevenueInfoDto(uniqueCompanyId: String): RevenueInfoDto{
        return RevenueInfoDto(
            uniqueRevenueId = uniqueRevenueId.toNotNull(),
            uniqueCompanyId = uniqueCompanyId.toNotNull(),
            uniquePersonnelId = uniquePersonnelId.toNotNull(),
            date = date.toNotNull(),
            dayOfWeek = dayOfWeek.toNotNull(),
            revenueType = revenueType.toNotNull(),
            numberOfHours = numberOfHours.toNotNull(),
            revenueAmount = revenueAmount.toNotNull(),
            otherInfo = otherInfo.toNotNull()
        )
    }
}

