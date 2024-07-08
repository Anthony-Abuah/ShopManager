package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.revenue.screens

sealed class RevenueScreens(val route: String){
    object RevenueListScreen: RevenueScreens("to_revenue_list_screen")
    object AddRevenueScreen: RevenueScreens("to_add_revenue_screen")
    object ViewRevenueScreen: RevenueScreens("to_view_revenue_screen")
    object PersonnelNavGraph: RevenueScreens("to_revenue_personnel_nav_graph_screen")
    object AddPersonnelScreen: RevenueScreens("to_add_revenue_personnel_screen")
    object AddPersonnelRoleScreen: RevenueScreens("to_add_revenue_personnel_role_screen")
    object AddPersonnelPhotoScreen: RevenueScreens("to_add_revenue_personnel_photo_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}