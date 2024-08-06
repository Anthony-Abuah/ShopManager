package com.example.myshopmanagerapp.feature_app.data.remote.dto.savings

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartSavings(
    val savings: List<SavingsInfoDto>,
    val uniqueSavingsIds: List<UniqueId>
)