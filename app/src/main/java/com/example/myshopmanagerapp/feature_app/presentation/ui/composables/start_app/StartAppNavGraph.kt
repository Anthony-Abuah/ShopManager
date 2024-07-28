package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.BottomNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.BottomNavScreens
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens.HomeScreens
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_company.screens.LoginCompanyScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyInfoScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyMoreInfoScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyPasswordScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_personnel.screens.RegisterPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun StartAppNavGraph (navController: NavHostController){
    val startAppNavHostController = rememberNavController()

    NavHost(
        navController = startAppNavHostController,
        startDestination = StartAppScreens.LogInCompanyScreen.route
    ) {
        composable(route = StartAppScreens.LogInCompanyScreen.route){
            LoginCompanyScreen {
                startAppNavHostController.navigate(StartAppScreens.RegisterCompanyNavigation.route)
            }
        }

        navigation(
            startDestination = StartAppScreens.RegisterCompanyInfoScreen.route,
            route = StartAppScreens.RegisterCompanyNavigation.route
        ) {
            composable(route = StartAppScreens.RegisterCompanyInfoScreen.route) {
                val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = startAppNavHostController)
                RegisterCompanyInfoScreen(companyViewModel) {
                    startAppNavHostController.navigate(StartAppScreens.RegisterCompanyMoreInfoScreen.route)
                }
            }

            composable(route = StartAppScreens.RegisterCompanyMoreInfoScreen.route) {
                val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = startAppNavHostController)
                RegisterCompanyMoreInfoScreen(companyViewModel) {
                    startAppNavHostController.navigate(StartAppScreens.RegisterCompanyPasswordScreen.route)
                }
            }

            composable(route = StartAppScreens.RegisterCompanyPasswordScreen.route) {
                val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = startAppNavHostController)
                RegisterCompanyPasswordScreen(companyViewModel) {
                    startAppNavHostController.navigate(HomeScreens.PersonnelProfileNavGraph.route){
                        popUpTo(HomeScreens.PersonnelProfileNavGraph.route) {
                            inclusive = true
                        }
                    }
                }
            }

        }

        composable(route = HomeScreens.PersonnelProfileNavGraph.route){
            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = startAppNavHostController)
            RegisterPersonnelScreen(companyViewModel = companyViewModel,
                navigateToBottomNav = {
                    navController.navigate(BottomNavScreens.Records.route){
                        popUpTo(BottomNavScreens.Records.route) {
                            inclusive = true
                        }
                    }
                }
            ) {
                navController.navigate(BottomNavScreens.Records.route){
                    popUpTo(BottomNavScreens.Records.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(route = StartAppScreens.BottomNavGraph.route){
            BottomNavGraph()
        }
    }
}

