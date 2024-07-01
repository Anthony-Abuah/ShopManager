package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_company.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_company.LoginCompanyContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun LoginCompanyScreen(
    companyViewModel: CompanyViewModel,
    navigateToCreateNewAccount: () -> Unit,
    navigateToBottomNav: () -> Unit
) {
    Scaffold{
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginCompanyContent(
                login = {email, password->
                    companyViewModel.companyLogin(email, password)
                },
                createNewAccount = navigateToCreateNewAccount
            ) {
                navigateToBottomNav()
            }
        }
    }
}