package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.ExpenseEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import kotlinx.coroutines.flow.Flow


interface ExpenseRepository {

    fun getAllExpenses(): Flow<Resource<ExpenseEntities?>>

    suspend fun addExpense(expense: ExpenseEntity): Flow<Resource<String?>>

    suspend fun addExpenses(expenses: ExpenseEntities)

    suspend fun getExpense(uniqueExpenseId: String): ExpenseEntity?

    suspend fun getExpenseByName(expenseName: String): ExpenseEntities?

    suspend fun updateExpense(expense: ExpenseEntity): Flow<Resource<String?>>

    suspend fun deleteExpense(expenseId: Int)

    suspend fun deleteExpense(uniqueExpenseId: String): Flow<Resource<String?>>

    suspend fun deleteAllExpenses()

    suspend fun getExpenseAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getAverageDailyExpenses(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>

    suspend fun getExpenseTypeAmounts(periodDropDownItem: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>
    suspend fun getMinimumExpenseDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun getMaximumExpenseDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>
    suspend fun generateExpenseList(context: Context, expenses: ExpenseEntities): Flow<Resource<String?>>

}
