package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.debt

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import java.util.*

data class DebtEntityState (
    val debtEntity: DebtEntity? = DebtEntity(0, emptyString, 0, emptyString, emptyString, 0.0, emptyString, emptyString),
    val isLoading: Boolean = false
)