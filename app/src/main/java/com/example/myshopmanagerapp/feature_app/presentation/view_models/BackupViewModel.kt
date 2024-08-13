package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.BackupRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupRepository: BackupRepository
): ViewModel(){

    val userPreferences = UserPreferences(MyShopManagerApp.applicationContext())

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    var floatValue by mutableStateOf(0f)
        private set

    var repositoryMessage by mutableStateOf(emptyString)
        private set

    private val _absoluteRemoteBackupState = mutableStateOf(AddCompanyState())
    val absoluteRemoteBackupState: State<AddCompanyState> = _absoluteRemoteBackupState

    private val _smartRemoteBackupState = mutableStateOf(AddCompanyState())
    val smartRemoteBackupState: State<AddCompanyState> = _smartRemoteBackupState

    private val _absoluteSyncDataState = mutableStateOf(AddCompanyState())
    val absoluteSyncDataState: State<AddCompanyState> = _absoluteSyncDataState

    private val _smartSyncDataState = mutableStateOf(AddCompanyState())
    val smartSyncDataState: State<AddCompanyState> = _smartSyncDataState

    private val _backupDatabaseState = mutableStateOf(AddCompanyState())
    val backupDatabaseState: State<AddCompanyState> = _backupDatabaseState

    private val _restoreDatabaseState = mutableStateOf(AddCompanyState())
    val restoreDatabaseState: State<AddCompanyState> = _restoreDatabaseState

    fun absoluteRemoteBackup1() = viewModelScope.launch {
        backupRepository.absoluteBackup1(this)
    }

    fun getFloatValue() = viewModelScope.launch {
        userPreferences.getDoubleValue.collectLatest { value->
            floatValue = value.toNotNull().toFloat()
            Log.d("BackupRepository", "viewModel- getFloatValue() - floatValue = $floatValue")
        }
    }
    fun getRepositoryMessage() = viewModelScope.launch {
        userPreferences.getRepositoryJobMessage.collectLatest { value->
            repositoryMessage = value.toNotNull().ifBlank { "Unknown response" }
            Log.d("BackupRepository", "viewModel- getRepositoryMessage() - floatValue = $repositoryMessage")
        }
    }

    fun smartBackup1() = viewModelScope.launch {
        backupRepository.smartBackup1()
    }

    fun absoluteRemoteBackup() = viewModelScope.launch {
        backupRepository.absoluteBackup(this).onEach { response->
            when(response){
                is Resource.Success ->{
                    _absoluteRemoteBackupState.value = absoluteRemoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Success"))
                }
                is Resource.Loading ->{
                    _absoluteRemoteBackupState.value = absoluteRemoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = false,
                        message = response.message,
                        isLoading = true
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Backing up data"))
                }
                is Resource.Error ->{
                    _absoluteRemoteBackupState.value = absoluteRemoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = false,
                        message = response.message ?: "Unknown Error",
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }
    fun smartRemoteBackup() = viewModelScope.launch {
        backupRepository.smartBackup(this).onEach { response->
            when(response){
                is Resource.Success ->{
                    _smartRemoteBackupState.value = smartRemoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Success"))
                }
                is Resource.Loading ->{
                    _smartRemoteBackupState.value = smartRemoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = false,
                        message = response.message,
                        isLoading = true
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Backing up data"))
                }
                is Resource.Error ->{
                    _smartRemoteBackupState.value = smartRemoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = false,
                        message = response.message ?: "Unknown Error",
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun absoluteSyncData() = viewModelScope.launch {
        backupRepository.absoluteSyncCompanyInfo()
    }

    fun smartSyncData() = viewModelScope.launch {
        backupRepository.smartSyncCompanyInfo(this).onEach { response->
            when(response){
                is Resource.Success ->{
                    _smartSyncDataState.value = smartSyncDataState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Success"))
                }
                is Resource.Loading ->{
                    _smartSyncDataState.value = smartSyncDataState.value.copy(
                        data = response.data,
                        isSuccessful = false,
                        message = response.message,
                        isLoading = true
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Backing up data"))
                }
                is Resource.Error ->{
                    _smartSyncDataState.value = smartSyncDataState.value.copy(
                        data = response.data,
                        isSuccessful = false,
                        message = response.message ?: "Unknown Error",
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.message ?: "Unknown Error"))
                }
            }
        }.launchIn(this)
    }

    fun backupDatabase(context: Context) = viewModelScope.launch {
        backupRepository.backupDatabase(context).onEach { response->
            when(response){
                is Resource.Success ->{
                    _backupDatabaseState.value = backupDatabaseState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _backupDatabaseState.value = backupDatabaseState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _backupDatabaseState.value = backupDatabaseState.value.copy(
                        data = response.data,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    fun restoreDatabase(context: Context) = viewModelScope.launch {
        backupRepository.restoreDatabase(context).onEach { response->
            when(response){
                is Resource.Success ->{
                    _restoreDatabaseState.value = restoreDatabaseState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                }
                is Resource.Loading ->{
                    _restoreDatabaseState.value = restoreDatabaseState.value.copy(
                        data = response.data ,
                        isSuccessful = false,
                        message = response.message,
                        isLoading = true
                    )
                }
                is Resource.Error ->{
                    _restoreDatabaseState.value = restoreDatabaseState.value.copy(
                        data = response.data,
                        isSuccessful = false,
                        message = response.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }


}