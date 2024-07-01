package com.example.myshopmanagerapp.feature_app.domain.model

data class ReceiptInfo(
   val uniqueReceiptId: String,
   val shopName: String,
   val shopLocation: String,
   val shopContact: String,
   val date: Long,
   val customerName: String,
   val customerContact: String?,
   val personnelName: String?,
   val personnelRole: String?,
   val items: List<ItemQuantityInfo>,
   val totalAmount: Double = items.sumOf { it.amount }
)
