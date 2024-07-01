package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.FirstScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.RegisterCompanyMoreInfoContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun RegisterCompanyMoreInfoScreen(
    companyViewModel: CompanyViewModel,
    navigateToNextScreen: () -> Unit
) {
    Scaffold{
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterCompanyMoreInfoContent(
                company = companyViewModel.addCompanyInfo,
                addCompanyProducts = {products-> companyViewModel.addCompanyProductAndServices(products.trim())},
                addCompanyOtherInfo = {otherInfo-> companyViewModel.addCompanyOtherInfo(otherInfo.trim())},
                addCompanyOwners = {owners-> companyViewModel.addCompanyOwners(owners.trim())},
                navigateToNextScreen = navigateToNextScreen,
            )
        }
    }
}



