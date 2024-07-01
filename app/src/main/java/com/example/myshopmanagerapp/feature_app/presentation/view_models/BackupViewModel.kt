package com.example.myshopmanagerapp.feature_app.presentation.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.domain.repository.BackupRepository
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company.AddCompanyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupRepository: BackupRepository
): ViewModel(){


    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _remoteBackupState = mutableStateOf(AddCompanyState())
    val remoteBackupState: State<AddCompanyState> = _remoteBackupState

    private val _syncDataState = mutableStateOf(AddCompanyState())
    val syncDataState: State<AddCompanyState> = _syncDataState

    private val _backupDatabaseState = mutableStateOf(AddCompanyState())
    val backupDatabaseState: State<AddCompanyState> = _backupDatabaseState

    private val _restoreDatabaseState = mutableStateOf(AddCompanyState())
    val restoreDatabaseState: State<AddCompanyState> = _restoreDatabaseState

    fun remoteBackup() = viewModelScope.launch {
        backupRepository.backupCompanyInfo(this).onEach { response->
            when(response){
                is Resource.Success ->{
                    _remoteBackupState.value = remoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Success"))
                }
                is Resource.Loading ->{
                    _remoteBackupState.value = remoteBackupState.value.copy(
                        data = response.data ,
                        isSuccessful = false,
                        message = response.message,
                        isLoading = true
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Backing up data"))
                }
                is Resource.Error ->{
                    _remoteBackupState.value = remoteBackupState.value.copy(
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

    fun syncData() = viewModelScope.launch {
        backupRepository.syncCompanyInfo(this).onEach { response->
            when(response){
                is Resource.Success ->{
                    _syncDataState.value = syncDataState.value.copy(
                        data = response.data ,
                        isSuccessful = true,
                        message = response.message,
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Success"))
                }
                is Resource.Loading ->{
                    _syncDataState.value = syncDataState.value.copy(
                        data = response.data,
                        isSuccessful = false,
                        message = response.message,
                        isLoading = true
                    )
                    _eventFlow.emit(UIEvent.ShowSnackBar(response.data ?: "Backing up data"))
                }
                is Resource.Error ->{
                    _syncDataState.value = syncDataState.value.copy(
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