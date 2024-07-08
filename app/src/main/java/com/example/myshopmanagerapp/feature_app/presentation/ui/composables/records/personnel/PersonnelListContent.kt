package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.PersonnelEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


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
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var uniquePersonnelId by remember {
        mutableStateOf(emptyString)
    }
    var personnelName by remember {
        mutableStateOf(emptyString)
    }


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
            personnel.forEachIndexed { index, personnel ->
                if (index ==0){
                    HorizontalDivider()
                }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    PersonnelCard(
                        personnel = personnel,
                        number = index.plus(1).toString(),
                        onDelete = {
                            personnelName = "${personnel.firstName} ${personnel.lastName}"
                            uniquePersonnelId = personnel.uniquePersonnelId
                            openDeleteConfirmation = !openDeleteConfirmation
                       },
                    ) {
                        navigateToViewPersonnelScreen(personnel.uniquePersonnelId)
                    }
                }
                HorizontalDivider()
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
    }
    
    
}
