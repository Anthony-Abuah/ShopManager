package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun StartAppNavGraph (isLoggedIn: Boolean, navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = StartAppScreens.StartScreen.route
    )
    {
        composable(route = StartAppScreens.StartScreen.route){
            Column(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ){
                LaunchedEffect(Unit){
                    if (isLoggedIn) {
                        navController.navigate(StartAppScreens.BottomNavGraph.route){
                            popUpTo(StartAppScreens.BottomNavGraph.route){
                                inclusive = true
                            }
                        }
                    }
                    else {
                        navController.navigate(StartAppScreens.LogInCompanyScreen.route){
                            popUpTo(StartAppScreens.LogInCompanyScreen.route){
                                inclusive = true
                            }
                        }
                    }
                }
                CircularProgressIndicator()
            }
        }

        composable(route = StartAppScreens.BottomNavGraph.route){
            BottomNavGraph()
        }

        composable(route = StartAppScreens.LogInCompanyScreen.route){
            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
            LoginCompanyScreen(
                companyViewModel
            ) {
                navController.navigate(StartAppScreens.RegisterCompanyNavigation.route)
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
                    navController.navigate(StartAppScreens.RegisterPersonnelScreen.route){
                        popUpTo(StartAppScreens.RegisterPersonnelScreen.route) {
                            inclusive = true
                        }
                    }
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
}

