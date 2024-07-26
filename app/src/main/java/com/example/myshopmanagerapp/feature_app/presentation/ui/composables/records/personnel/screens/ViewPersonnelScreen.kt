package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.ViewPersonnelContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel


@Composable
fun ViewPersonnelScreen(
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniquePersonnelId: String,
    navigateBack: () -> Unit,
) {
    LaunchedEffect(Unit){
        personnelViewModel.getPersonnel(uniquePersonnelId)
        personnelViewModel.getAllPersonnel()
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Personnel") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewPersonnelContent(
                personnel = personnelViewModel.personnelInfo,
                updateFirstName = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(firstName = it)) },
                updateContact = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(contact = it)) },
                updateLastName = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(lastName = it)) },
                updateOtherNames = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(otherNames = it)) },
                updateUsername = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(userName = it)) },
                updatePersonnelRole = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(role = it)) },
                updateOtherInfo = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(otherInfo = it)) },
                updatePersonnelHasAdminRights = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(hasAdminRights = it)) },
                updatePersonnelIsActive = { personnelViewModel.updatePersonnel(personnelViewModel.personnelInfo.copy(isActive = it)) },
                updateIsSuccessful = personnelViewModel.updatePersonnelState.value.isSuccessful,
                updatePersonnelMessage = personnelViewModel.updatePersonnelState.value.message.toNotNull(),
                isUpdatingPersonnel = personnelViewModel.updatePersonnelState.value.isLoading
            ) {
                personnelViewModel.getPersonnel(uniquePersonnelId)
            }
        }
    }
}
