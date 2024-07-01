package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.stock

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import java.time.LocalDate

data class StockEntityState (
    val stockEntity: StockEntity? = StockEntity(0, emptyString, LocalDate.now().toTimestamp(), emptyString, emptyString, emptyString, emptyList(),0, LocalDate.now().toTimestamp(), 0,false, null),
    val isLoading: Boolean = false
)




