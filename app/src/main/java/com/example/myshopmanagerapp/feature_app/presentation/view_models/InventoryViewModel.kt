package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.repository.GeneratePDFRepository
import com.example.myshopmanagerapp.feature_app.domain.repository.InventoryRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory.InventoryEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory.InventoryState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item.ItemValuesState
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
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val generatePDFRepository: GeneratePDFRepository
): ViewModel() {

    private val date = LocalDate.now().toDate().time
    private val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    var inventoryInfo by mutableStateOf(InventoryEntity(0, emptyString, emptyString, emptyString, date, dayOfWeek,
        emptyList(), 0, 0.0, 0.0,emptyString, emptyString))
        private set

    var inventoryQuantityDisplayValues by mutableStateOf<List<InventoryQuantityDisplayValues>>(emptyList())
        private set

    var addInventoryInfo by mutableStateOf(InventoryEntity(0, emptyString, emptyString, emptyString, date, dayOfWeek,
        emptyList(), 0, 0.0, 0.0, emptyString, emptyString))
        private set

    private val _generateInventoryListState = mutableStateOf(AddCompanyState())
    val generateInventoryListState: State<AddCompanyState> = _generateInventoryListState


    private val _addInventoryState = mutableStateOf(InventoryState())
    val addInventoryState: State<InventoryState> = _addInventoryState

    private val _updateInventoryState = mutableStateOf(InventoryState())
    val updateInventoryState: State<InventoryState> = _updateInventoryState

    private val _deleteInventoryState = mutableStateOf(InventoryState())
    val deleteInventoryState: State<InventoryState> = _deleteInventoryState

    private val _inventoryItemQuantities = mutableStateOf(ItemValuesState())
    val inventoryItemQuantities: State<ItemValuesState> = _inventoryItemQuantities

    private val _inventoryEntitiesState = mutableStateOf(InventoryEntitiesState())
    val inventoryEntitiesState: State<InventoryEntitiesState> = _inventoryEntitiesState

    private val _totalNumberOfItems = mutableStateOf(ItemValueState())
    val totalNumberOfItems: State<ItemValueState> = _totalNumberOfItems

    private val _totalCostValue = mutableStateOf(ItemValueState())
    val totalCostValue: State<ItemValueState> = _totalCostValue

    private val _totalExpectedSalesValue = mutableStateOf(ItemValueState())
    val totalExpectedSalesValue: State<ItemValueState> = _totalExpectedSalesValue

    private val _mostAvailableItem = mutableStateOf(ItemValueState())
    val mostAvailableItem: State<ItemValueState> = _mostAvailableItem

    private val _leastAvailableItem = mutableStateOf(ItemValueState())
    val leastAvailableItem: State<ItemValueState> = _leastAvailableItem


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun generateInventoryListPDF(context: Context, date: String, inventories: List<InventoryQuantityDisplayValues>) = viewModelScope.launch {
        inventoryRepository.generateInventoryList(context, date, inventories).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateInventoryListState.value = generateInventoryListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateInventoryListState.value = generateInventoryListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateInventoryListState.value = generateInventoryListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }



    fun getAllInventories() = viewModelScope.launch {
        inventoryRepository.getAllInventories().onEach { response->
            when(response){
                is Resource.Success ->{
                    _inventoryEntitiesState.value = inventoryEntitiesState.value.copy(
                        inventoryEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _inventoryEntitiesState.value = inventoryEntitiesState.value.copy(
                        inventoryEntities = response.data ?: emptyList(),
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _inventoryEntitiesState.value = inventoryEntitiesState.value.copy(
                        inventoryEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getInventory(uniqueInventoryId: String) = viewModelScope.launch {
        inventoryInfo = inventoryRepository.getInventory(uniqueInventoryId) ?: InventoryEntity(0, emptyString, emptyString, emptyString, date, dayOfWeek,
            emptyList(), 0, 0.0, 0.0,emptyString, emptyString)
    }

    fun addInventoryWithStock(inventory: InventoryEntity) = viewModelScope.launch {
        inventoryRepository.addInventoryWithStock(inventory).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addInventoryState.value = addInventoryState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _addInventoryState.value = addInventoryState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _addInventoryState.value = addInventoryState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateInventory(inventory: InventoryEntity) = viewModelScope.launch {
        inventoryRepository.updateInventoryWithStock(inventory).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateInventoryState.value = updateInventoryState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _updateInventoryState.value = updateInventoryState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _updateInventoryState.value = updateInventoryState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteInventory(uniqueInventoryId: String) = viewModelScope.launch {
        inventoryRepository.deleteInventoryWithStock(uniqueInventoryId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteInventoryState.value = deleteInventoryState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _deleteInventoryState.value = deleteInventoryState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _deleteInventoryState.value = deleteInventoryState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun getInventoryItemQuantities(period: PeriodDropDownItem) = viewModelScope.launch {
        inventoryRepository.getItemsAndTheirQuantities(period).onEach { response->
            when(response){
                is Resource.Success ->{
                    _inventoryItemQuantities.value = inventoryItemQuantities.value.copy(
                        itemValues = response.data ?: emptyList(),
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _inventoryItemQuantities.value = inventoryItemQuantities.value.copy(
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _inventoryItemQuantities.value = inventoryItemQuantities.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getTotalNumberOfItems(period: PeriodDropDownItem) = viewModelScope.launch {
        inventoryRepository.getTotalNumberOfItems(period).onEach { response->
            when(response){
                is Resource.Success ->{
                    _totalNumberOfItems.value = totalNumberOfItems.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _totalNumberOfItems.value = totalNumberOfItems.value.copy(
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _totalNumberOfItems.value = totalNumberOfItems.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getTotalCostValue(period: PeriodDropDownItem) = viewModelScope.launch {
        inventoryRepository.getTotalCostValue(period).onEach { response->
            when(response){
                is Resource.Success ->{
                    _totalCostValue.value = totalCostValue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _totalCostValue.value = totalCostValue.value.copy(
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _totalCostValue.value = totalCostValue.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getTotalExpectedSalesValue(period: PeriodDropDownItem) = viewModelScope.launch {
        inventoryRepository.getTotalExpectedSalesValue(period).onEach { response->
            when(response){
                is Resource.Success ->{
                    _totalExpectedSalesValue.value = totalExpectedSalesValue.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _totalExpectedSalesValue.value = totalExpectedSalesValue.value.copy(
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _totalExpectedSalesValue.value = totalExpectedSalesValue.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getMostAvailableItem(period: PeriodDropDownItem) = viewModelScope.launch {
        inventoryRepository.getMostAvailableItem(period).onEach { response->
            when(response){
                is Resource.Success ->{
                    _mostAvailableItem.value = mostAvailableItem.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _mostAvailableItem.value = mostAvailableItem.value.copy(
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _mostAvailableItem.value = mostAvailableItem.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getLeastAvailableItem(period: PeriodDropDownItem) = viewModelScope.launch {
        inventoryRepository.getLeastAvailableItem(period).onEach { response->
            when(response){
                is Resource.Success ->{
                    _leastAvailableItem.value = leastAvailableItem.value.copy(
                        itemValue = response.data ?: ItemValue(emptyString, 0.0),
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _leastAvailableItem.value = leastAvailableItem.value.copy(
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _leastAvailableItem.value = leastAvailableItem.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }



    fun updateInventoryDate(date: Long, dayOfWeek: String) {
        inventoryInfo = inventoryInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun updateInventoryCostPrices(unitCostPrice: Double, totalCostPrice: Double) {
        inventoryInfo = inventoryInfo.copy(
            unitCostPrice = unitCostPrice,
            totalCostPrice = totalCostPrice
        )
    }
    fun updateInventoryReceiptId(id: String) {
        inventoryInfo = inventoryInfo.copy(
            receiptId = id
        )
    }
    fun updateInventoryQuantity(quantity: ItemQuantities) {
        inventoryInfo = inventoryInfo.copy(
            quantityInfo = quantity,
            totalNumberOfUnits = quantity.getTotalNumberOfUnits(),
            totalCostPrice = inventoryInfo.unitCostPrice.times(quantity.getTotalNumberOfUnits())
        )
    }
    fun updateInventoryOtherInfo(otherInfo: String) {
        inventoryInfo = inventoryInfo.copy(
            otherInfo = otherInfo
        )
    }

    fun addDate(date: Long, dayOfWeek: String) {
        addInventoryInfo = addInventoryInfo.copy(
            date = date,
            dayOfWeek = dayOfWeek
        )
    }
    fun addCostPrices(unitCostPrice: Double, totalCostPrice: Double) {
        addInventoryInfo = addInventoryInfo.copy(
            unitCostPrice = unitCostPrice,
            totalCostPrice = totalCostPrice
        )
    }
    fun addOtherInfo(otherInfo: String) {
        addInventoryInfo = addInventoryInfo.copy(
            otherInfo = otherInfo
        )
    }
    fun addUniqueInventoryItemId(id: String) {
        addInventoryInfo = addInventoryInfo.copy(
            uniqueInventoryItemId = id
        )
    }
    fun addInventoryQuantityValues(_inventoryQuantityDisplayValues: List<InventoryQuantityDisplayValues>) {
        inventoryQuantityDisplayValues = _inventoryQuantityDisplayValues
    }
    fun addItemQuantities(_itemQuantities: ItemQuantities) {
        addInventoryInfo = addInventoryInfo.copy(
            quantityInfo = _itemQuantities,
            totalNumberOfUnits = _itemQuantities.getTotalNumberOfUnits(),
            totalCostPrice = addInventoryInfo.unitCostPrice.times(_itemQuantities.getTotalNumberOfUnits())
        )
    }
    fun addReceiptId(receiptId: String) {
        addInventoryInfo = addInventoryInfo.copy(
            receiptId = receiptId
        )
    }
    fun clearInventory() {
        addInventoryInfo = InventoryEntity(0, emptyString, emptyString, emptyString, addInventoryInfo.date, addInventoryInfo.dayOfWeek,
            emptyList(), 0, 0.0, 0.0, emptyString, emptyString)
    }


}