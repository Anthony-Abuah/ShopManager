package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ProfileDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.profile.ProfileContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ProfileScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun ProfileScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    openLoginPage: () -> Unit,
    openSignUpPage: () -> Unit,
    navigateToChangePasswordScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val isLoggedIn = userPreferences.getLoggedInState.collectAsState(initial = false).value
    val shopInfo = userPreferences.getShopInfo.collectAsState(initial = null).value

    val listOfDropDownItems = listOf(ProfileDropDownItem("Change Password"))
    Scaffold(
        topBar = {
            ProfileScreenTopBar(
                topBarTitleText = "Shop Profile",
                profileDropDownItems = listOfDropDownItems,
                onClickItem = {
                    if (isLoggedIn.toNotNull()) {
                        navigateToChangePasswordScreen()
                    }
                }
            ) {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val companyLogOutMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = emptyString).value

            ProfileContent(
                isLoggedIn = isLoggedIn?: false,
                shopInfo = shopInfo.toCompanyEntity(),
                logoutMessage = companyLogOutMessage.toNotNull(),
                isLoggingOut = false,
                logout = { companyViewModel.companyLogout() },
                openRegisterPage = { openSignUpPage() },
                openLoginPage = { openLoginPage() },
            )
        }
    }
}
