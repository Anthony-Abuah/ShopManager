package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.backup.screens.BackupAndRestoreScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.change_password.screens.ChangePasswordScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.expense_name.screens.ExpenseNameScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.expense_type.screen.ExpenseTypeScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.item_category.screen.ItemCategoryScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.manufacturers.screen.ManufacturerScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.personnel_role.screen.PersonnelRoleScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.preferences.screens.PreferencesScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.profile.screens.ProfileScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.register.screens.RegisterScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.supplier_role.screen.SupplierRoleScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.bank_personnel.screen.BankPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel.screens.PersonnelProfileNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens.HomeScreens
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt.screen.ReceiptNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.StartAppScreens
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_company.screens.LoginCompanyScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyInfoScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyMoreInfoScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company.screens.RegisterCompanyPasswordScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_personnel.screens.RegisterPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun SettingsNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SettingsScreens.MainSettingsScreen.route)
    {
        composable(route = SettingsScreens.MainSettingsScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }
        ){
            SettingsScreen(
                navigateToProfileScreen = {
                    navController.navigate(SettingsScreens.ProfileScreen.route)
                },
                navigateToRegisterScreen = {
                    navController.navigate(StartAppScreens.RegisterCompanyNavigation.route)
                },
                navigateToLoginScreen = {
                    navController.navigate(StartAppScreens.LogInCompanyScreen.route)
                },
                navigateToExpenseTypeScreen = {
                    navController.navigate(SettingsScreens.ExpenseTypeScreen.route)
                },
                navigateToExpenseNameScreen = {
                    navController.navigate(SettingsScreens.ExpenseNameScreen.route)
                },
                navigateToSusuCollectorsScreen = {
                    navController.navigate(SettingsScreens.SusuCollectorsScreen.route)
                },
                navigateToPersonnelRolesScreen = {
                    navController.navigate(SettingsScreens.PersonnelRoleScreen.route)
                },
                navigateToManufacturersScreen = {
                    navController.navigate(SettingsScreens.ManufacturersScreen.route)
                },
                navigateToItemCategoryScreen = {
                    navController.navigate(SettingsScreens.ItemCategoryScreen.route)
                },
                navigateToBackupAndRestoreScreen = {
                    navController.navigate(SettingsScreens.BackupAndRestoreScreen.route)
                },
                navigateToSupplierRoleScreen = {
                    navController.navigate(SettingsScreens.SupplierRoleScreen.route)
                },
                navigateToGenerateInvoiceScreen = {
                    navController.navigate(SettingsScreens.GenerateReceiptNavGraph.route)
                },
                navigateToPreferencesScreen = {
                    navController.navigate(SettingsScreens.PreferenceScreen.route)
                }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = SettingsScreens.GenerateReceiptNavGraph.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            ReceiptNavGraph(navHostController = navController)
        }

        composable(route = StartAppScreens.LogInCompanyScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            LoginCompanyScreen {
                navController.navigate(StartAppScreens.RegisterCompanyNavigation.route)
            }
        }

        navigation(
            startDestination = StartAppScreens.RegisterCompanyInfoScreen.route,
            route = StartAppScreens.RegisterCompanyNavigation.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }
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
                    navController.navigate(HomeScreens.PersonnelProfileNavGraph.route)
                }
            }
        }

        composable(route = HomeScreens.PersonnelProfileNavGraph.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }) {
            PersonnelProfileNavGraph(isLoggedIn = false, navController = navController)
        }

        composable(route = HomeScreens.PersonnelProfileNavGraph.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
            RegisterPersonnelScreen(companyViewModel = companyViewModel,
                navigateToBottomNav = { navHostController.popBackStack() }) {
            }
        }

        composable(route = SettingsScreens.ExpenseTypeScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            ExpenseTypeScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.PreferenceScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            PreferencesScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.SupplierRoleScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            SupplierRoleScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.BackupAndRestoreScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            BackupAndRestoreScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.ExpenseNameScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            ExpenseNameScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.PersonnelRoleScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            PersonnelRoleScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.SusuCollectorsScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            BankPersonnelScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.ManufacturersScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            ManufacturerScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.ItemCategoryScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            ItemCategoryScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.RegisterScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            RegisterScreen {
                navController.popBackStack()
            }
        }
        composable(route = SettingsScreens.ChangePasswordScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            ChangePasswordScreen {
                navController.popBackStack()
            }
        }

        composable(route = SettingsScreens.ProfileScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            ProfileScreen(
                openLoginPage = { navController.navigate(StartAppScreens.LogInCompanyScreen.route) },
                navigateToChangePasswordScreen = { navController.navigate(SettingsScreens.ChangePasswordScreen.route) },
                openSignUpPage = { navController.navigate(StartAppScreens.RegisterCompanyNavigation.route) }
            ) {
                navController.popBackStack()
            }
        }

    }
}