package com.example.myshopmanagerapp.feature_app.data.remote.dto.debt

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartDebts(
    val debts: List<DebtInfoDto>,
    val uniqueDebtIds: List<UniqueId>
)