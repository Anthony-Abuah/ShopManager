package com.example.myshopmanagerapp.feature_app.data.local.entities.expenses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Expense_Table
import com.example.myshopmanagerapp.feature_app.data.remote.dto.expense.ExpenseInfoDto

@Entity(tableName = Expense_Table)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val expenseId: Int,
    val uniqueExpenseId: String,
    val date: Long,
    val dayOfWeek: String?,
    val expenseName: String,
    val expenseAmount: Double,
    val expenseType: String,
    val uniquePersonnelId: String,
    val otherInfo: String?
){
    fun toExpenseInfoDto(uniqueCompanyId: String): ExpenseInfoDto{
        return ExpenseInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            uniqueExpenseId = uniqueExpenseId,
            uniquePersonnelId = uniquePersonnelId,
            date = date,
            dayOfWeek = dayOfWeek,
            expenseName = expenseName,
            expenseAmount = expenseAmount,
            expenseType = expenseType,
            otherInfo = otherInfo
        )
    }
}

