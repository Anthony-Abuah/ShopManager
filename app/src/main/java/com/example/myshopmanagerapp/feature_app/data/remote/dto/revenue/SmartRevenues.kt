package com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartRevenues(
    val revenues: List<RevenueInfoDto>,
    val uniqueRevenueIds: List<UniqueId>
)