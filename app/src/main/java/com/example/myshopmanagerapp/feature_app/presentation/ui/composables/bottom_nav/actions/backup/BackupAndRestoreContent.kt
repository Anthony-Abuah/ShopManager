package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.backup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AbsoluteBackUpDialogMessage
import com.example.myshopmanagerapp.core.FormRelatedString.AbsoluteRemoteBackUp
import com.example.myshopmanagerapp.core.FormRelatedString.AbsoluteSync
import com.example.myshopmanagerapp.core.FormRelatedString.AbsoluteSyncDialogMessage
import com.example.myshopmanagerapp.core.FormRelatedString.ClickToBackupDataRemotely
import com.example.myshopmanagerapp.core.FormRelatedString.ClickToRestoreDatabaseToFile
import com.example.myshopmanagerapp.core.FormRelatedString.ClickToSaveDatabaseToFile
import com.example.myshopmanagerapp.core.FormRelatedString.ClickToSyncData
import com.example.myshopmanagerapp.core.FormRelatedString.ConfirmBackup
import com.example.myshopmanagerapp.core.FormRelatedString.ConfirmRestore
import com.example.myshopmanagerapp.core.FormRelatedString.ConfirmSync
import com.example.myshopmanagerapp.core.FormRelatedString.LocalBackUp
import com.example.myshopmanagerapp.core.FormRelatedString.LocalBackUpDialogMessage
import com.example.myshopmanagerapp.core.FormRelatedString.RestoreBackedUpDataDialogMessage
import com.example.myshopmanagerapp.core.FormRelatedString.RestoreData
import com.example.myshopmanagerapp.core.FormRelatedString.SmartBackUpDialogMessage
import com.example.myshopmanagerapp.core.FormRelatedString.SmartRemoteBackUp
import com.example.myshopmanagerapp.core.FormRelatedString.SmartSync
import com.example.myshopmanagerapp.core.FormRelatedString.SmartSyncDialogMessage
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.SettingsContentCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch


