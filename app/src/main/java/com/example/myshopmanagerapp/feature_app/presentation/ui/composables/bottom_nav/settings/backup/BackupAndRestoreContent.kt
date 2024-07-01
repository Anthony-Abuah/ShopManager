package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.backup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.SettingsContentCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun BackupAndRestoreContent(
    isBackingUpDatabase: Boolean,
    dataBackupConfirmationMessage: String,
    isRestoringDatabase: Boolean,
    dataRestoreConfirmationMessage: String,
    isBackingUpDatabaseRemotely: Boolean,
    remoteBackupMessage: String,
    isSyncingData: Boolean,
    syncDataMessage: String,
    restoreData: ()-> Unit,
    remoteBackup: ()-> Unit,
    syncData: ()-> Unit,
    backupData: ()-> Unit,
) {
    var confirmBackup by remember {
        mutableStateOf(false)
    }
    var confirmBackupInfo by remember {
        mutableStateOf(false)
    }
    var confirmRestoreInfo by remember {
        mutableStateOf(false)
    }
    var confirmRemoteBackupInfo by remember {
        mutableStateOf(false)
    }
    var confirmSyncDataInfo by remember {
        mutableStateOf(false)
    }
    var confirmRestore by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { confirmBackup = !confirmBackup },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_local_backup,
                title = "Local backup",
                info = "Click to save database to file"
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { confirmRestore = !confirmRestore },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_restore,
                title = "Restore data from local backup",
                info = "Click to restore database from file"
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                remoteBackup()
                confirmRemoteBackupInfo = !confirmRemoteBackupInfo
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_backup,
                title = "Remote backup",
                info = "Click to store data on our servers"
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                syncData()
                confirmSyncDataInfo = !confirmSyncDataInfo
            },
            contentAlignment = Alignment.Center
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_sync,
                title = "Sync data",
                info = "Click to sync remote data with local data"
            )
        }
    }


    DeleteConfirmationDialog(
        openDialog = confirmBackup,
        title = "Confirm Backup",
        textContent = "Are you sure you want to back up your data?",
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            backupData()
            confirmBackupInfo = !confirmBackupInfo
        }
    ) {
        confirmBackup = false
    }

    DeleteConfirmationDialog(
        openDialog = confirmRestore,
        title = "Restore Data",
        textContent = "Are you sure you want to restore data from backed up file?" +
                "\nNB: This action can't be undone. Ensure your data is backed up",
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            restoreData()
            confirmRestoreInfo = !confirmRestoreInfo
        }
    ) {
        confirmRestore = false
    }

    ConfirmationInfoDialog(
        openDialog = confirmBackupInfo,
        isLoading = isBackingUpDatabase ,
        title = emptyString,
        textContent = dataBackupConfirmationMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        confirmBackupInfo = false
    }
    ConfirmationInfoDialog(
        openDialog = confirmRestoreInfo,
        isLoading = isRestoringDatabase ,
        title = emptyString,
        textContent = dataRestoreConfirmationMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        confirmRestoreInfo = false
    }
    ConfirmationInfoDialog(
        openDialog = confirmRemoteBackupInfo,
        isLoading = isBackingUpDatabaseRemotely ,
        title = emptyString,
        textContent = remoteBackupMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        confirmRemoteBackupInfo = false
    }
    ConfirmationInfoDialog(
        openDialog = confirmSyncDataInfo,
        isLoading = isSyncingData ,
        title = emptyString,
        textContent = syncDataMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        confirmSyncDataInfo = false
    }

}



