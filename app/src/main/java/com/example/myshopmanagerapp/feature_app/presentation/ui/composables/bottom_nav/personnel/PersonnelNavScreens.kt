package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel


sealed class PersonnelNavScreens(val route: String){

    object Login: PersonnelNavScreens("Login")
    object Profile: PersonnelNavScreens( "Profile")
    object Register: PersonnelNavScreens( "Register")
    object ChangePassword: PersonnelNavScreens( "Change_Password")

}