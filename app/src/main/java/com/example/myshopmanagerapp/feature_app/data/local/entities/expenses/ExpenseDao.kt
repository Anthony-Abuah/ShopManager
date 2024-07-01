package com.example.myshopmanagerapp.feature_app.data.local.entities.expenses

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.Expense_Table
import com.example.myshopmanagerapp.core.ExpenseEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import java.util.*

@Dao
interface ExpenseDao {

    @Query ("SELECT * FROM $Expense_Table")
    suspend fun getAllExpenses(): ExpenseEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpenses(expenses: ExpenseEntities)

    @Query ("SELECT * FROM $Expense_Table WHERE uniqueExpenseId LIKE :uniqueExpenseId")
    suspend fun getExpense(uniqueExpenseId: String): ExpenseEntity?

    @Query ("SELECT * FROM $Expense_Table WHERE expenseName LIKE :expenseName")
    suspend fun getExpenseByName(expenseName: String): List<ExpenseEntity>?

    @Query ("DELETE FROM $Expense_Table")
    suspend fun deleteAllExpenses()

    @Query ("DELETE FROM $Expense_Table WHERE uniqueExpenseId LIKE :uniqueExpenseId")
    suspend fun deleteExpense(uniqueExpenseId: String)

    @Query ("DELETE FROM $Expense_Table WHERE expenseId LIKE :expenseId")
    suspend fun deleteExpense(expenseId: Int)

    @Query ("DELETE FROM $Expense_Table WHERE expenseId NOT IN (SELECT MIN(expenseId) FROM $Expense_Table GROUP BY uniqueExpenseId)")
    suspend fun deleteDuplicateExpenses()

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)


}