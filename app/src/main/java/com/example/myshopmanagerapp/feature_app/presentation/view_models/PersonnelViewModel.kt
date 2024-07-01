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
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.PersonnelRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.personnel.PersonnelEntitiesState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.personnel.PersonnelEntityState
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonnelViewModel @Inject constructor(
    private val personnelRepository: PersonnelRepository
): ViewModel() {

    var personnelInfo by mutableStateOf(PersonnelEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString,
        emptyString, "1234", emptyString, emptyString, emptyString, false))
        private set

    var addPersonnelInfo by mutableStateOf(
        PersonnelEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString,
            emptyString, "1234", emptyString, emptyString, emptyString, false))
        private set

    private val _addPersonnelState = mutableStateOf(ItemValueState())
    val addPersonnelState: State<ItemValueState> = _addPersonnelState

    private val _updatePersonnelState = mutableStateOf(ItemValueState())
    val updatePersonnelState: State<ItemValueState> = _updatePersonnelState

    private val _deletePersonnelState = mutableStateOf(ItemValueState())
    val deletePersonnelState: State<ItemValueState> = _deletePersonnelState

    private val _personnelEntityState = mutableStateOf(PersonnelEntityState())
    val personnelEntityState: State<PersonnelEntityState> = _personnelEntityState

    private val _personnelEntitiesState = mutableStateOf(PersonnelEntitiesState())
    val personnelEntitiesState: State<PersonnelEntitiesState> = _personnelEntitiesState


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllPersonnel() = viewModelScope.launch {
        personnelRepository.getAllPersonnel().onEach { response->
            when(response){
                is Resource.Success ->{
                    _personnelEntitiesState.value = personnelEntitiesState.value.copy(
                        personnelEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _personnelEntitiesState.value = personnelEntitiesState.value.copy(
                        personnelEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _personnelEntitiesState.value = personnelEntitiesState.value.copy(
                        personnelEntities = response.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun getPersonnel(uniquePersonnelId: String) = viewModelScope.launch {
        personnelInfo = personnelRepository.getPersonnel(uniquePersonnelId) ?: personnelInfo
        _personnelEntityState.value = personnelEntityState.value.copy(
            personnelEntity = personnelRepository.getPersonnel(uniquePersonnelId),
            isLoading = false
        )
    }

    fun addPersonnel(personnel: PersonnelEntity) = viewModelScope.launch {
        personnelRepository.addPersonnel(personnel).onEach { response->
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

    fun updatePersonnel(personnel: PersonnelEntity) = viewModelScope.launch {
        personnelRepository.updatePersonnel(personnel).onEach { response->
            when(response){
                is Resource.Success ->{
                    _updatePersonnelState.value = updatePersonnelState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false,
                    )
                }
                is Resource.Loading ->{
                    _updatePersonnelState.value = updatePersonnelState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true,
                    )
                }
                is Resource.Error ->{
                    _updatePersonnelState.value = updatePersonnelState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false,
                    )
                }
            }
        }.launchIn(this)
    }

    fun deletePersonnel(uniquePersonnelId: String) = viewModelScope.launch {
        personnelRepository.deletePersonnel(uniquePersonnelId).onEach { response->
            when(response){
                is Resource.Success ->{
                    _deletePersonnelState.value = deletePersonnelState.value.copy(
                        message = response.data,
                        isSuccessful = true,
                        isLoading = false,
                    )
                }
                is Resource.Loading ->{
                    _deletePersonnelState.value = deletePersonnelState.value.copy(
                        message = response.data,
                        isSuccessful = false,
                        isLoading = true,
                    )
                }
                is Resource.Error ->{
                    _deletePersonnelState.value = deletePersonnelState.value.copy(
                        message = response.message,
                        isSuccessful = false,
                        isLoading = false,
                    )
                }
            }
        }.launchIn(this)
    }

    fun updatePersonnelFirstName(name: String) {
        updatePersonnel(personnelInfo.copy(firstName = name))
    }
    fun updatePersonnelLastName(name: String) {
        updatePersonnel(personnelInfo.copy(lastName = name))
    }
    fun updatePersonnelOtherName(name: String?) {
        updatePersonnel(personnelInfo.copy(otherNames = name))
    }
    fun updatePersonnelUserName(name: String) {
        updatePersonnel(personnelInfo.copy(userName = name))
    }
    fun updatePersonnelContact(contact: String) {
        updatePersonnel(personnelInfo.copy(contact = contact))
    }
    fun updatePersonnelPhoto(photo: String) {
        updatePersonnel(personnelInfo.copy(personnelPhoto = photo))
    }
    fun updatePersonnelOtherInfo(info: String?) {
        updatePersonnel(personnelInfo.copy(otherInfo = info))
    }
    fun updatePersonnelRole(role: String?) {
        updatePersonnel(personnelInfo.copy(role = role))
    }
    fun updatePersonnelAdminRights(rights: Boolean?) {
        updatePersonnel(personnelInfo.copy(hasAdminRights = rights))
    }
    fun updatePersonnelIsActive(isActive: Boolean?) {
        updatePersonnel(personnelInfo.copy(isActive = isActive))
    }


    fun addUniquePersonnelId(id: String) {
        addPersonnelInfo = addPersonnelInfo.copy(
            uniquePersonnelId = id
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
    fun addPersonnelHasAdminRight(rights: Boolean?) {
        addPersonnelInfo = addPersonnelInfo.copy(
            hasAdminRights = rights
        )
    }

}