package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens

import android.util.Log
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
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.RegisterCompanyPasswordContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun RegisterCompanyPasswordScreen(
    companyViewModel: CompanyViewModel,
    navigateToRegisterPersonnelScreen: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)

    LaunchedEffect(Unit){
        userPreferences.saveRepositoryJobMessage(Constants.emptyString)
        userPreferences.saveRepositoryJobSuccessValue(false)
    }

    Scaffold{
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val isRegistered = userPreferences.getRepositoryJobSuccessState.collectAsState(initial = false).value
            val isRegisteredMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = Constants.emptyString).value

            RegisterCompanyPasswordContent(
                company = companyViewModel.addCompanyInfo,
                companySavingMessage = isRegisteredMessage,
                companySavingIsSuccessful = isRegistered ?: false,
                addCompanyEmail = {email-> companyViewModel.addEmail(email)},
                addCompanyPassword = {password-> companyViewModel.addPassword(password)},
                addPasswordConfirmation = {password-> companyViewModel.addPasswordConfirmation(password)},
                addCompany = {company->
                    companyViewModel.registerShopAccount(company, companyViewModel.passwordConfirmation)
                    Log.d("RegisterCompanyPasswordScreen", "(RegisterCompanyPasswordScreen) Company Owners(inside) = ${company.companyOwners}")
                },
            ) {
                navigateToRegisterPersonnelScreen()
            }
            Log.d("RegisterCompanyPasswordScreen", "(RegisterCompanyPasswordScreen) Company Owners(outside) = ${companyViewModel.addCompanyInfo.companyOwners}")

        }
    }
}



