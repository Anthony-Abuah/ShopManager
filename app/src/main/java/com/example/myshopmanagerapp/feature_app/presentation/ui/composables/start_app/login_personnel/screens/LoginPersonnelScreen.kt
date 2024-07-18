package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_personnel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_personnel.LoginPersonnelContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel

@Composable
fun LoginPersonnelScreen(
    companyViewModel: CompanyViewModel,
    navigateToCreateNewAccount: () -> Unit,
    navigateToBottomNav: () -> Unit
) {
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Personnel Login") {
                navigateToBottomNav()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginPersonnelContent(
                isLoggingIn = companyViewModel.loginPersonnelState.value.isLoading,
                LoginMessage = companyViewModel.loginPersonnelState.value.message.toNotNull(),
                loginSuccessful = companyViewModel.loginPersonnelState.value.isSuccessful,
                login = {userName, password->
                    companyViewModel.loginPersonnel(userName, password)
                },
                createNewAccount = { navigateToCreateNewAccount() }
            ) {
                navigateToBottomNav()
            }
        }
    }
}
