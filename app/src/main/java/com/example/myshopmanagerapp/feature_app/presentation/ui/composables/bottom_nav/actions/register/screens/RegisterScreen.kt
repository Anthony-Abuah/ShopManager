package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.register.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.register.RegisterContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val isLoggedIn = userPreferences.getLoggedInState.collectAsState(initial = false).value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        userPreferences.saveRepositoryJobMessage(emptyString)
        userPreferences.saveRepositoryJobSuccessValue(false)
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Sign up") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val isRegistered = userPreferences.getRepositoryJobSuccessState.collectAsState(initial = false).value
            val isRegisteredMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = emptyString).value

            RegisterContent(
                companyEntity = companyViewModel.addCompanyInfo,
                isLoggedIn = isLoggedIn ?: false,
                logoutMessage = if (isLoggedIn == true) "Unable to logout" else "Logged out Successfully",
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
                addCompany = {company-> companyViewModel.registerShopAccount(company, companyViewModel.passwordConfirmation)},
                logout = {
                    coroutineScope.launch {
                        userPreferences.saveShopInfo(emptyString)
                        userPreferences.saveLoggedInState(false)
                    }
                }
            ) {
                navigateBack()
            }
        }
    }
}
