package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.screens

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
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.backup.screens.BackupAndRestoreScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.change_password.screens.ChangePasswordScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.expense_name.screens.ExpenseNameScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.expense_type.screen.ExpenseTypeScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.item_category.screen.ItemCategoryScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.login.screens.LoginScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.manufacturers.screen.ManufacturerScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.personnel_role.screen.PersonnelRoleScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.preferences.screens.PreferencesScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.profile.screens.ProfileScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.register.screens.RegisterScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.supplier_role.screen.SupplierRoleScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.susu_collectors.screen.BankPersonnelScreen

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
                    navController.navigate(SettingsScreens.RegisterScreen.route)
                },
                navigateToLoginScreen = {
                    navController.navigate(SettingsScreens.LoginScreen.route)
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
                navigateToPreferencesScreen = {
                    navController.navigate(SettingsScreens.PreferenceScreen.route)
                }
            ) {
                navHostController.popBackStack()
            }

        }

        composable(route = SettingsScreens.LoginScreen.route,
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
            LoginScreen(
                openSignUpPage = {
                    navController.navigate(SettingsScreens.RegisterScreen.route)
                }
            ) {
                navController.navigate(SettingsScreens.ProfileScreen.route)
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
                openLoginPage = { navController.navigate(SettingsScreens.LoginScreen.route) },
                navigateToChangePasswordScreen = { navController.navigate(SettingsScreens.ChangePasswordScreen.route) },
                openSignUpPage = { navController.navigate(SettingsScreens.RegisterScreen.route) }
            ) {
                navController.popBackStack()
            }
        }

    }
}