package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.core.WithdrawalEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.WithdrawalRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.withdrawal.WithdrawalEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.withdrawal.WithdrawalEntityState
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
class WithdrawalViewModel @Inject constructor(
    private val withdrawalRepository: WithdrawalRepository
): ViewModel() {
    private val date = LocalDate.now().toDate().time
    private val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    var withdrawalInfo by mutableStateOf(WithdrawalEntity(0, emptyString, date, dayOfWeek, emptyString, 0.0, emptyString, emptyString, emptyString))
        private set

    var addWithdrawalInfo by mutableStateOf(WithdrawalEntity(0, emptyString, date, dayOfWeek, emptyString, 0.0, emptyString, emptyString, emptyString))
        private set

    private val _generateWithdrawalListState = mutableStateOf(AddCompanyState())
    val generateWithdrawalListState: State<AddCompanyState> = _generateWithdrawalListState

    private val _addWithdrawalState = mutableStateOf(ItemValueState())
    val addWithdrawalState: State<ItemValueState> = _addWithdrawalState

    private val _updateWithdrawalState = mutableStateOf(ItemValueState())
    val updateWithdrawalState: State<ItemValueState> = _updateWithdrawalState

    private val _deleteWithdrawalState = mutableStateOf(ItemValueState())
    val deleteWithdrawalState: State<ItemValueState> = _deleteWithdrawalState

    private val _withdrawalEntityState = mutableStateOf(WithdrawalEntityState())
    val withdrawalEntityState: State<WithdrawalEntityState> = _withdrawalEntityState

    private val _withdrawalEntitiesState = mutableStateOf(WithdrawalEntitiesState())
    val withdrawalEntitiesState: State<WithdrawalEntitiesState> = _withdrawalEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun generateWithdrawalListPDF(context: Context, withdrawals: WithdrawalEntities, mapOfBankAccounts: Map <String, String>) = viewModelScope.launch {
        withdrawalRepository.generateWithdrawalsList(context, withdrawals, mapOfBankAccounts).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateWithdrawalListState.value = generateWithdrawalListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateWithdrawalListState.value = generateWithdrawalListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateWithdrawalListState.value = generateWithdrawalListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getAllWithdrawals() = viewModelScope.launch {
        withdrawalRepository.getAllWithdrawals().onEach { response->
            when(response){
                is Resource.Success ->{
                    _withdrawalEntitiesState.value = withdrawalEntitiesState.value.copy(
                        withdrawalEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _withdrawalEntitiesState.value = withdrawalEntitiesState.value.copy(
                        withdrawalEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _withdrawalEntitiesState.value = withdrawalEntitiesState.value.copy(
                        withdrawalEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getWithdrawal(uniqueWithdrawalId: String) = viewModelScope.launch {
        withdrawalInfo = withdrawalRepository.getWithdrawal(uniqueWithdrawalId) ?: WithdrawalEntity(0, emptyString, date, dayOfWeek, emptyString, 0.0, emptyString, emptyString, emptyString)
        _withdrawalEntityState.value = withdrawalEntityState.value.copy(
            withdrawalEntity = withdrawalRepository.getWithdrawal(uniqueWithdrawalId),
            isLoading = false
        )
    }

    fun addWithdrawal(withdrawal: WithdrawalEntity) = viewModelScope.launch {
        withdrawalRepository.addWithdrawal(withdrawal).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addWithdrawalState.value = addWithdrawalState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false,
                    )
                }
                is Resource.Loading ->{
                    _addWithdrawalState.value = addWithdrawalState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true,
                    )
                }
                is Resource.Error ->{
                    _addWithdrawalState.value = addWithdrawalState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false,
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateWithdrawal(withdrawal: WithdrawalEntity) = viewModelScope.launch {
        withdrawalRepository.updateWithdrawal(withdrawal).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateWithdrawalState.value = updateWithdrawalState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false,
                    )
                }
                is Resource.Loading ->{
                    _updateWithdrawalState.value = updateWithdrawalState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true,
                    )
                }
                is Resource.Error ->{
                    _updateWithdrawalState.value = updateWithdrawalState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false,
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteWithdrawal(uniqueWithdrawalId: String) = viewModelScope.launch {
        withdrawalRepository.deleteWithdrawal(uniqueWithdrawalId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteWithdrawalState.value = deleteWithdrawalState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false,
                    )
                }
                is Resource.Loading ->{
                    _deleteWithdrawalState.value = deleteWithdrawalState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = true,
                    )
                }
                is Resource.Error ->{
                    _deleteWithdrawalState.value = deleteWithdrawalState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false,
                    )
                }
            }
        }.launchIn(this)
    }


    fun updateWithdrawalDate(date: Long, dayOfWeek: String) {
        withdrawalInfo = withdrawalInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }

    fun updateTransactionId(id: String) {
        withdrawalInfo = withdrawalInfo.copy(
            transactionId = id
        )
    }
    fun updateWithdrawalAmount(amount: String) {
        withdrawalInfo = withdrawalInfo.copy(
            withdrawalAmount = convertToDouble(amount)
        )
    }
    fun updateOtherInfo(info: String) {
        withdrawalInfo = withdrawalInfo.copy(
            otherInfo = info
        )
    }


    fun addWithdrawalDate(date: Long, dayOfWeek: String) {
        addWithdrawalInfo = addWithdrawalInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun addTransactionId(id: String) {
        addWithdrawalInfo = addWithdrawalInfo.copy(
            transactionId = id
        )
    }
    fun addWithdrawalAmount(amount: String) {
        addWithdrawalInfo = addWithdrawalInfo.copy(
            withdrawalAmount = convertToDouble(amount)
        )
    }
    fun addUniqueBankId(id: String) {
        addWithdrawalInfo = addWithdrawalInfo.copy(
            uniqueBankAccountId = id
        )
    }
    fun addOtherInfo(info: String) {
        addWithdrawalInfo = addWithdrawalInfo.copy(
            otherInfo = info
        )
    }


}