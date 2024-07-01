package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.login.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.login.LoginContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    openSignUpPage: () -> Unit,
    navigateToProfileScreen: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val isLoggedIn = userPreferences.getLoggedInState.collectAsState(initial = false).value
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Login") {
                navigateToProfileScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginContent(
                isLoggedIn = isLoggedIn ?: false,
                logoutMessage = if (isLoggedIn == true) "Unable to logout" else "Logged out Successfully",
                isLoggingIn = companyViewModel.loginState.value.isLoading,
                LoginMessage = companyViewModel.loginState.value.message,
                loginSuccessful = companyViewModel.loginState.value.isSuccessful,
                login = {email, password->
                    companyViewModel.login(email, password)
                },
                logout = {
                    coroutineScope.launch {
                        userPreferences.saveShopInfo(Constants.emptyString)
                        userPreferences.saveLoggedInState(false)
                    }
                },
                openSignUpPage = openSignUpPage
            ) {
                navigateToProfileScreen()
            }
        }
    }
}
