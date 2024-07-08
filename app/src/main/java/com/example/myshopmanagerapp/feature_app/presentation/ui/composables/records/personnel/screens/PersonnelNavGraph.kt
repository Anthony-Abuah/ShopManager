package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun PersonnelNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = PersonnelScreens.PersonnelListScreen.route)
    {
        composable(route = PersonnelScreens.PersonnelListScreen.route,
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
            val personnelViewModel = it.sharedViewModel<PersonnelViewModel>(navHostController = navController)
            PersonnelListScreen(
                personnelViewModel = personnelViewModel,
                navigateToAddPersonnelScreen = {navController.navigate(PersonnelScreens.AddPersonnelScreen.route)},
                navigateToViewPersonnelScreen = {uniquePersonnelId->
                    navController.navigate(PersonnelScreens.ViewPersonnelScreen.withArgs(uniquePersonnelId))}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = PersonnelScreens.AddPersonnelScreen.route,
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
            val personnelViewModel = it.sharedViewModel<PersonnelViewModel>(navHostController = navController)
            AddPersonnelScreen (
                personnelViewModel = personnelViewModel,
                navigateToTakePhoto = {
                    navController.navigate(PersonnelScreens.PersonnelPhotoScreen.route)
                },
            ){
                navController.popBackStack()
            }
        }

        composable(route = PersonnelScreens.ViewPersonnelScreen.route+ "/{uniquePersonnelId}",
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
            },
            arguments = listOf(
                navArgument("uniquePersonnelId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                })
        ){
            val personnelViewModel = it.sharedViewModel<PersonnelViewModel>(navHostController = navController)
            val uniquePersonnelId = it.arguments?.getString("uniquePersonnelId")
            if (uniquePersonnelId != null) {
                ViewPersonnelScreen(
                    personnelViewModel = personnelViewModel,
                    uniquePersonnelId = uniquePersonnelId,
                ) {
                    navController.popBackStack()
                }
            }
        }

        composable(route = PersonnelScreens.PersonnelPhotoScreen.route,
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
            PersonnelCameraScreen {
                navController.popBackStack()
            }
        }


    }
}