package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company.AddCompanyContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun AddCompanyScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Company") {
                navigateBack()
            }
        }
    ){it
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddCompanyContent(
                companyEntity = companyViewModel.addCompanyInfo,
                isSavingCompany = companyViewModel.addCompanyState.value.isLoading,
                companySavingMessage = companyViewModel.addCompanyState.value.data,
                companySavingIsSuccessful = companyViewModel.addCompanyState.value.isSuccessful,
                addCompanyName = {name-> companyViewModel.addCompanyName(name)},
                addCompanyContact = {contact-> companyViewModel.addCompanyContact(contact)},
                addCompanyLocation = {location-> companyViewModel.addCompanyLocation(location)},
                addCompanyOwners = {owners-> companyViewModel.addCompanyOwners(owners)},
                addCompanyEmail = {email-> companyViewModel.addEmail(email)},
                addCompanyPassword = {password-> companyViewModel.addPassword(password)},
                addCompanyProducts = {products-> companyViewModel.addCompanyProductAndServices(products)},
                addCompanyOtherInfo = {otherInfo-> companyViewModel.addCompanyOtherInfo(otherInfo)},
                addCompany = {company-> companyViewModel.registerShopAccount(company, "")}
            ) {
                navigateBack()
            }
        }
    }
}
