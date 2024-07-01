package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.BottomNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_company.screens.LoginCompanyScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyInfoScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyMoreInfoScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyPasswordScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_personnel.screens.RegisterPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.screens.StartScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun StartAppNavGraph (){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = StartAppScreens.StartScreen.route
    )
    {
        composable(route = StartAppScreens.StartScreen.route){
            StartScreen(navigateToStartScreen = {_isLoggedIn->
                if (_isLoggedIn) {
                    navController.popBackStack()
                    navController.navigate(StartAppScreens.BottomNavGraph.route) {
                        popUpTo(StartAppScreens.BottomNavGraph.route) {
                            inclusive = true
                        }
                    }
                }else{
                    navController.popBackStack()
                    navController.navigate(StartAppScreens.RegisterNavigation.route) {
                        popUpTo(StartAppScreens.RegisterNavigation.route) {
                            inclusive = true
                        }
                    }
                }
            })
        }

        navigation(
            startDestination = StartAppScreens.LogInCompanyScreen.route,
            route = StartAppScreens.RegisterNavigation.route
        ) {
            composable(route = StartAppScreens.LogInCompanyScreen.route){
                val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
                LoginCompanyScreen(companyViewModel,
                    navigateToCreateNewAccount = {
                    navController.navigate(StartAppScreens.RegisterCompanyNavigation.route)
                }) {
                    navController.navigate(StartAppScreens.BottomNavGraph.route){
                        popUpTo(StartAppScreens.BottomNavGraph.route){
                            inclusive = true
                        }
                    }
                }
            }

            navigation(
                startDestination = StartAppScreens.RegisterCompanyInfoScreen.route,
                route = StartAppScreens.RegisterCompanyNavigation.route
            ) {
                composable(route = StartAppScreens.RegisterCompanyInfoScreen.route) {
                    val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
                    RegisterCompanyInfoScreen(companyViewModel) {
                        navController.navigate(StartAppScreens.RegisterCompanyMoreInfoScreen.route)
                    }
                }
                composable(route = StartAppScreens.RegisterCompanyMoreInfoScreen.route) {
                    val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
                    RegisterCompanyMoreInfoScreen(companyViewModel) {
                        navController.navigate(StartAppScreens.RegisterCompanyPasswordScreen.route)
                    }
                }
                composable(route = StartAppScreens.RegisterCompanyPasswordScreen.route) {
                    val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
                    RegisterCompanyPasswordScreen(companyViewModel) {
                        navController.navigate(StartAppScreens.RegisterPersonnelScreen.route)
                    }
                }

                composable(route = StartAppScreens.RegisterPersonnelScreen.route) {
                    val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
                    RegisterPersonnelScreen(
                        companyViewModel = companyViewModel,
                        navigateToBottomNav = {
                            navController.navigate(StartAppScreens.BottomNavGraph.route) {
                                popUpTo(StartAppScreens.BottomNavGraph.route) {
                                    inclusive = true
                                }
                            }
                        }) {
                        navController.popBackStack()
                    }
                }
            }
        }

        composable(route = StartAppScreens.BottomNavGraph.route){
            BottomNavGraph()
        }
    }
}

