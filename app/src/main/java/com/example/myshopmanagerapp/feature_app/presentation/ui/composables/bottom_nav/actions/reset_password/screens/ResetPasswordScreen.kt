package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.reset_password.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.reset_password.ResetPasswordContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun ResetPasswordScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    LaunchedEffect(Unit){
        userPreferences.saveRepositoryJobMessage(emptyString)
        userPreferences.saveRepositoryJobSuccessValue(false)
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Reset Password") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val passwordChangedSuccessful = userPreferences.getRepositoryJobSuccessState.collectAsState(initial = false).value
            val changePasswordMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = emptyString).value
            ResetPasswordContent(
                resetPasswordMessage = changePasswordMessage,
                resetPasswordIsSuccessful = passwordChangedSuccessful ?: false,
                resetPassword = {email, newPassword, confirmedPassword, personnelPassword->
                    companyViewModel.resetPassword(email, newPassword, confirmedPassword, personnelPassword)
                }
            ) {
                navigateBack()
            }
        }
    }
}


