package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens

sealed class PersonnelScreens(val route: String){
    object PersonnelListScreen: PersonnelScreens("to_personnel_list_screen")
    object AddPersonnelScreen: PersonnelScreens("to_add_personnel_screen")
    object ViewPersonnelScreen: PersonnelScreens("to_view_personnel_screen")
    object PersonnelPhotoScreen: PersonnelScreens("to_personnel_photo_screen")
    object PersonnelAddRoleScreen: PersonnelScreens("to_personnel_add_role_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}