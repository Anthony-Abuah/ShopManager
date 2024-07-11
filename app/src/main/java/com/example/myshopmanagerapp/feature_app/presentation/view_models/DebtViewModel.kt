package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.DebtEntities
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.repository.DebtRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.debt.DebtEntitiesState
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
class DebtViewModel @Inject constructor(
    private val debtRepository: DebtRepository
): ViewModel() {
    private val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    private val dateToday = LocalDate.now().toDate().time

    var debtInfo by mutableStateOf(DebtEntity(0, emptyString, dateToday, dayOfWeek, emptyString, 0.0, emptyString, emptyString))
        private set

    var addDebtInfo by mutableStateOf(DebtEntity(0, emptyString, dateToday, dayOfWeek, emptyString, 0.0, emptyString, emptyString))
        private set

    private val _generateDebtListState = mutableStateOf(AddCompanyState())
    val generateDebtListState: State<AddCompanyState> = _generateDebtListState

    private val _addDebtState = mutableStateOf(ItemValueState())
    val addDebtState: State<ItemValueState> = _addDebtState

    private val _updateDebtState = mutableStateOf(ItemValueState())
    val updateDebtState: State<ItemValueState> = _updateDebtState

    private val _deleteDebtState = mutableStateOf(ItemValueState())
    val deleteDebtState: State<ItemValueState> = _deleteDebtState

    private val _debtEntitiesState = mutableStateOf(DebtEntitiesState())
    val debtEntitiesState: State<DebtEntitiesState> = _debtEntitiesState

    private val _debtAmount = mutableStateOf(ItemValueState())
    val debtAmount: State<ItemValueState> = _debtAmount


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun generateDebtListPDF(context: Context, debts: DebtEntities, mapOfCustomers: Map <String, String>) = viewModelScope.launch {
        debtRepository.generateDebtList(context, debts, mapOfCustomers).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateDebtListState.value = generateDebtListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateDebtListState.value = generateDebtListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateDebtListState.value = generateDebtListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getAllDebt() = viewModelScope.launch {
        debtRepository.getAllDebt().onEach { response->
            when(response){
                is Resource.Success ->{
                    _debtEntitiesState.value = debtEntitiesState.value.copy(
                        debtEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _debtEntitiesState.value = debtEntitiesState.value.copy(
                        debtEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _debtEntitiesState.value = debtEntitiesState.value.copy(
                        debtEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getDebt(uniqueDebtId: String) = viewModelScope.launch {
        debtInfo = debtRepository.getDebt(uniqueDebtId) ?: DebtEntity(0, emptyString, dateToday, emptyString, emptyString, 0.0, emptyString, emptyString)
    }

    fun addDebt(debt: DebtEntity) = viewModelScope.launch {
        debtRepository.addDebt(debt).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addDebtState.value = addDebtState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _addDebtState.value = addDebtState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _addDebtState.value = addDebtState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateDebt(debt: DebtEntity) = viewModelScope.launch {
        debtRepository.updateDebt(debt).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateDebtState.value = updateDebtState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateDebtState.value = updateDebtState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateDebtState.value = updateDebtState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteDebt(uniqueDebtId: String) = viewModelScope.launch {
        debtRepository.deleteDebt(uniqueDebtId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteDebtState.value = deleteDebtState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteDebtState.value = deleteDebtState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteDebtState.value = deleteDebtState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun getDebtAmount(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        debtRepository.getDebtAmount(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _debtAmount.value = debtAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _debtAmount.value = debtAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _debtAmount.value = debtAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun updateDebtDate(date: Long) {
        debtInfo = debtInfo.copy(
            date = date
        )
    }
    fun updateDebtDayOfTheWeek(day: String) {
        debtInfo = debtInfo.copy(
            dayOfWeek = day
        )
    }
    fun updateDebtAmount(amount: Double) {
        debtInfo = debtInfo.copy(
            debtAmount = amount
        )
    }
    fun updateDebtShortNotes(shortNotes: String) {
        debtInfo = debtInfo.copy(
            otherInfo = shortNotes
        )
    }

    fun addDebtDate(date: Long) {
        addDebtInfo = addDebtInfo.copy(
            date = date
        )
    }
    fun addDebtDayOfTheWeek(day: String) {
        addDebtInfo = addDebtInfo.copy(
            dayOfWeek = day
        )
    }
    fun addDebtCustomer(customerId: String) {
        addDebtInfo = addDebtInfo.copy(
            uniqueCustomerId = customerId
        )
    }
    fun addDebtAmount(amount: Double) {
        addDebtInfo = addDebtInfo.copy(
            debtAmount = amount
        )
    }
    fun addDebtShortNotes(shortNotes: String) {
        addDebtInfo = addDebtInfo.copy(
            otherInfo = shortNotes
        )
    }

}