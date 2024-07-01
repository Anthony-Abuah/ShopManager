package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.expense

import com.example.myshopmanagerapp.core.ExpenseEntities

data class ExpenseEntitiesState (
    val expenseEntities: ExpenseEntities? = emptyList(),
    val isLoading: Boolean = false
)