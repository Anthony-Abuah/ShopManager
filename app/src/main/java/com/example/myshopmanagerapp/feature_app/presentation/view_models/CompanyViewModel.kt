package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntityJson
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.domain.model.ReceiptInfo
import com.example.myshopmanagerapp.feature_app.domain.repository.CompanyRepository
import com.example.myshopmanagerapp.feature_app.domain.repository.GeneratePDFRepository
import com.example.myshopmanagerapp.feature_app.domain.repository.PersonnelRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.CompanyEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.CompanyEntityState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.ReceiptEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val companyRepository: CompanyRepository,
    private val personnelRepository: PersonnelRepository,
    private val generatePDFRepository: GeneratePDFRepository
): ViewModel() {

    var receiptInfo by mutableStateOf(ReceiptEntity(0, emptyString, emptyString, emptyString, emptyString, Date().time, emptyString, emptyString, emptyString, emptyString, emptyList(), 0.0))
        private set

    var updateReceiptInfo by mutableStateOf(ReceiptEntity(0, emptyString, emptyString, emptyString, emptyString, Date().time, emptyString, emptyString, emptyString, emptyString, emptyList(), 0.0))
        private set

    var addPersonnelInfo by mutableStateOf(PersonnelEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString,"1234", emptyString, emptyString,emptyString, emptyString, false))
        private set


    var companyInfo by mutableStateOf<CompanyEntity?>(CompanyEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, Date(), emptyString, emptyString, 0, emptyString))
        private set

    var addCompanyInfo by mutableStateOf(CompanyEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, Date(), emptyString, emptyString, 0, emptyString))
        private set

    var passwordConfirmation by mutableStateOf(emptyString)
        private set


    private val _addPersonnelState = mutableStateOf(ItemValueState())
    val addPersonnelState: State<ItemValueState> = _addPersonnelState

    private val _companyEntityState = mutableStateOf(CompanyEntityState())
    val companyEntityState: State<CompanyEntityState> = _companyEntityState

    private val _addCompanyState = mutableStateOf(AddCompanyState())
    val addCompanyState: State<AddCompanyState> = _addCompanyState

    private val _addReceiptState = mutableStateOf(AddCompanyState())
    val addReceiptState: State<AddCompanyState> = _addReceiptState

    private val _updateReceiptState = mutableStateOf(AddCompanyState())
    val updateReceiptState: State<AddCompanyState> = _updateReceiptState

    private val _deleteReceiptState = mutableStateOf(AddCompanyState())
    val deleteReceiptState: State<AddCompanyState> = _deleteReceiptState

    private val _generateInvoiceState = mutableStateOf(AddCompanyState())
    val generateInvoiceState: State<AddCompanyState> = _generateInvoiceState

    private val _logoutState = mutableStateOf(AddCompanyState())
    val logoutState: State<AddCompanyState> = _logoutState

    private val _loginPersonnelState = mutableStateOf(AddCompanyState())
    val loginPersonnelState: State<AddCompanyState> = _loginPersonnelState

    private val _logoutPersonnelState = mutableStateOf(AddCompanyState())
    val logoutPersonnelState: State<AddCompanyState> = _logoutPersonnelState

    private val _loginState = mutableStateOf(AddCompanyState())
    val loginState: State<AddCompanyState> = _loginState

    private val _companyEntitiesState = mutableStateOf(CompanyEntitiesState())
    val companyEntitiesState: State<CompanyEntitiesState> = _companyEntitiesState

    private val _receiptEntitiesState = mutableStateOf(ReceiptEntitiesState())
    val receiptEntitiesState: State<ReceiptEntitiesState> = _receiptEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun registerPersonnel(personnel: PersonnelEntity) = viewModelScope.launch {
        personnelRepository.registerAndLogIn(personnel.copy(hasAdminRights = true)).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addPersonnelState.value = addPersonnelState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false,
                    )
                }
                is Resource.Loading ->{
                    _addPersonnelState.value = addPersonnelState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true,
                    )
                }
                is Resource.Error ->{
                    _addPersonnelState.value = addPersonnelState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false,
                    )
                }
            }
        }.launchIn(this)
    }


    fun getAllCompanies() = viewModelScope.launch {
        companyRepository.getAllCompanies().onEach { response->
            when(response){
                is Resource.Success ->{
                    _companyEntitiesState.value = companyEntitiesState.value.copy(
                        companyEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _companyEntitiesState.value = companyEntitiesState.value.copy(
                        companyEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _companyEntitiesState.value = companyEntitiesState.value.copy(
                        companyEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getAllReceipts() = viewModelScope.launch {
        generatePDFRepository.getAllReceipts().onEach { response->
            when(response){
                is Resource.Success ->{
                    _receiptEntitiesState.value = receiptEntitiesState.value.copy(
                        receiptEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _receiptEntitiesState.value = receiptEntitiesState.value.copy(
                        receiptEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _receiptEntitiesState.value = receiptEntitiesState.value.copy(
                        receiptEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getCompany(uniqueCompanyId: String) = viewModelScope.launch {
        companyInfo = companyRepository.getCompany(uniqueCompanyId)
        _companyEntityState.value = companyEntityState.value.copy(
            companyEntity = companyRepository.getCompany(uniqueCompanyId),
            isLoading = false
        )
    }

    fun getReceipt(uniqueReceiptId: String) = viewModelScope.launch {
        updateReceiptInfo = generatePDFRepository.getReceipt(uniqueReceiptId) ?: updateReceiptInfo
    }

    fun registerShopAccount(company: CompanyEntity, passwordConfirmation: String) = viewModelScope.launch {
        companyRepository.registerShopAccount(company, passwordConfirmation)
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmedPassword: String) = viewModelScope.launch {
        companyRepository.changePassword(currentPassword, newPassword, confirmedPassword)
    }

    fun updateReceipt() = viewModelScope.launch {
        generatePDFRepository.updateReceipt(updateReceiptInfo).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updateReceiptState.value = updateReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _updateReceiptState.value = updateReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _updateReceiptState.value = updateReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun generateReceipt() = viewModelScope.launch {
        generatePDFRepository.addReceipt(receiptInfo).onEach { response->
            when(response){
                is Resource.Success ->{
                    _addReceiptState.value = addReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _addReceiptState.value = addReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _addReceiptState.value = addReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun deleteReceipt(uniqueReceiptId: String) = viewModelScope.launch {
        generatePDFRepository.deleteReceipt(uniqueReceiptId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deleteReceiptState.value = deleteReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _deleteReceiptState.value = deleteReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _deleteReceiptState.value = deleteReceiptState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun generateReceipt(context: Context, receiptInfo: ReceiptInfo) = viewModelScope.launch {
        generatePDFRepository.generateReceipt(context, receiptInfo).onEach { response->
            when(response){
                is Resource.Success ->{
                    _generateInvoiceState.value = generateInvoiceState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _generateInvoiceState.value = generateInvoiceState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _generateInvoiceState.value = generateInvoiceState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun companyLogout() = viewModelScope.launch {
        companyRepository.companyLogout()
    }

    fun logout() = viewModelScope.launch {
        companyRepository.logout().onEach { response->
            when(response){
                is Resource.Success ->{
                    _logoutState.value = logoutState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _logoutState.value = logoutState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _logoutState.value = logoutState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        companyRepository.login(email, password).onEach { response->
            when(response){
                is Resource.Success ->{
                    _loginState.value = loginState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _loginState.value = loginState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _loginState.value = loginState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun loginPersonnel(username: String, password: String) = viewModelScope.launch {
        personnelRepository.loginPersonnel(username, password).onEach { response->
            when(response){
                is Resource.Success ->{
                    _loginPersonnelState.value = loginPersonnelState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _loginPersonnelState.value = loginPersonnelState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _loginPersonnelState.value = loginPersonnelState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun logoutPersonnel() = viewModelScope.launch {
        personnelRepository.logoutPersonnel().onEach { response->
            when(response){
                is Resource.Success ->{
                    _logoutPersonnelState.value = logoutPersonnelState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = true,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _logoutPersonnelState.value = logoutPersonnelState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.data.toNotNull(),
                        isSuccessful = false,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _logoutPersonnelState.value = logoutPersonnelState.value.copy(
                        data = response.data.toNotNull(),
                        message = response.message.toNotNull(),
                        isSuccessful = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun companyLogin(email: String, password: String) = viewModelScope.launch {
        companyRepository.companyLogin(email, password)
    }

    fun updateCompany(company: CompanyEntity) = viewModelScope.launch {
        companyRepository.updateCompany(company )
    }

    fun deleteCompany(uniqueCompanyId: String) = viewModelScope.launch {
        companyRepository.deleteCompany(uniqueCompanyId)
    }

    fun updateCompanyName(name: String) {
        companyInfo = companyInfo?.copy(
            companyName = name
        )
    }

    fun updateCompanyContact(contact: String) {
        companyInfo = companyInfo?.copy(
            companyContact = contact
        )
    }

    fun updateCompanyLocation(location: String) {
        companyInfo = companyInfo?.copy(
            companyLocation = location
        )
    }

    fun updateCompanyOtherInfo(info: String) {
        companyInfo = companyInfo?.copy(
            otherInfo = info
        )
    }

    fun updateCompanyItemsSold(itemsSold: String) {
        companyInfo = companyInfo?.copy(
            companyProductsAndServices = itemsSold
        )
    }

    fun updateCompanyOwners(owners: String) {
        companyInfo = companyInfo?.copy(
            companyOwners = owners
        )
    }


    fun addCompanyName(name: String) {
        addCompanyInfo = addCompanyInfo.copy(
            companyName = name
        )
    }
    fun addCompanyContact(contact: String) {
        addCompanyInfo = addCompanyInfo.copy(
            companyContact = contact
        )
    }
    fun addCompanyLocation(location: String) {
        addCompanyInfo = addCompanyInfo.copy(
            companyLocation = location
        )
    }

    fun addCompanyProductAndServices(products: String) {
        addCompanyInfo = addCompanyInfo.copy(
            otherInfo = products
        )
    }
    fun addPassword(password: String) {
        addCompanyInfo = addCompanyInfo.copy(
            password = password
        )
    }
    fun addPasswordConfirmation(password: String) {
        passwordConfirmation = password
    }
    fun addEmail(email: String) {
        addCompanyInfo = addCompanyInfo.copy(
            email = email
        )
    }
    fun addCompanyItemsSold(itemsSold: String) {
        addCompanyInfo = addCompanyInfo.copy(
            companyProductsAndServices = itemsSold
        )
    }
    fun addCompanyOwners(owners: String) {
        addCompanyInfo = addCompanyInfo.copy(
            companyOwners = owners
        )
    }
    fun addCompanyOtherInfo(info: String) {
        addCompanyInfo = addCompanyInfo.copy(
            otherInfo = info
        )
    }
    fun addReceiptShopInfo(name: String, contact: String, location: String) {
        receiptInfo = receiptInfo.copy(
            shopName = name
        )
        receiptInfo = receiptInfo.copy(
            shopLocation = location
        )
        receiptInfo = receiptInfo.copy(
            shopContact = contact
        )
    }

    fun addReceiptCustomer(name: String, contact: String) {
        receiptInfo = receiptInfo.copy(
            customerName = name
        )
        receiptInfo = receiptInfo.copy(
            customerContact = contact
        )
    }

    fun addReceiptDate(date: Long) {
        receiptInfo = receiptInfo.copy(
            date = date
        )
    }


    fun addReceiptItems(items: List<ItemQuantityInfo>) {
        receiptInfo = receiptInfo.copy(
            items = items
        )
    }


    fun addPersonnelFirstName(name: String) {
        addPersonnelInfo = addPersonnelInfo.copy(
            firstName = name
        )
    }
    fun addPersonnelLastName(name: String) {
        addPersonnelInfo = addPersonnelInfo.copy(
            lastName = name
        )
    }
    fun addPersonnelOtherName(name: String?) {
        addPersonnelInfo = addPersonnelInfo.copy(
            otherNames = name
        )
    }
    fun addPersonnelContact(contact: String) {
        addPersonnelInfo = addPersonnelInfo.copy(
            contact = contact
        )
    }
    fun addPersonnelPhoto(location: String?) {
        addPersonnelInfo = addPersonnelInfo.copy(
            personnelPhoto = location
        )
    }
    fun addPersonnelOtherInfo(info: String?) {
        addPersonnelInfo = addPersonnelInfo.copy(
            otherInfo = info
        )
    }
    fun addPersonnelRole(role: String?) {
        addPersonnelInfo = addPersonnelInfo.copy(
            role = role
        )
    }
    fun addPersonnelPassword(password: String) {
        addPersonnelInfo = addPersonnelInfo.copy(
            password = password
        )
    }
    fun addPersonnelUsername(username: String) {
        addPersonnelInfo = addPersonnelInfo.copy(
            userName = username
        )
    }
    fun addPersonnelHasAdminRight(rights: Boolean?) {
        addPersonnelInfo = addPersonnelInfo.copy(
            hasAdminRights = rights
        )
    }


    fun updateReceiptShopInfo(name: String, contact: String, location: String) {
        updateReceiptInfo = updateReceiptInfo.copy(
            shopName = name
        )
        updateReceiptInfo = updateReceiptInfo.copy(
            shopLocation = location
        )
        updateReceiptInfo = updateReceiptInfo.copy(
            shopContact = contact
        )
    }

    fun updateReceiptCustomer(name: String, contact: String) {
        updateReceiptInfo = updateReceiptInfo.copy(
            customerName = name
        )
        updateReceiptInfo = updateReceiptInfo.copy(
            customerContact = contact
        )
    }

    fun updateReceiptDate(date: Long) {
        updateReceiptInfo = updateReceiptInfo.copy(
            date = date
        )
    }


    fun updateReceiptItems(items: List<ItemQuantityInfo>) {
        updateReceiptInfo = updateReceiptInfo.copy(
            items = items
        )
    }



}