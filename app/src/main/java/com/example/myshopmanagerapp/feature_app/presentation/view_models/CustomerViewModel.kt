package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.CustomerEntities
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.CustomerRepository
import com.example.myshopmanagerapp.feature_app.domain.repository.GeneratePDFRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.customer.CustomerEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.customer.CustomerEntityState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val generatePDFRepository: GeneratePDFRepository
): ViewModel() {


    var customerInfo by mutableStateOf(CustomerEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, 0.0))
        private set

    var addCustomerInfo by mutableStateOf(CustomerEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, 0.0))
        private set

    private val _addCustomerState = mutableStateOf(ItemValueState())
    val addCustomerState: State<ItemValueState> = _addCustomerState

    private val _updateCustomerState = mutableStateOf(ItemValueState())
    val updateCustomerState: State<ItemValueState> = _updateCustomerState

    private val _deleteCustomerState = mutableStateOf(ItemValueState())
    val deleteCustomerState: State<ItemValueState> = _deleteCustomerState

    private val _customerEntityState = mutableStateOf(CustomerEntityState())
    val customerEntityState: State<CustomerEntityState> = _customerEntityState

    private val _generateCustomerListState = mutableStateOf(AddCompanyState())
    val generateCustomerListState: State<AddCompanyState> = _generateCustomerListState

    private val _customerEntitiesState = mutableStateOf(CustomerEntitiesState())
    val customerEntitiesState: State<CustomerEntitiesState> = _customerEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllCustomers() = viewModelScope.launch {
        customerRepository.getAllCustomers().onEach { response->
            when(response){
                is Resource.Success ->{
                    _customerEntitiesState.value = customerEntitiesState.value.copy(
                        customerEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _customerEntitiesState.value = customerEntitiesState.value.copy(
                        customerEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _customerEntitiesState.value = customerEntitiesState.value.copy(
                        customerEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getCustomer(uniqueCustomerId: String) = viewModelScope.launch {
        customerInfo = customerRepository.getCustomer(uniqueCustomerId) ?: CustomerEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, 0.0)
        _customerEntityState.value = customerEntityState.value.copy(
            customerEntity = customerRepository.getCustomer(uniqueCustomerId),
            isLoading = false
        )
    }

    fun addCustomer(customer: CustomerEntity) = viewModelScope.launch {
        customerRepository.addCustomer(customer).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addCustomerState.value = addCustomerState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _addCustomerState.value = addCustomerState.value.copy(
                        message = null,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _addCustomerState.value = addCustomerState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun updateCustomer(customer: CustomerEntity) = viewModelScope.launch {
        customerRepository.updateCustomer(customer).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateCustomerState.value = updateCustomerState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateCustomerState.value = updateCustomerState.value.copy(
                        message = null,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateCustomerState.value = updateCustomerState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteCustomer(uniqueCustomerId: String) = viewModelScope.launch {
        customerRepository.deleteCustomer(uniqueCustomerId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteCustomerState.value = deleteCustomerState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteCustomerState.value = deleteCustomerState.value.copy(
                        message = null,
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteCustomerState.value = deleteCustomerState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


    fun generateCustomerListPDF(context: Context, customers: CustomerEntities) = viewModelScope.launch {
        generatePDFRepository.generateCustomerList(context, customers).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateCustomerListState.value = generateCustomerListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateCustomerListState.value = generateCustomerListState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateCustomerListState.value = generateCustomerListState.value.copy(
                        data = response.message.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }



    fun updateCustomerName(name: String) {
        customerInfo = customerInfo.copy(
            customerName = name
        )
    }
    fun updateCustomerContact(contact: String) {
        customerInfo = customerInfo.copy(
            customerContact = contact
        )
    }
    fun updateCustomerLocation(location: String) {
        customerInfo = customerInfo.copy(
            customerLocation = location
        )
    }
    fun updateCustomerOtherInfo(info: String) {
        customerInfo = customerInfo.copy(
            otherInfo = info
        )
    }

    fun addCustomerName(name: String) {
        addCustomerInfo = addCustomerInfo.copy(
            customerName = name
        )
    }
    fun addCustomerContact(contact: String) {
        addCustomerInfo = addCustomerInfo.copy(
            customerContact = contact
        )
    }
    fun addCustomerLocation(location: String) {
        addCustomerInfo = addCustomerInfo.copy(
            customerLocation = location
        )
    }
    fun addAnyOtherInfo(info: String) {
        addCustomerInfo = addCustomerInfo.copy(
            otherInfo = info
        )
    }
    fun addCustomerPhoto(photo: String) {
        addCustomerInfo = addCustomerInfo.copy(
            customerPhoto = photo
        )
    }


}