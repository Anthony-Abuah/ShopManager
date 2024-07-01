package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.backup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.backup.BackupAndRestoreContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BackupViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun BackupAndRestoreScreen(
    backupViewModel: BackupViewModel =  hiltViewModel(),
    navigateBack: () -> Unit
) {

    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true ){
        backupViewModel.eventFlow.collectLatest { event->
            when(event){
                is UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Backup And Restore") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val dataRestore = backupViewModel.restoreDatabaseState.value
            val dataBackup = backupViewModel.backupDatabaseState.value
            val remoteBackup = backupViewModel.remoteBackupState.value
            val syncData = backupViewModel.syncDataState.value
            BackupAndRestoreContent(
                isBackingUpDatabase = dataBackup.isLoading,
                dataBackupConfirmationMessage = dataBackup.data.toNotNull(),
                isRestoringDatabase = dataRestore.isLoading,
                dataRestoreConfirmationMessage = dataRestore.data.toNotNull(),
                isBackingUpDatabaseRemotely = remoteBackup.isLoading,
                remoteBackupMessage = remoteBackup.data.toNotNull(),
                isSyncingData = syncData.isLoading,
                syncDataMessage = syncData.data.toNotNull(),
                restoreData = {
                    backupViewModel.restoreDatabase(context)
                },
                remoteBackup = {
                    backupViewModel.remoteBackup()
                },
                syncData = {
                    backupViewModel.syncData()
                }
            ) {
                backupViewModel.backupDatabase(context)
            }
        }
    }
}
