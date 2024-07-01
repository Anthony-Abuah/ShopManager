package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.savings

import com.example.myshopmanagerapp.core.Constants.ZERO
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import java.util.*

data class SavingsEntityState (
    val savingsEntity: SavingsEntity? = SavingsEntity(0, emptyString, ZERO.toLong(), emptyString, 0.0, emptyString, emptyString, emptyString, emptyString),
    val isLoading: Boolean = false
)