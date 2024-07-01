package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.RevenueEntities
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.repository.RevenueRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.RevenueEntitiesState
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
class RevenueViewModel @Inject constructor(
    private val revenueRepository: RevenueRepository,
): ViewModel() {

    private val SALES = "Sales"
    private val date = LocalDate.now().toDate().time
    private val dayOfTheWeek = LocalDate.now().dayOfWeek.toString().lowercase(Locale.ROOT)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    var revenueInfo by mutableStateOf(RevenueEntity(0, emptyString, date, dayOfTheWeek, 0, SALES,0.0, emptyString, emptyString))
        private set

    var addRevenueInfo by mutableStateOf(RevenueEntity(0, emptyString, date, dayOfTheWeek, 0, SALES,0.0, emptyString, emptyString))
        private set


    private val _generateRevenueListState = mutableStateOf(AddCompanyState())
    val generateRevenueListState: State<AddCompanyState> = _generateRevenueListState


    private val _revenueAmount = mutableStateOf(ItemValueState())
    val revenueAmount: State<ItemValueState> = _revenueAmount

    private val _expenseAmount = mutableStateOf(ItemValueState())
    val expenseAmount: State<ItemValueState> = _expenseAmount

    private val _debtAmount = mutableStateOf(ItemValueState())
    val debtAmount: State<ItemValueState> = _debtAmount

    private val _debtRepaymentAmount = mutableStateOf(ItemValueState())
    val debtRepaymentAmount: State<ItemValueState> = _debtRepaymentAmount

    private val _revenueDays = mutableStateOf(ItemValueState())
    val revenueDays: State<ItemValueState> = _revenueDays

    private val _revenueHours = mutableStateOf(ItemValueState())
    val revenueHours: State<ItemValueState> = _revenueHours

    private val _maxRevenue = mutableStateOf(ItemValueState())
    val maxRevenue: State<ItemValueState> = _maxRevenue

    private val _minRevenue = mutableStateOf(ItemValueState())
    val minRevenue: State<ItemValueState> = _minRevenue

    private val _maxExpense = mutableStateOf(ItemValueState())
    val maxExpense: State<ItemValueState> = _maxExpense

    private val _minExpense = mutableStateOf(ItemValueState())
    val minExpense: State<ItemValueState> = _minExpense

    private val _inventoryCost = mutableStateOf(ItemValueState())
    val inventoryCost: State<ItemValueState> = _inventoryCost


    private val _insertRevenueState = mutableStateOf(ItemValueState())
    val insertRevenueState: State<ItemValueState> = _insertRevenueState

    private val _updateRevenueState = mutableStateOf(ItemValueState())
    val updateRevenueState: State<ItemValueState> = _updateRevenueState

    private val _deleteRevenueState = mutableStateOf(ItemValueState())
    val deleteRevenueState: State<ItemValueState> = _deleteRevenueState

    private val _revenueEntitiesState = mutableStateOf(RevenueEntitiesState())
    val revenueEntitiesState: State<RevenueEntitiesState> = _revenueEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()



    fun generateRevenueListPDF(context: Context, revenue: RevenueEntities) = viewModelScope.launch {
        revenueRepository.generateRevenueList(context, revenue).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateRevenueListState.value = generateRevenueListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateRevenueListState.value = generateRevenueListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateRevenueListState.value = generateRevenueListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun getRevenueAmount(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getRevenueAmount(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _revenueAmount.value = revenueAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _revenueAmount.value = revenueAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _revenueAmount.value = revenueAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getExpenseAmount(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getExpenseAmount(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _expenseAmount.value = expenseAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _expenseAmount.value = expenseAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _expenseAmount.value = expenseAmount.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getDebtAmount(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getDebtAmount(periodDropDownItem).onEach { response->
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

    fun getDebtRepaymentAmount(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getDebtRepaymentAmount(periodDropDownItem).onEach { response->
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

    fun getRevenueDays(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getRevenueDays(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _revenueDays.value = revenueDays.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _revenueDays.value = revenueDays.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _revenueDays.value = revenueDays.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getRevenueHours(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getRevenueHours(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _revenueHours.value = revenueHours.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _revenueHours.value = revenueHours.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _revenueHours.value = revenueHours.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getMaxRevenue(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getMaximumRevenueDay(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _maxRevenue.value = maxRevenue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _maxRevenue.value = maxRevenue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _maxRevenue.value = maxRevenue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getMinRevenue(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getMinimumRevenueDay(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _minRevenue.value = minRevenue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _minRevenue.value = minRevenue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _minRevenue.value = minRevenue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getMaxExpense(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getMaximumExpenseDay(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _maxExpense.value = maxExpense.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _maxExpense.value = maxExpense.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _maxExpense.value = maxExpense.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getMinExpense(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getMinimumExpenseDay(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _minExpense.value = minExpense.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _minExpense.value = minExpense.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _minExpense.value = minExpense.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getInventoryCost(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        revenueRepository.getCostOfInventory(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _inventoryCost.value = inventoryCost.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _inventoryCost.value = inventoryCost.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _inventoryCost.value = inventoryCost.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getAllRevenues() = viewModelScope.launch {
        revenueRepository.getAllRevenues().onEach { response->
            when(response){
                is Resource.Success ->{
                    _revenueEntitiesState.value = revenueEntitiesState.value.copy(
                        revenueEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _revenueEntitiesState.value = revenueEntitiesState.value.copy(
                        revenueEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _revenueEntitiesState.value = revenueEntitiesState.value.copy(
                        revenueEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getRevenue(uniqueRevenueId: String) = viewModelScope.launch {
        revenueInfo = revenueRepository.getRevenue(uniqueRevenueId) ?: RevenueEntity(0, emptyString, date, dayOfTheWeek, 0, SALES,0.0, emptyString, emptyString)
    }

    fun deleteRevenue(uniqueRevenueId: String) = viewModelScope.launch {
        revenueRepository.deleteRevenue(uniqueRevenueId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteRevenueState.value = deleteRevenueState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteRevenueState.value = deleteRevenueState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteRevenueState.value = deleteRevenueState.value.copy(
                        message = response.message ?: emptyString,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun insertRevenue(revenue: RevenueEntity) = viewModelScope.launch {
        revenueRepository.addRevenue(revenue).onEach { response->
            when(response){
                is Resource.Success ->{
                    _insertRevenueState.value = insertRevenueState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _insertRevenueState.value = insertRevenueState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _insertRevenueState.value = insertRevenueState.value.copy(
                        message = response.message ?: emptyString,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateRevenue(revenue: RevenueEntity) = viewModelScope.launch {
        revenueRepository.updateRevenue(revenue).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateRevenueState.value = updateRevenueState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateRevenueState.value = updateRevenueState.value.copy(
                        message = response.data ?: emptyString,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateRevenueState.value = updateRevenueState.value.copy(
                        message = response.message ?: emptyString,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun updateRevenueDate(date: Long, dayOfWeek: String) {
        revenueInfo = revenueInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun updateRevenueDayOfWeek(dayOfWeek: String) {
        revenueInfo = revenueInfo.copy(
            dayOfWeek = dayOfWeek
        )
    }
    fun updateRevenueType(type: String) {
        addRevenueInfo = addRevenueInfo.copy(
            revenueType = type
        )
    }
    fun updateRevenueNumberOfHours(numberOfHours: Int) {
        revenueInfo = revenueInfo.copy(
            numberOfHours = numberOfHours
        )
    }
    fun updateRevenueOtherInfo(info: String) {
        revenueInfo = revenueInfo.copy(
            otherInfo = info
        )
    }
    fun updateRevenueAmount(amount: Double) {
        revenueInfo = revenueInfo.copy(
            revenueAmount = amount
        )
    }
    fun updatePersonnelId(id: String) {
        revenueInfo = revenueInfo.copy(
            uniquePersonnelId = id
        )
    }


    fun addUniqueRevenueId(id: String) {
        addRevenueInfo = addRevenueInfo.copy(
            uniqueRevenueId = id
        )
    }
    fun addRevenueDate(date: Long, dayOfWeek: String) {
        addRevenueInfo = addRevenueInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun addRevenueNumberOfHours(numberOfHours: Int) {
        addRevenueInfo = addRevenueInfo.copy(
            numberOfHours = numberOfHours
        )
    }
    fun addRevenueOtherInfo(info: String) {
        addRevenueInfo = addRevenueInfo.copy(
            otherInfo = info
        )
    }
    fun addRevenueType(type: String) {
        addRevenueInfo = addRevenueInfo.copy(
            revenueType = type
        )
    }
    fun addRevenueAmount(amount: Double) {
        addRevenueInfo = addRevenueInfo.copy(
            revenueAmount = amount
        )
    }
    fun addRevenueUniquePersonnelId(id: String) {
        addRevenueInfo = addRevenueInfo.copy(
            uniquePersonnelId = id
        )
    }

}