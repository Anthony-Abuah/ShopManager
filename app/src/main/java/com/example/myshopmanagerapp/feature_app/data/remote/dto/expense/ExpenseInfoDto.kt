package com.example.myshopmanagerapp.feature_app.data.remote.dto.expense

import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity


data class ExpenseInfoDto(
    val uniqueExpenseId: String,
    val uniqueCompanyId: String,
    val uniquePersonnelId: String,
    val date: Long,
    val dayOfWeek: String,
    val expenseName: String,
    val expenseAmount: Double,
    val expenseType: String,
    val otherInfo: String
){
    fun toExpenseEntity(): ExpenseEntity{
        return ExpenseEntity(
            0,
            uniqueExpenseId = uniqueExpenseId,
            uniquePersonnelId = uniquePersonnelId,
            dayOfWeek = dayOfWeek,
            date = date,
            expenseName = expenseName,
            expenseAmount = expenseAmount,
            expenseType = expenseType,
            otherInfo = otherInfo
        )
    }
}
