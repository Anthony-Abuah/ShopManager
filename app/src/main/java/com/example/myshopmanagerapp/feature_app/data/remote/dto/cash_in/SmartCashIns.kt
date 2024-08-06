package com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartCashIns(
    val cashIns: List<CashInInfoDto>,
    val uniqueCashInIds: List<UniqueId>
)