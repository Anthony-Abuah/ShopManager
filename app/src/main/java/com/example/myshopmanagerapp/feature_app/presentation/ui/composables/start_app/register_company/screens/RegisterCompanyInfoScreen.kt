package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.RegisterCompanyInfoContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun RegisterCompanyInfoScreen(
    companyViewModel: CompanyViewModel,
    navigateToNextScreen: () -> Unit,
) {
    Scaffold(
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterCompanyInfoContent(
                company = companyViewModel.addCompanyInfo,
                addCompanyName = {name-> companyViewModel.addCompanyName(name.trim())},
                addCompanyContact = {contact-> companyViewModel.addCompanyContact(contact.trim())},
                addCompanyLocation = {location-> companyViewModel.addCompanyLocation(location.trim())},
                navigateToNextScreen = navigateToNextScreen
            )
        }
    }
}



