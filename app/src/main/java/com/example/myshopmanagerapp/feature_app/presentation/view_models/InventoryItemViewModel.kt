package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.shortDateFormatter
import com.example.myshopmanagerapp.core.QuantityCategorizations
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.repository.InventoryItemRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.PeriodicInventoryItemState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item.InventoryItemEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item.InventoryItemEntityState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item.InventoryItemState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.inventory_item.ItemValuesState
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
class InventoryItemViewModel @Inject constructor(
    private val inventoryItemRepository: InventoryItemRepository
): ViewModel() {


    val dateString: String = LocalDate.now().format(shortDateFormatter)
    val dayOfWeek = LocalDate.now().dayOfWeek.toString().lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    var inventoryItemInfo by mutableStateOf(InventoryItemEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyList(), null, emptyList(), null, null, null, null, null, emptyString))
        private set

    var maximumInventoryItem by mutableStateOf<InventoryItemEntity?>(InventoryItemEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyList(), null, emptyList(), null, null, null, null, null, emptyString))
        private set

    var minimumInventoryItem by mutableStateOf<InventoryItemEntity?>(InventoryItemEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyList(), null, emptyList(), null, null, null, null, null, emptyString))
        private set

    var addInventoryItemInfo by mutableStateOf(InventoryItemEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyList(), null, emptyList(), null, null, null, null, null, emptyString))
        private set

    private val _itemProfitValues = mutableStateOf(ItemValuesState())
    val itemProfitValues: State<ItemValuesState> = _itemProfitValues

    private val _itemProfitPercentageValues = mutableStateOf(ItemValuesState())
    val itemProfitPercentageValues: State<ItemValuesState> = _itemProfitPercentageValues

    private val _itemCostValues = mutableStateOf(ItemValuesState())
    val itemCostValues: State<ItemValuesState> = _itemCostValues

    private val _itemSalesValues = mutableStateOf(ItemValuesState())
    val itemSalesValues: State<ItemValuesState> = _itemSalesValues

    private val _addInventoryItemState = mutableStateOf(InventoryItemState())
    val addInventoryItemState: State<InventoryItemState> = _addInventoryItemState

    private val _updateInventoryItemState = mutableStateOf(InventoryItemState())
    val updateInventoryItemState: State<InventoryItemState> = _updateInventoryItemState

    private val _updateSellingPricesInventoryItemState = mutableStateOf(InventoryItemState())
    val updateSellingPricesInventoryItemState: State<InventoryItemState> = _updateSellingPricesInventoryItemState

    private val _deleteInventoryItemState = mutableStateOf(InventoryItemState())
    val deleteInventoryItemState: State<InventoryItemState> = _deleteInventoryItemState


    private val _inventoryItemEntityState = mutableStateOf(InventoryItemEntityState())
    val inventoryItemEntityState: State<InventoryItemEntityState> = _inventoryItemEntityState

    private val _inventoryItemEntitiesState = mutableStateOf(InventoryItemEntitiesState())
    val inventoryItemEntitiesState: State<InventoryItemEntitiesState> = _inventoryItemEntitiesState

    private val _periodicInventoryItems = mutableStateOf(PeriodicInventoryItemState())
    val periodicInventoryItems: State<PeriodicInventoryItemState> = _periodicInventoryItems


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllInventoryItems() = viewModelScope.launch {
        inventoryItemRepository.getAllInventoryItems().onEach { response->
            when(response){
                is Resource.Success ->{
                    _inventoryItemEntitiesState.value = inventoryItemEntitiesState.value.copy(
                        inventoryItemEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _inventoryItemEntitiesState.value = inventoryItemEntitiesState.value.copy(
                        inventoryItemEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _inventoryItemEntitiesState.value = inventoryItemEntitiesState.value.copy(
                        inventoryItemEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getInventoryItem(uniqueInventoryItemId: String) = viewModelScope.launch {
        inventoryItemInfo = inventoryItemRepository.getInventoryItem(uniqueInventoryItemId) ?: InventoryItemEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyList(), null, emptyList(), null, null, null, null, null, emptyString)
        _inventoryItemEntityState.value = inventoryItemEntityState.value.copy(
            inventoryItemEntity = inventoryItemRepository.getInventoryItem(uniqueInventoryItemId),
            isLoading = false
        )
    }


    fun getPeriodicInventoryItems(periodDropDownItem: PeriodDropDownItem) = viewModelScope.launch {
        inventoryItemRepository.getPeriodicInventoryItems(periodDropDownItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _periodicInventoryItems.value = periodicInventoryItems.value.copy(
                        data = response.data ?: emptyMap(),
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _periodicInventoryItems.value = periodicInventoryItems.value.copy(
                        data = response.data ?: emptyMap(),
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _periodicInventoryItems.value = periodicInventoryItems.value.copy(
                        data = response.data ?: emptyMap(),
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getShopItemProfitPercentageValues() = viewModelScope.launch {
        inventoryItemRepository.getShopItemProfitPercentageValues().onEach { response->
            when(response){
                is Resource.Success ->{
                    _itemProfitPercentageValues.value = itemProfitPercentageValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = true,
                        itemValues = response.data ?: emptyList()
                    )
                }
                is Resource.Loading ->{
                    _itemProfitPercentageValues.value = itemProfitPercentageValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                        itemValues = response.data ?: emptyList()
                    )
                }
                is Resource.Error ->{
                    _itemProfitPercentageValues.value = itemProfitPercentageValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                        itemValues = response.data ?: emptyList()
                    )
                }
            }
        }.launchIn(this)
    }

    fun getShopItemProfitValues() = viewModelScope.launch {
        inventoryItemRepository.getShopItemProfitValues().onEach { response->
            when(response){
                is Resource.Success ->{
                    _itemProfitValues.value = itemProfitValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = true,
                        itemValues = response.data ?: emptyList()
                    )
                }
                is Resource.Loading ->{
                    _itemProfitValues.value = itemProfitValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                        itemValues = response.data ?: emptyList()
                    )
                }
                is Resource.Error ->{
                    _itemProfitValues.value = itemProfitValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                        itemValues = response.data ?: emptyList()
                    )
                }
            }
        }.launchIn(this)
    }

    fun getShopItemSalesValues() = viewModelScope.launch {
        inventoryItemRepository.getShopItemSellingValues().onEach { response->
            when(response){
                is Resource.Success ->{
                    _itemSalesValues.value = itemSalesValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = true,
                        itemValues = response.data ?: emptyList()
                    )
                }
                is Resource.Loading ->{
                    _itemSalesValues.value = itemSalesValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                        itemValues = response.data ?: emptyList()
                    )
                }
                is Resource.Error ->{
                    _itemSalesValues.value = itemSalesValues.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false,
                        itemValues = response.data ?: emptyList()
                    )
                }
            }
        }.launchIn(this)
    }

    fun getMaximumInventoryItem() = viewModelScope.launch {
        inventoryItemRepository.getMaximumInventoryItem().onEach { response->
            when(response){
                is Resource.Success ->{
                    maximumInventoryItem = response.data
                }
                is Resource.Loading ->{

                }
                is Resource.Error ->{

                }
            }
        }.launchIn(this)
    }

    fun getMinimumInventoryItem() = viewModelScope.launch {
        inventoryItemRepository.getMinimumInventoryItem().onEach { response->
            when(response){
                is Resource.Success ->{
                    minimumInventoryItem = response.data
                }
                is Resource.Loading ->{

                }
                is Resource.Error ->{

                }
            }
        }.launchIn(this)
    }

    fun addInventoryItem(inventoryItem: InventoryItemEntity) = viewModelScope.launch {
        inventoryItemRepository.addInventoryItem(inventoryItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addInventoryItemState.value = addInventoryItemState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _addInventoryItemState.value = addInventoryItemState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _addInventoryItemState.value = addInventoryItemState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateInventoryItem(inventoryItem: InventoryItemEntity) = viewModelScope.launch {
        inventoryItemRepository.updateInventoryItem(inventoryItem).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateInventoryItemState.value = updateInventoryItemState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _updateInventoryItemState.value = updateInventoryItemState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _updateInventoryItemState.value = updateInventoryItemState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteInventoryItem(uniqueInventoryItemId: String) = viewModelScope.launch {
        inventoryItemRepository.deleteInventoryItem(uniqueInventoryItemId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteInventoryItemState.value = deleteInventoryItemState.value.copy(
                        message = response.data,
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                is Resource.Loading ->{
                    _deleteInventoryItemState.value = deleteInventoryItemState.value.copy(
                        message = response.data,
                        isLoading = true,
                        isSuccessful = false
                    )
                }
                is Resource.Error ->{
                    _deleteInventoryItemState.value = deleteInventoryItemState.value.copy(
                        message = response.message,
                        isLoading = false,
                        isSuccessful = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateItemName(name: String) {
        inventoryItemInfo = inventoryItemInfo.copy(
            inventoryItemName = name
        )
    }
    fun updateQuantityCategorizations(quantityCategorizations: QuantityCategorizations) {
        Log.d("InventoryItemViewModel", "updateQuantityCategorizations = $quantityCategorizations")
        inventoryItemInfo = inventoryItemInfo.copy(
            quantityCategorizations = quantityCategorizations
        )
    }
    fun updateManufacturerName(name: String) {
        inventoryItemInfo = inventoryItemInfo.copy(
            manufacturerName = name
        )
    }
    fun updateItemCategory(category: String) {
        inventoryItemInfo = inventoryItemInfo.copy(
            itemCategory = category
        )
    }
    fun updateOtherInfo(info: String) {
        inventoryItemInfo = inventoryItemInfo.copy(
            otherInfo = info
        )
    }

    fun addItemName(name: String) {
        addInventoryItemInfo = addInventoryItemInfo.copy(
            inventoryItemName = name
        )
    }
    fun addManufacturerName(name: String) {
        addInventoryItemInfo = addInventoryItemInfo.copy(
            manufacturerName = name
        )
    }
    fun addSellingPrice(price: String) {
        addInventoryItemInfo = addInventoryItemInfo.copy(
            currentSellingPrice = convertToDouble(price)
        )
    }
    fun addItemCategory(category: String) {
        addInventoryItemInfo = addInventoryItemInfo.copy(
            itemCategory = category
        )
    }
    fun addQuantityCategorizations(quantityCategorizations: QuantityCategorizations) {
        Log.d("InventoryItemViewModel", "addQuantityCategorizations = $quantityCategorizations")
        addInventoryItemInfo = addInventoryItemInfo.copy(
            quantityCategorizations = quantityCategorizations
        )
    }
    fun addOtherInfo(info: String) {
        addInventoryItemInfo = addInventoryItemInfo.copy(
            otherInfo = info
        )
    }

}