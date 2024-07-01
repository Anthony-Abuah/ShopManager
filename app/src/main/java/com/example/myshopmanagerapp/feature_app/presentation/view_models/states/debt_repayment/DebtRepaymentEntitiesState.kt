package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.debt_repayment

import com.example.myshopmanagerapp.core.DebtRepaymentEntities

data class DebtRepaymentEntitiesState (
    val debtRepaymentEntities: DebtRepaymentEntities? = emptyList(),
    val isLoading: Boolean = false
)