@Composable
fun BackupAndRestoreContent(
    isBackingUpDatabase: Boolean,
    dataBackupConfirmationMessage: String,
    isRestoringDatabase: Boolean,
    dataRestoreConfirmationMessage: String,
    localBackupData: ()-> Unit,
    localRestoreData: ()-> Unit,
    absoluteRemoteBackup: ()-> Unit,
    smartRemoteBackup: ()-> Unit,
    absoluteSyncData: ()-> Unit,
    smartSyncData: ()-> Unit,
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val coroutineScope = rememberCoroutineScope()

    var openLocalConfirmationDialog by remember {
        mutableStateOf(false)
    }
    var dialogTitle by remember {
        mutableStateOf(emptyString)
    }
    var dialogMessage by remember {
        mutableStateOf(emptyString)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openRemoteBackupConfirmationDialog by remember {
        mutableStateOf(false)
    }
    var openSyncDataConfirmationDialog by remember {
        mutableStateOf(false)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isLocalBackup by remember {
        mutableStateOf<Boolean?>(null)
    }
    var isAbsoluteBackup by remember {
        mutableStateOf<Boolean?>(null)
    }
    var isAbsoluteSync by remember {
        mutableStateOf<Boolean?>(null)
    }
    val repositoryJobMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = emptyString).value

    BasicScreenColumnWithoutBottomBar {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                isLocalBackup = true
                dialogTitle = ConfirmBackup
                dialogMessage = LocalBackUpDialogMessage
                openLocalConfirmationDialog = !openLocalConfirmationDialog
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_local_backup,
                title = LocalBackUp,
                info = ClickToSaveDatabaseToFile
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                isLocalBackup = false
                dialogTitle = ConfirmRestore
                dialogMessage = RestoreBackedUpDataDialogMessage
                openLocalConfirmationDialog = !openLocalConfirmationDialog
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_restore,
                title = RestoreData,
                info = ClickToRestoreDatabaseToFile
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                isAbsoluteBackup = true
                dialogTitle = ConfirmBackup
                dialogMessage = AbsoluteBackUpDialogMessage
                openRemoteBackupConfirmationDialog = !openRemoteBackupConfirmationDialog
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_backup,
                title = AbsoluteRemoteBackUp,
                info = ClickToBackupDataRemotely
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                isAbsoluteBackup = false
                dialogTitle = ConfirmBackup
                dialogMessage = SmartBackUpDialogMessage
                openRemoteBackupConfirmationDialog = !openRemoteBackupConfirmationDialog
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_backup,
                title = SmartRemoteBackUp,
                info = ClickToBackupDataRemotely
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                isAbsoluteSync = true
                dialogTitle = ConfirmSync
                dialogMessage = AbsoluteSyncDialogMessage
                openSyncDataConfirmationDialog = !openSyncDataConfirmationDialog
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_sync,
                title = AbsoluteSync,
                info = ClickToSyncData
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                isAbsoluteSync = false
                dialogTitle = ConfirmSync
                dialogMessage = SmartSyncDialogMessage
                openSyncDataConfirmationDialog = !openSyncDataConfirmationDialog
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_sync,
                title = SmartSync,
                info = ClickToSyncData
            )
        }

    }


    DeleteConfirmationDialog(
        openDialog = openLocalConfirmationDialog,
        title = dialogTitle,
        textContent = dialogMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            when(isLocalBackup){
                true->{
                    localBackupData()
                    isLoading = isBackingUpDatabase
                    dialogMessage = dataBackupConfirmationMessage
                    confirmationInfoDialog = !confirmationInfoDialog
                }
                false->{
                    localRestoreData()
                    isLoading = isRestoringDatabase
                    dialogMessage = dataRestoreConfirmationMessage
                    confirmationInfoDialog = !confirmationInfoDialog
                }
                null->{ openLocalConfirmationDialog = false }
            }
        }
    ) {
        openLocalConfirmationDialog = false
    }

    DeleteConfirmationDialog(
        openDialog = openRemoteBackupConfirmationDialog,
        title = dialogTitle,
        textContent = dialogMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            coroutineScope.launch {
                userPreferences.saveRepositoryJobMessage(emptyString)
            }
            when(isAbsoluteBackup){
                true->{
                    absoluteRemoteBackup()
                    isLoading = repositoryJobMessage.isNullOrBlank()
                    dialogMessage = repositoryJobMessage ?: "Unknown outcome"
                    confirmationInfoDialog = !confirmationInfoDialog
                }
                false->{
                    smartRemoteBackup()
                    isLoading = repositoryJobMessage.isNullOrBlank()
                    dialogMessage = repositoryJobMessage ?: "Unknown outcome"
                    confirmationInfoDialog = !confirmationInfoDialog
                }
                null->{ openRemoteBackupConfirmationDialog = false }
            }
        }
    ) {
        openRemoteBackupConfirmationDialog = false
    }

    DeleteConfirmationDialog(
        openDialog = openSyncDataConfirmationDialog,
        title = dialogTitle,
        textContent = dialogMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            when(isAbsoluteSync){
                true->{
                    absoluteSyncData()
                    isLoading = repositoryJobMessage.isNullOrBlank()
                    dialogMessage = repositoryJobMessage ?: "Unknown outcome"
                    confirmationInfoDialog = !confirmationInfoDialog
                }
                false->{
                    smartSyncData()
                    isLoading = repositoryJobMessage.isNullOrBlank()
                    dialogMessage = repositoryJobMessage ?: "Unknown outcome"
                    confirmationInfoDialog = !confirmationInfoDialog
                }
                null->{ openSyncDataConfirmationDialog = false }
            }
        }
    ) {
        openSyncDataConfirmationDialog = false
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isLoading,
        title = emptyString,
        textContent = dialogMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        confirmationInfoDialog = false
    }

}



