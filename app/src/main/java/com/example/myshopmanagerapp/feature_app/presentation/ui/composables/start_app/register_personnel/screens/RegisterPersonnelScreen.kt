package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_personnel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_personnel.RegisterPersonnelContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun RegisterPersonnelScreen(
    companyViewModel: CompanyViewModel,
    //navigateToTakePhoto: () -> Unit,
    navigateToBottomNav: () -> Unit,
    navigateBack: () -> Unit,
) {

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Personnel") {
                navigateBack()
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val personnel = companyViewModel.addPersonnelInfo
            RegisterPersonnelContent(
                personnel = personnel,
                isSavingPersonnel = companyViewModel.addPersonnelState.value.isLoading,
                personnelSavingIsSuccessful = companyViewModel.addPersonnelState.value.isSuccessful,
                personnelSavingMessage = companyViewModel.addPersonnelState.value.message,
                addFirstName = {name-> companyViewModel.addPersonnelFirstName(name)},
                addLastName = {lastName-> companyViewModel.addPersonnelLastName(lastName)},
                addOtherNames = {name-> companyViewModel.addPersonnelOtherName(name)},
                addContact = {contact-> companyViewModel.addPersonnelContact(contact)},
                addRole = {role-> companyViewModel.addPersonnelRole(role)},
                addPassword = {_password-> companyViewModel.addPersonnelPassword(_password)},
                addUserName = {_username-> companyViewModel.addPersonnelUsername(_username)},
                addAdminRights = {adminRights-> companyViewModel.addPersonnelHasAdminRight(adminRights)},
                addOtherInfo = {otherInfo-> companyViewModel.addPersonnelOtherInfo(otherInfo)},
                onTakePhoto = {/*navigateToTakePhoto()*/},
                addPersonnel = {_personnel->
                    companyViewModel.registerPersonnel(_personnel)

                }
            ) {
                navigateToBottomNav()
            }
        }

    }
}
