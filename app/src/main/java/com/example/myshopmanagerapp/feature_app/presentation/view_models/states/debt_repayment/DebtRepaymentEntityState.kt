package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.debt_repayment

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import java.util.*

data class DebtRepaymentEntityState (
    val debtRepaymentEntity: DebtRepaymentEntity? = DebtRepaymentEntity(0, emptyString, 0, emptyString, emptyString, 0.0, emptyString, emptyString),
    val isLoading: Boolean = false
)