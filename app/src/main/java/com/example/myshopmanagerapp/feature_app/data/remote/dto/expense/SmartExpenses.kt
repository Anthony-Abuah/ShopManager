package com.example.myshopmanagerapp.feature_app.data.remote.dto.expense

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartExpenses(
    val expenses: List<ExpenseInfoDto>,
    val uniqueExpenseIds: List<UniqueId>
)