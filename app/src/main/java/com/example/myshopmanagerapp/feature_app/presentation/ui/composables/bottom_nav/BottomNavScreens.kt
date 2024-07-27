package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav

import com.example.myshopmanagerapp.R


sealed class BottomNavScreens(
    val name: String,
    val route: String,
    val focused_icon: Int,
    val unfocused_icon: Int,
){

    object Records: BottomNavScreens(
        name = "Records",
        route = "to_records",
        focused_icon = R.drawable.ic_records,
        unfocused_icon = R.drawable.ic_records
    )
    object Report: BottomNavScreens(
        name = "Report",
        route = "to_report",
        focused_icon = R.drawable.ic_report,
        unfocused_icon = R.drawable.ic_report
    )
    object Actions: BottomNavScreens(
        name = "Actions",
        route = "to_actions",
        focused_icon = R.drawable.ic_activity,
        unfocused_icon = R.drawable.ic_activity
    )

}