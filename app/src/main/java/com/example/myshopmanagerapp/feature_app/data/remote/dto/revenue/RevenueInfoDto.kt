package com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue

import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity

data class RevenueInfoDto(
    val uniqueRevenueId: String,
    val uniqueCompanyId: String,
    val uniquePersonnelId: String,
    val revenueDate: Long,
    val revenueDayOfWeek: String?,
    val revenueType: String?,
    val numberOfHours: Int?,
    val revenueAmount: Double,
    val otherInfo: String?
){
    fun toRevenueEntity(): RevenueEntity{
        return RevenueEntity(
            0,
            uniquePersonnelId = uniquePersonnelId,
            uniqueRevenueId = uniqueRevenueId,
            date = revenueDate,
            dayOfWeek = revenueDayOfWeek,
            revenueType = revenueType,
            numberOfHours = numberOfHours,
            revenueAmount = revenueAmount,
            otherInfo = otherInfo
        )
    }
}
