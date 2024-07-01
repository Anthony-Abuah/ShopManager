package com.example.myshopmanagerapp.feature_app.presentation.view_models

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.StockRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.stock.AddStockState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.stock.StockEntitiesState
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
class StockViewModel @Inject constructor(
    private val stockRepository: StockRepository
): ViewModel() {
    private val date = LocalDate.now().toDate().time
    private val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase(Locale.ROOT)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    var stockInfo by mutableStateOf(StockEntity(0, emptyString, date, dayOfWeek, emptyString, emptyString, emptyList(), 0, date, 0, false, emptyString))
        private set

    var addStockInfo by mutableStateOf(StockEntity(0, emptyString, date, dayOfWeek, emptyString, emptyString, emptyList(), 0, date, 0, false, emptyString))
        private set

    private val _addStockState = mutableStateOf(AddStockState())
    val addStockState: State<AddStockState> = _addStockState

    private val _updateStockState = mutableStateOf(AddStockState())
    val updateStockState: State<AddStockState> = _updateStockState

    private val _deleteStockState = mutableStateOf(AddStockState())
    val deleteStockState: State<AddStockState> = _deleteStockState

    private val _stockEntitiesState = mutableStateOf(StockEntitiesState())
    val stockEntitiesState: State<StockEntitiesState> = _stockEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllStocks() = viewModelScope.launch {
        stockRepository.getAllStocks().onEach { response->
            when(response){
                is Resource.Success ->{
                    _stockEntitiesState.value = stockEntitiesState.value.copy(
                        stockEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _stockEntitiesState.value = stockEntitiesState.value.copy(
                        stockEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _stockEntitiesState.value = stockEntitiesState.value.copy(
                        stockEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getStock(uniqueStockId: String) = viewModelScope.launch {
        stockInfo = stockRepository.getStock(uniqueStockId) ?: StockEntity(0, emptyString, date, dayOfWeek, emptyString, emptyString, emptyList(), 0, date, 0, false, emptyString)

    }

    fun addStock(stock: StockEntity) = viewModelScope.launch {
        stockRepository.addStock(stock).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addStockState.value = addStockState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _addStockState.value = addStockState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _addStockState.value = addStockState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateStock(stock: StockEntity) = viewModelScope.launch {
        stockRepository.updateStock(stock).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateStockState.value = updateStockState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _updateStockState.value = updateStockState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _updateStockState.value = updateStockState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteStock(uniqueStockId: String) = viewModelScope.launch {
        stockRepository.deleteStock(uniqueStockId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteStockState.value = deleteStockState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _deleteStockState.value = deleteStockState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _deleteStockState.value = deleteStockState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateStockDate(date: Long, dayOfWeek: String) {
        stockInfo = stockInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }

    fun updateStockQuantities(quantities: ItemQuantities) {
        stockInfo = stockInfo.copy(
            stockQuantityInfo = quantities
        )
    }
    fun updateOtherInfo(otherInfo: String) {
        stockInfo = stockInfo.copy(
            otherInfo = otherInfo
        )
    }

    fun addStockDate(date: Long, dayOfWeek: String) {
        addStockInfo = addStockInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun addUniqueItemId(uniqueItemId: String) {
        addStockInfo = addStockInfo.copy(
            uniqueInventoryItemId = uniqueItemId
        )
    }
    fun addRemainingStock(remainingStock: ItemQuantities) {
        addStockInfo = addStockInfo.copy(
            stockQuantityInfo = remainingStock
        )
    }
    fun addOtherInfo(otherInfo: String) {
        addStockInfo = addStockInfo.copy(
            otherInfo = otherInfo
        )
    }



}