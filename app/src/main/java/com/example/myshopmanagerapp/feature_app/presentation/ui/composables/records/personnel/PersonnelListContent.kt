package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.PersonnelEntities
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*


@Composable
fun PersonnelListContent(
    personnel: PersonnelEntities,
    isDeletingPersonnel: Boolean,
    personnelDeletionIsSuccessful: Boolean,
    personnelDeletingMessage: String?,
    reloadAllPersonnel: ()-> Unit,
    onConfirmDelete: (String)-> Unit,
    navigateToViewPersonnelScreen: (String)-> Unit
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val thisPersonnelJson = userPreferences.getPersonnelInfo.collectAsState(initial = emptyString).value
    val thisPersonnel = thisPersonnelJson.toPersonnelEntity()
    val isPrincipalAdmin = thisPersonnel?.isPrincipalAdmin == true
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var openAlertDialog by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var alertDialogMessage by remember {
        mutableStateOf(emptyString)
    }
    var uniquePersonnelId by remember {
        mutableStateOf(emptyString)
    }
    var personnelName by remember {
        mutableStateOf(emptyString)
    }

    val secondaryContentColor = if (isSystemInDarkTheme()) Grey70 else Grey40
    val contentColor = if (isSystemInDarkTheme()) Grey99 else Grey10


    if (personnel.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No personnel have been added yet!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    else {
        BasicScreenColumnWithoutBottomBar {
            HorizontalDivider()

            personnel.forEachIndexed { index, personnel ->
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    PersonnelCard(
                        personnel = personnel,
                        mainContentColor = contentColor,
                        secondaryContentColor = secondaryContentColor,
                        number = index.plus(1).toString(),
                        onDelete = {
                            if (isPrincipalAdmin) {
                                personnelName = "${personnel.firstName} ${personnel.lastName}"
                                uniquePersonnelId = personnel.uniquePersonnelId
                                openDeleteConfirmation = !openDeleteConfirmation
                            }else{
                                alertDialogMessage = "You do not have the administrative rights to delete this personnel"
                                openAlertDialog = !openAlertDialog
                            }
                       },
                    ) {
                        navigateToViewPersonnelScreen(personnel.uniquePersonnelId)
                    }
                }
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Personnel",
            textContent = "Are your sure you want to permanently remove $personnelName",
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniquePersonnelId)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }

        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = isDeletingPersonnel,
            title = null,
            textContent = personnelDeletingMessage.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            if (personnelDeletionIsSuccessful){
                reloadAllPersonnel()
            }
            confirmationInfoDialog = false
        }

        ConfirmationInfoDialog(
            openDialog = openAlertDialog,
            isLoading = false,
            title = null,
            textContent = alertDialogMessage,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openAlertDialog = false
        }

    }
    
    
}
