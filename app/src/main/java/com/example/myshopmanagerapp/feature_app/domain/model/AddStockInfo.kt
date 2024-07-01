package com.example.myshopmanagerapp.feature_app.domain.model

import java.util.*

data class AddStockInfo(
   val uniqueItemId: String,
   val date: Date,
   val quantityCategorization: ItemQuantityCategorization,
   val anyOtherInfo: String
)
