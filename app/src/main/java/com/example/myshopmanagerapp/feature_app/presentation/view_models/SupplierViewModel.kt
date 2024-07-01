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
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.SupplierRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.supplier.SupplierEntitiesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
): ViewModel() {

    var supplierInfo by mutableStateOf(SupplierEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString))
        private set

    var addSupplierInfo by mutableStateOf(SupplierEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString))
        private set

    private val _supplierEntitiesState = mutableStateOf(SupplierEntitiesState())
    val supplierEntitiesState: State<SupplierEntitiesState> = _supplierEntitiesState

    private val _addSupplierState = mutableStateOf(SupplierEntitiesState())
    val addSupplierState: State<SupplierEntitiesState> = _addSupplierState

    private val _updateSupplierState = mutableStateOf(SupplierEntitiesState())
    val updateSupplierState: State<SupplierEntitiesState> = _updateSupplierState

    private val _deleteSupplierState = mutableStateOf(SupplierEntitiesState())
    val deleteSupplierState: State<SupplierEntitiesState> = _deleteSupplierState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllSuppliers() = viewModelScope.launch {
        supplierRepository.getAllSuppliers().onEach { response->
            when(response){
                is Resource.Success ->{
                    _supplierEntitiesState.value = supplierEntitiesState.value.copy(
                        supplierEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _supplierEntitiesState.value = supplierEntitiesState.value.copy(
                        supplierEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _supplierEntitiesState.value = supplierEntitiesState.value.copy(
                        supplierEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun deleteSupplier(uniqueSupplierId: String) = viewModelScope.launch {
        supplierRepository.deleteSupplier(uniqueSupplierId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteSupplierState.value = deleteSupplierState.value.copy(
                        isSuccessful = true,
                        message = response.data,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteSupplierState.value = deleteSupplierState.value.copy(
                        isSuccessful = false,
                        message = response.data,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteSupplierState.value = deleteSupplierState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun getSupplier(uniqueSupplierId: String) = viewModelScope.launch {
        supplierInfo = supplierRepository.getSupplier(uniqueSupplierId) ?: SupplierEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString)
    }

    fun addSupplier(supplier: SupplierEntity) = viewModelScope.launch {
        supplierRepository.addSupplier(supplier).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addSupplierState.value = addSupplierState.value.copy(
                        isSuccessful = true,
                        message = response.data,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _addSupplierState.value = addSupplierState.value.copy(
                        isSuccessful = false,
                        message = response.data,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _addSupplierState.value = addSupplierState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateSupplier(supplier: SupplierEntity) = viewModelScope.launch {
        supplierRepository.updateSupplier(supplier).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateSupplierState.value = updateSupplierState.value.copy(
                        isSuccessful = true,
                        message = response.data,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateSupplierState.value = updateSupplierState.value.copy(
                        isSuccessful = false,
                        message = response.data,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateSupplierState.value = updateSupplierState.value.copy(
                        message = response.message ,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateSupplierName(name: String) {
        supplierInfo = supplierInfo.copy(
            supplierName = name
        )
    }
    fun updateSupplierContact(contact: String) {
        supplierInfo = supplierInfo.copy(
            supplierContact = contact
        )
    }
    fun updateSupplierLocation(location: String) {
        supplierInfo = supplierInfo.copy(
            supplierLocation = location
        )
    }
    fun updateSupplierOtherInfo(info: String) {
        supplierInfo = supplierInfo.copy(
            otherInfo = info
        )
    }
    fun updateSupplierRole(role: String) {
        supplierInfo = supplierInfo.copy(
            supplierRole = role
        )
    }


    fun addSupplierName(name: String) {
        addSupplierInfo = addSupplierInfo.copy(
            supplierName = name
        )
    }
    fun addSupplierContact(contact: String) {
        addSupplierInfo = addSupplierInfo.copy(
            supplierContact = contact
        )
    }
    fun addSupplierLocation(location: String) {
        addSupplierInfo = addSupplierInfo.copy(
            supplierLocation = location
        )
    }
    fun addSupplierOtherInfo(info: String) {
        addSupplierInfo = addSupplierInfo.copy(
            otherInfo = info
        )
    }
    fun addSupplierRole(role: String) {
        addSupplierInfo = addSupplierInfo.copy(
            supplierRole = role
        )
    }


}