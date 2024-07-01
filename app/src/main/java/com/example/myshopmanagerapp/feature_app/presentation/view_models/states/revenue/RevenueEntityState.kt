package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import java.time.LocalDate

private val date = LocalDate.now().toDate().time

data class RevenueEntityState (
    val revenueEntity: RevenueEntity? = RevenueEntity(0, emptyString, date, emptyString, 0, emptyString, 0.0, emptyString, emptyString),
    val isLoading: Boolean = false
)