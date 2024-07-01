package com.example.myshopmanagerapp.feature_app.domain.model

import java.util.*

data class UpdateStockInfo(
   val stockId: Int,
   val uniqueStockId: String,
   val uniqueItemId: String,
   val date: Date,
   val dayOfWeek: String,
   val numberOfRemainingStock: ItemQuantityCategorization,
   val anyOtherInfo: String,
)
