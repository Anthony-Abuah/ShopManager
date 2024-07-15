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
import com.example.myshopmanagerapp.core.SavingsEntities
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.repository.SavingsRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.savings.SavingsEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.savings.SavingsEntityState
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
class SavingsViewModel @Inject constructor(
    private val savingsRepository: SavingsRepository
): ViewModel() {

    private val date = LocalDate.now().toDate().time
    private val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    var savingsInfo by mutableStateOf(SavingsEntity(0, emptyString, date, dayOfWeek, 0.0, emptyString, emptyString, emptyString, emptyString))
        private set

    var addSavingsInfo by mutableStateOf(SavingsEntity(0, emptyString, date, dayOfWeek, 0.0, emptyString, emptyString, emptyString, emptyString))
        private set


    private val _generateSavingsListState = mutableStateOf(AddCompanyState())
    val generateSavingsListState: State<AddCompanyState> = _generateSavingsListState


    private val _addSavingsState = mutableStateOf(ItemValueState())
    val addSavingsState: State<ItemValueState> = _addSavingsState

    private val _updateSavingsState = mutableStateOf(ItemValueState())
    val updateSavingsState: State<ItemValueState> = _updateSavingsState

    private val _deleteSavingsState = mutableStateOf(ItemValueState())
    val deleteSavingsState: State<ItemValueState> = _deleteSavingsState

    private val _savingsEntityState = mutableStateOf(SavingsEntityState())
    val savingsEntityState: State<SavingsEntityState> = _savingsEntityState

    private val _savingsEntitiesState = mutableStateOf(SavingsEntitiesState())
    val savingsEntitiesState: State<SavingsEntitiesState> = _savingsEntitiesState

    private val _savingsAmount = mutableStateOf(ItemValueState())
    val savingsAmount: State<ItemValueState> = _savingsAmount


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun generateSavingsListPDF(context: Context, savings: SavingsEntities, mapOfBankAccounts: Map <String, String>) = viewModelScope.launch {
        savingsRepository.generateSavingsList(context, savings, mapOfBankAccounts).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateSavingsListState.value = generateSavingsListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateSavingsListState.value = generateSavingsListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateSavingsListState.value = generateSavingsListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getAllSavings() = viewModelScope.launch {
        savingsRepository.getAllSavings().onEach { response->
            when(response){
                is Resource.Success ->{
                    _savingsEntitiesState.value = savingsEntitiesState.value.copy(
                        savingsEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _savingsEntitiesState.value = savingsEntitiesState.value.copy(
                        savingsEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _savingsEntitiesState.value = savingsEntitiesState.value.copy(
                        savingsEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getSavings(uniqueSavingsId: String) = viewModelScope.launch {
        savingsInfo = savingsRepository.getSavings(uniqueSavingsId) ?: SavingsEntity(0, emptyString, date, dayOfWeek, 0.0, emptyString, emptyString, emptyString, emptyString)
        _savingsEntityState.value = savingsEntityState.value.copy(
            savingsEntity = savingsRepository.getSavings(uniqueSavingsId),
            isLoading = false
        )
    }

    fun addSavings(savings: SavingsEntity) = viewModelScope.launch {
        savingsRepository.addSavings(savings).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addSavingsState.value = addSavingsState.value.copy(
                        message = response.data ,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _addSavingsState.value = addSavingsState.value.copy(
                        message = response.data ,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _addSavingsState.value = addSavingsState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateSavings(savings: SavingsEntity) = viewModelScope.launch {
        savingsRepository.updateSavings(savings).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateSavingsState.value = updateSavingsState.value.copy(
                        message = response.data ,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateSavingsState.value = updateSavingsState.value.copy(
                        message = response.data ,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateSavingsState.value = updateSavingsState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteSavings(uniqueSavingsId: String) = viewModelScope.launch {
        savingsRepository.deleteSavings(uniqueSavingsId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteSavingsState.value = deleteSavingsState.value.copy(
                        message = response.data ,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteSavingsState.value = deleteSavingsState.value.copy(
                        message = response.data ,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteSavingsState.value = deleteSavingsState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun getPeriodicSavingsAmount(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        savingsRepository.getPeriodicSavingsAmount(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _savingsAmount.value = savingsAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _savingsAmount.value = savingsAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _savingsAmount.value = savingsAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun updateSavingsDate(date: Long, dayOfWeek: String) {
        savingsInfo = savingsInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun updateSavingsAmount(amount: String) {
        savingsInfo = savingsInfo.copy(
            savingsAmount = convertToDouble(amount)
        )
    }
    fun updateBankPersonnelName(bankPersonnel: String) {
        savingsInfo = savingsInfo.copy(
            bankPersonnel = bankPersonnel
        )
    }
    fun updateOtherInfo(info: String) {
        savingsInfo = savingsInfo.copy(
            otherInfo = info
        )
    }


    fun addSavingsDate(date: Long, dayOfWeek: String) {
        addSavingsInfo = addSavingsInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun addSavingsAmount(amount: String) {
        addSavingsInfo = addSavingsInfo.copy(
            savingsAmount = convertToDouble(amount)
        )
    }
    fun addBankPersonnelName(bankPersonnel: String) {
        addSavingsInfo = addSavingsInfo.copy(
            bankPersonnel = bankPersonnel
        )
    }
    fun addUniqueBankId(id: String) {
        addSavingsInfo = addSavingsInfo.copy(
            uniqueBankAccountId = id
        )
    }
    fun addOtherInfo(info: String) {
        addSavingsInfo = addSavingsInfo.copy(
            otherInfo = info
        )
    }


}