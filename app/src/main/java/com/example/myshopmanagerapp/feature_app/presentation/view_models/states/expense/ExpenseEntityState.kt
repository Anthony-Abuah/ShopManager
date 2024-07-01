package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.expense

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import java.time.LocalDate
import java.util.*
private val date = LocalDate.now().toDate().time
data class ExpenseEntityState (
    val expenseEntity: ExpenseEntity? = ExpenseEntity(0, emptyString, date, emptyString, emptyString, 0.0, emptyString,  emptyString, emptyString),
    val isLoading: Boolean = false
)