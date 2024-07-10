package com.example.myshopmanagerapp.feature_app.presentation.view_models

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.BankAccountRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.bank.BankEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankAccountViewModel @Inject constructor(
    private val bankAccountRepository: BankAccountRepository
): ViewModel() {

    var bankAccountInfo by mutableStateOf<BankAccountEntity?>(null)
        private set

    var addBankAccountInfo by mutableStateOf<BankAccountEntity?>(BankAccountEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, 0.0))
        private set

    private val _addBankAccountState = mutableStateOf(ItemValueState())
    val addBankAccountState: State<ItemValueState> = _addBankAccountState

    private val _updateBankAccountState = mutableStateOf(ItemValueState())
    val updateBankAccountState: State<ItemValueState> = _updateBankAccountState

    private val _deleteBankAccountState = mutableStateOf(ItemValueState())
    val deleteBankState: State<ItemValueState> = _deleteBankAccountState

    private val _bankEntitiesState = mutableStateOf(BankEntitiesState())
    val bankAccountEntitiesState: State<BankEntitiesState> = _bankEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllBankAccounts() = viewModelScope.launch {
        bankAccountRepository.getAllBanks().onEach { response->
            when(response){
                is Resource.Success ->{
                    _bankEntitiesState.value = bankAccountEntitiesState.value.copy(
                        bankAccountEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _bankEntitiesState.value = bankAccountEntitiesState.value.copy(
                        bankAccountEntities = response.data ?: emptyList(),
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _bankEntitiesState.value = bankAccountEntitiesState.value.copy(
                        bankAccountEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getBankAccount(uniqueBankAccountId: String) = viewModelScope.launch {
        bankAccountInfo = bankAccountRepository.getBankAccount(uniqueBankAccountId)
    }

    fun addBankAccount(bankAccount: BankAccountEntity) = viewModelScope.launch {
        bankAccountRepository.addBankAccount(bankAccount).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addBankAccountState.value = addBankAccountState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _addBankAccountState.value = addBankAccountState.value.copy(
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _addBankAccountState.value = addBankAccountState.value.copy(
                        message = response.message ?: emptyString,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateBankAccount(bankAccount: BankAccountEntity) = viewModelScope.launch {
        bankAccountRepository.updateBankAccount(bankAccount).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateBankAccountState.value = updateBankAccountState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateBankAccountState.value = updateBankAccountState.value.copy(
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateBankAccountState.value = updateBankAccountState.value.copy(
                        message = response.message ?: emptyString,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteBankAccount(uniqueBankAccountId: String) = viewModelScope.launch {
        bankAccountRepository.deleteBankAccount(uniqueBankAccountId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteBankAccountState.value = deleteBankState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteBankAccountState.value = deleteBankState.value.copy(
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteBankAccountState.value = deleteBankState.value.copy(
                        message = response.message ?: emptyString,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun updateBankName(name: String) {
        bankAccountInfo = bankAccountInfo?.copy(
            bankName = name
        )
    }
    fun updateBankAccountName(name: String) {
        bankAccountInfo = bankAccountInfo?.copy(
            bankAccountName = name
        )
    }
    fun updateBankContact(contact: String) {
        bankAccountInfo = bankAccountInfo?.copy(
            bankContact = contact
        )
    }
    fun updateBankLocation(location: String) {
        bankAccountInfo = bankAccountInfo?.copy(
            bankLocation = location
        )
    }
    fun updateBankOtherInfo(info: String) {
        bankAccountInfo = bankAccountInfo?.copy(
            otherInfo = info
        )
    }

}