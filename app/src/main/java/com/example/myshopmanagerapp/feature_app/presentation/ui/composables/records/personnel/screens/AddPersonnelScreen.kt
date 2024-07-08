package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.AddPersonnelContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel


@Composable
fun AddPersonnelScreen(
    personnelViewModel: PersonnelViewModel,
    navigateToTakePhoto: () -> Unit,
    navigateBack: () -> Unit,
) {
    LaunchedEffect(Unit){
        personnelViewModel.getAllPersonnel()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Personnel") {
                navigateBack()
            }
        }
    ) {
        it
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val personnel = personnelViewModel.addPersonnelInfo
            AddPersonnelContent(
                personnel = personnel,
                isSavingPersonnel = personnelViewModel.addPersonnelState.value.isLoading,
                personnelSavingIsSuccessful = personnelViewModel.addPersonnelState.value.isSuccessful,
                personnelSavingMessage = personnelViewModel.addPersonnelState.value.message,
                addFirstName = {name-> personnelViewModel.addPersonnelFirstName(name)},
                addLastName = {lastName-> personnelViewModel.addPersonnelLastName(lastName)},
                addOtherNames = {name-> personnelViewModel.addPersonnelOtherName(name)},
                addContact = {contact-> personnelViewModel.addPersonnelContact(contact)},
                addRole = {role-> personnelViewModel.addPersonnelRole(role)},
                addAdminRights = {adminRights-> personnelViewModel.addPersonnelHasAdminRight(adminRights)},
                addOtherInfo = {otherInfo-> personnelViewModel.addPersonnelOtherInfo(otherInfo)},
                onTakePhoto = {navigateToTakePhoto()},
                addPersonnel = {_personnel-> personnelViewModel.addPersonnel(_personnel)}
            ) {
                navigateBack()
            }
        }

    }
}
