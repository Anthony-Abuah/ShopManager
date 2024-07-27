package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.listOfChangePassword
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntityJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel.PersonnelProfileContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ProfileScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import kotlinx.coroutines.launch


@Composable
fun PersonnelProfileScreen(
    companyViewModel: CompanyViewModel,
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniquePersonnelId: String,
    navigateToChangePasswordScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        personnelViewModel.getPersonnel(uniquePersonnelId)
    }

    Scaffold(
        topBar = {
            ProfileScreenTopBar(
                topBarTitleText = "Personnel",
                profileDropDownItems = listOfChangePassword,
                onClickItem = {
                    navigateToChangePasswordScreen()
                }
            ) {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PersonnelProfileContent(
                personnel = personnelViewModel.personnelInfo,
                updateIsSuccessful = personnelViewModel.updatePersonnelState.value.isSuccessful,
                isUpdatingPersonnel = personnelViewModel.updatePersonnelState.value.isLoading,
                updatePersonnelMessage = personnelViewModel.updatePersonnelState.value.message.toNotNull(),
                isLoggingOut = companyViewModel.logoutPersonnelState.value.isLoading,
                logoutMessage = companyViewModel.logoutPersonnelState.value.message.toNotNull(),
                logoutIsSuccessful = companyViewModel.logoutPersonnelState.value.isSuccessful,
                updateUsername = {personnelViewModel.updatePersonnelUserName(it)},
                updateFirstName = {personnelViewModel.updatePersonnelFirstName(it)},
                updateLastName = {personnelViewModel.updatePersonnelLastName(it)},
                updateOtherNames = {personnelViewModel.updatePersonnelOtherName(it)},
                updateContact = {personnelViewModel.updatePersonnelContact(it)},
                updatePersonnelRole = {personnelViewModel.updatePersonnelRole(it)},
                reloadPersonnel = {
                    personnelViewModel.getPersonnel(uniquePersonnelId)
                    coroutineScope.launch {
                        UserPreferences(context).savePersonnelInfo(personnelViewModel.personnelInfo.toPersonnelEntityJson())
                    }
                },
                logout = { companyViewModel.logoutPersonnel() },
                updateOtherInfo = {personnelViewModel.updatePersonnelOtherInfo(it)}
            ) {
                navigateBack()
            }
        }
    }

}
