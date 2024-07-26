package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel.PersonnelNavScreens
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_personnel.screens.LoginPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_personnel.screens.RegisterPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun PersonnelProfileNavGraph (
    isLoggedIn: Boolean,
    navController: NavController,
){
    val context = LocalContext.current
    val personnel = UserPreferences(context).getPersonnelInfo.collectAsState(initial = emptyString).value ?: emptyString
    val uniquePersonnelId = personnel.toPersonnelEntity()?.uniquePersonnelId.toNotNull()

    val mainNavController = rememberNavController()
    NavHost(
        navController = mainNavController,
        startDestination = if (isLoggedIn) PersonnelNavScreens.Profile.route else PersonnelNavScreens.Login.route)
    {
        composable(route = PersonnelNavScreens.Profile.route){
            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = mainNavController)
            PersonnelProfileScreen(companyViewModel = companyViewModel, uniquePersonnelId = uniquePersonnelId) {
                navController.popBackStack()
            }
        }

        composable(route = PersonnelNavScreens.Login.route){
            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = mainNavController)
            LoginPersonnelScreen(
                companyViewModel = companyViewModel,
                navigateToCreateNewAccount = { mainNavController.navigate(PersonnelNavScreens.Register.route) }
            ) { navController.popBackStack() }
        }

        composable(route = PersonnelNavScreens.Register.route){
            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = mainNavController)
            RegisterPersonnelScreen(companyViewModel = companyViewModel,
                navigateToBottomNav = { navController.popBackStack() }
            ) {
                navController.popBackStack()
            }
        }
    }
}

