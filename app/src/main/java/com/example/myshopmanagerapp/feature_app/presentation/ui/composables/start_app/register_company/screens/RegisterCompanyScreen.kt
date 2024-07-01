package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.FirstScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.RegisterCompanyFullInfoContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.RegisterCompanyInfoContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun RegisterCompanyScreen(
    companyViewModel: CompanyViewModel,
    navigateToRegisterPersonnelScreen: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)

    LaunchedEffect(Unit){
        userPreferences.saveRepositoryJobMessage(Constants.emptyString)
        userPreferences.saveRepositoryJobSuccessValue(false)
    }

    Scaffold(
        topBar = { FirstScreenTopBar(topBarTitleText = "Sign up") }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val isRegistered = userPreferences.getRepositoryJobSuccessState.collectAsState(initial = false).value
            val isRegisteredMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = Constants.emptyString).value

            RegisterCompanyFullInfoContent(
                company = companyViewModel.addCompanyInfo,
                companySavingMessage = isRegisteredMessage,
                companySavingIsSuccessful = isRegistered ?: false,
                addCompanyName = {name-> companyViewModel.addCompanyName(name)},
                addCompanyContact = {contact-> companyViewModel.addCompanyContact(contact)},
                addCompanyLocation = {location-> companyViewModel.addCompanyLocation(location)},
                addCompanyOwners = {owners-> companyViewModel.addCompanyOwners(owners)},
                addCompanyEmail = {email-> companyViewModel.addEmail(email)},
                addCompanyPassword = {password-> companyViewModel.addPassword(password)},
                addPasswordConfirmation = {password-> companyViewModel.addPasswordConfirmation(password)},
                addCompanyProducts = {products-> companyViewModel.addCompanyProductAndServices(products)},
                addCompanyOtherInfo = {otherInfo-> companyViewModel.addCompanyOtherInfo(otherInfo)},
                addCompany = { company-> companyViewModel.registerShopAccount(company, companyViewModel.passwordConfirmation)},
            ) {
                navigateToRegisterPersonnelScreen()
            }
        }
    }
}



