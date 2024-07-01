package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.change_password.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.change_password.ChangePasswordContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun ChangePasswordScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit
) {

    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    LaunchedEffect(Unit){
        userPreferences.saveRepositoryJobMessage(emptyString)
        userPreferences.saveRepositoryJobSuccessValue(false)
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Change Password") {
                navigateToProfileScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val passwordChangedSuccessful = userPreferences.getRepositoryJobSuccessState.collectAsState(initial = false).value
            val changePasswordMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = emptyString).value
            ChangePasswordContent(
                changePasswordMessage = changePasswordMessage,
                changePasswordIsSuccessful = passwordChangedSuccessful ?: false,
                changePassword = {currentPassword, newPassword, confirmedPassword->
                    companyViewModel.changePassword(currentPassword, newPassword, confirmedPassword)
                }
            ) {
                navigateToProfileScreen()
            }
        }
    }
}