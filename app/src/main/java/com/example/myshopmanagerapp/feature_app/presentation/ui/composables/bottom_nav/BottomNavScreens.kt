package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav

import com.example.myshopmanagerapp.R


sealed class BottomNavScreens(
    val name: String,
    val route: String,
    val focused_icon: Int,
    val unfocused_icon: Int,
){

    object Home: BottomNavScreens(
        name = "Home",
        route = "to_home",
        focused_icon = R.drawable.ic_home,
        unfocused_icon = R.drawable.ic_home
    )
    object Report: BottomNavScreens(
        name = "Report",
        route = "to_report",
        focused_icon = R.drawable.ic_report,
        unfocused_icon = R.drawable.ic_report
    )
    object Settings: BottomNavScreens(
        name = "Settings",
        route = "to_settings",
        focused_icon = R.drawable.ic_settings,
        unfocused_icon = R.drawable.ic_settings
    )

}