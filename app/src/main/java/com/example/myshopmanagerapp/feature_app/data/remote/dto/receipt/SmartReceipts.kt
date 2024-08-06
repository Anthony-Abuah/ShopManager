package com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartReceipts(
    val receipts: List<ReceiptInfoDto>,
    val uniqueReceiptIds: List<UniqueId>
)