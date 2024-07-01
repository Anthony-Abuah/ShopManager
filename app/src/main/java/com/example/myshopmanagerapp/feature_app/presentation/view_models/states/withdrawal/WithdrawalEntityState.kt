package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.withdrawal

import com.example.myshopmanagerapp.core.Constants.ZERO
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import java.util.*

data class WithdrawalEntityState (
    val withdrawalEntity: WithdrawalEntity? = WithdrawalEntity(0, emptyString, ZERO.toLong(), emptyString, emptyString, 0.0, emptyString, emptyString, emptyString),
    val isLoading: Boolean = false
)