package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.ExpenseEntities
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.ExpenseRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.expense.ExpenseEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.expense.ExpenseEntityState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val date = LocalDate.now().toDate().time
    private val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    var expenseInfo by mutableStateOf(ExpenseEntity(0, emptyString, date, dayOfWeek, emptyString, 0.0, emptyString, emptyString, emptyString))
        private set

    var addExpenseInfo by mutableStateOf(ExpenseEntity(0, emptyString, date, dayOfWeek, emptyString, 0.0, emptyString, emptyString, emptyString))
        private set


    private val _generateExpenseListState = mutableStateOf(AddCompanyState())
    val generateExpenseListState: State<AddCompanyState> = _generateExpenseListState


    private val _addExpenseState = mutableStateOf(ItemValueState())
    val addExpenseState: State<ItemValueState> = _addExpenseState

    private val _updateExpenseState = mutableStateOf(ItemValueState())
    val updateExpenseState: State<ItemValueState> = _updateExpenseState

    private val _deleteExpenseState = mutableStateOf(ItemValueState())
    val deleteExpenseState: State<ItemValueState> = _deleteExpenseState

    private val _expenseEntityState = mutableStateOf(ExpenseEntityState())
    val expenseEntityState: State<ExpenseEntityState> = _expenseEntityState

    private val _expenseEntitiesState = mutableStateOf(ExpenseEntitiesState())
    val expenseEntitiesState: State<ExpenseEntitiesState> = _expenseEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun generateExpenseListPDF(context: Context, expense: ExpenseEntities) = viewModelScope.launch {
        expenseRepository.generateExpenseList(context, expense).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateExpenseListState.value = generateExpenseListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateExpenseListState.value = generateExpenseListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateExpenseListState.value = generateExpenseListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }



    fun getAllExpenses() = viewModelScope.launch {
        expenseRepository.getAllExpenses().onEach { response->
            when(response){
                is Resource.Success ->{
                    _expenseEntitiesState.value = expenseEntitiesState.value.copy(
                        expenseEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _expenseEntitiesState.value = expenseEntitiesState.value.copy(
                        expenseEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _expenseEntitiesState.value = expenseEntitiesState.value.copy(
                        expenseEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun addExpense(expenseEntity: ExpenseEntity) = viewModelScope.launch {
        expenseRepository.addExpense(expenseEntity).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addExpenseState.value = addExpenseState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true,
                    )
                }
                is Resource.Loading ->{
                    _addExpenseState.value = addExpenseState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false,
                    )
                }
                is Resource.Error ->{
                    _addExpenseState.value = addExpenseState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateExpense(expenseEntity: ExpenseEntity) = viewModelScope.launch {
        expenseRepository.updateExpense(expenseEntity).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateExpenseState.value = updateExpenseState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true,
                    )
                }
                is Resource.Loading ->{
                    _updateExpenseState.value = updateExpenseState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false,
                    )
                }
                is Resource.Error ->{
                    _updateExpenseState.value = updateExpenseState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteExpense(uniqueExpenseId: String) = viewModelScope.launch {
        expenseRepository.deleteExpense(uniqueExpenseId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteExpenseState.value = deleteExpenseState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true,
                    )
                }
                is Resource.Loading ->{
                    _deleteExpenseState.value = deleteExpenseState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false,
                    )
                }
                is Resource.Error ->{
                    _deleteExpenseState.value = deleteExpenseState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                    )
                }
            }
        }.launchIn(this)
    }

    fun getExpense(uniqueExpenseId: String) = viewModelScope.launch {
        expenseInfo = expenseRepository.getExpense(uniqueExpenseId) ?: ExpenseEntity(0, emptyString, date, dayOfWeek, emptyString, 0.0, emptyString, emptyString, emptyString)
        _expenseEntityState.value = expenseEntityState.value.copy(
            expenseEntity = expenseRepository.getExpense(uniqueExpenseId),
            isLoading = false
        )
    }


    fun updateExpenseDate(date: Long, dayOfWeek: String) {
        expenseInfo = expenseInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun updateExpenseName(expenseName: String) {
        expenseInfo = expenseInfo.copy(
            expenseName = expenseName
        )
    }
    fun updateExpenseType(type: String) {
        expenseInfo = expenseInfo.copy(
            expenseType = type
        )
    }
    fun updateExpenseAmount(amount: Double) {
        expenseInfo = expenseInfo.copy(
            expenseAmount = amount
        )
    }
    fun updateExpenseOtherInfo(info: String) {
        expenseInfo = expenseInfo.copy(
            otherInfo = info
        )
    }

    fun addExpenseDate(date: Long, dayOfWeek: String) {
        addExpenseInfo = addExpenseInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun addExpenseName(expenseName: String) {
        addExpenseInfo = addExpenseInfo.copy(
            expenseName = expenseName
        )
    }
    fun addExpenseType(type: String) {
        addExpenseInfo = addExpenseInfo.copy(
            expenseType = type
        )
    }
    fun addExpenseAmount(amount: Double) {
        addExpenseInfo = addExpenseInfo.copy(
            expenseAmount = amount
        )
    }
    fun addExpenseOtherInfo(info: String) {
        addExpenseInfo = addExpenseInfo.copy(
            otherInfo = info
        )
    }



}