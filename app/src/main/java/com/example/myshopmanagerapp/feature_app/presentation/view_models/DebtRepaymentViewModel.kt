package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.DebtRepaymentEntities
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.repository.DebtRepaymentRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.debt_repayment.DebtRepaymentEntitiesState
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
class DebtRepaymentViewModel @Inject constructor(
    private val debtRepaymentRepository: DebtRepaymentRepository
): ViewModel() {

    private val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    private val dateToday = LocalDate.now().toDate().time

    var debtRepaymentInfo by mutableStateOf(DebtRepaymentEntity(0, emptyString, dateToday, dayOfWeek, emptyString, 0.0, emptyString, emptyString))
        private set

    var addDebtRepaymentInfo by mutableStateOf(DebtRepaymentEntity(0, emptyString, dateToday, dayOfWeek, emptyString, 0.0, emptyString, emptyString))
        private set


    private val _generateDebtRepaymentListState = mutableStateOf(AddCompanyState())
    val generateDebtRepaymentListState: State<AddCompanyState> = _generateDebtRepaymentListState

    private val _addDebtRepaymentState = mutableStateOf(ItemValueState())
    val addDebtRepaymentState: State<ItemValueState> = _addDebtRepaymentState

    private val _updateDebtRepaymentState = mutableStateOf(ItemValueState())
    val updateDebtRepaymentState: State<ItemValueState> = _updateDebtRepaymentState

    private val _deleteDebtRepaymentState = mutableStateOf(ItemValueState())
    val deleteDebtRepaymentState: State<ItemValueState> = _deleteDebtRepaymentState

    private val _debtRepaymentEntitiesState = mutableStateOf(DebtRepaymentEntitiesState())
    val debtRepaymentEntitiesState: State<DebtRepaymentEntitiesState> = _debtRepaymentEntitiesState

    private val _debtRepaymentAmount = mutableStateOf(ItemValueState())
    val debtRepaymentAmount: State<ItemValueState> = _debtRepaymentAmount


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun generateDebtRepaymentListPDF(context: Context, debtRepayments: DebtRepaymentEntities, mapOfCustomers: Map <String, String>) = viewModelScope.launch {
        debtRepaymentRepository.generateDebtRepaymentList(context, debtRepayments, mapOfCustomers).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateDebtRepaymentListState.value = generateDebtRepaymentListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateDebtRepaymentListState.value = generateDebtRepaymentListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateDebtRepaymentListState.value = generateDebtRepaymentListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun getAllDebtRepayment() = viewModelScope.launch {
        debtRepaymentRepository.getAllDebtRepayment().onEach { response->
            when(response){
                is Resource.Success ->{
                    _debtRepaymentEntitiesState.value = debtRepaymentEntitiesState.value.copy(
                        debtRepaymentEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _debtRepaymentEntitiesState.value = debtRepaymentEntitiesState.value.copy(
                        debtRepaymentEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _debtRepaymentEntitiesState.value = debtRepaymentEntitiesState.value.copy(
                        debtRepaymentEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getDebtRepayment(uniqueDebtRepaymentId: String) = viewModelScope.launch {
        debtRepaymentInfo = debtRepaymentRepository.getDebtRepayment(uniqueDebtRepaymentId) ?: DebtRepaymentEntity(0, emptyString, dateToday, dayOfWeek, emptyString, 0.0, emptyString, emptyString)
    }

    fun addDebtRepayment(debtRepayment: DebtRepaymentEntity) = viewModelScope.launch {
        debtRepaymentRepository.addDebtRepayment(debtRepayment).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addDebtRepaymentState.value = addDebtRepaymentState.value.copy(
                        message = response.data ,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _addDebtRepaymentState.value = addDebtRepaymentState.value.copy(
                        message = response.data ,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _addDebtRepaymentState.value = addDebtRepaymentState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateDebtRepayment(debtRepayment: DebtRepaymentEntity) = viewModelScope.launch {
        debtRepaymentRepository.updateDebtRepayment(debtRepayment).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateDebtRepaymentState.value = updateDebtRepaymentState.value.copy(
                        message = response.data ,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateDebtRepaymentState.value = updateDebtRepaymentState.value.copy(
                        message = response.data ,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateDebtRepaymentState.value = updateDebtRepaymentState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteDebtRepayment(uniqueDebtRepaymentId: String) = viewModelScope.launch {
        debtRepaymentRepository.deleteDebtRepayment(uniqueDebtRepaymentId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteDebtRepaymentState.value = deleteDebtRepaymentState.value.copy(
                        message = response.data ,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteDebtRepaymentState.value = deleteDebtRepaymentState.value.copy(
                        message = response.data ,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteDebtRepaymentState.value = deleteDebtRepaymentState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getPeriodicDebtRepaymentAmount(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        debtRepaymentRepository.getPeriodicDebtRepaymentAmount(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _debtRepaymentAmount.value = debtRepaymentAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _debtRepaymentAmount.value = debtRepaymentAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _debtRepaymentAmount.value = debtRepaymentAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun updateDebtRepaymentDate(date: Long, dayOfWeek: String) {
        debtRepaymentInfo = debtRepaymentInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun updateDebtRepaymentAmount(amount: Double) {
        debtRepaymentInfo = debtRepaymentInfo.copy(
            debtRepaymentAmount = amount
        )
    }
    fun updateDebtRepaymentShortNotes(shortNotes: String) {
        debtRepaymentInfo = debtRepaymentInfo.copy(
            otherInfo = shortNotes
        )
    }

    fun addDebtRepaymentDate(date: Long, dayOfWeek: String) {
        addDebtRepaymentInfo = addDebtRepaymentInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun addDebtRepaymentCustomer(customerId: String) {
        addDebtRepaymentInfo = addDebtRepaymentInfo.copy(
            uniqueCustomerId = customerId
        )
    }
    fun addDebtRepaymentAmount(amount: Double) {
        addDebtRepaymentInfo = addDebtRepaymentInfo.copy(
            debtRepaymentAmount = amount
        )
    }
    fun addDebtRepaymentShortNotes(shortNotes: String) {
        addDebtRepaymentInfo = addDebtRepaymentInfo.copy(
            otherInfo = shortNotes
        )
    }


}