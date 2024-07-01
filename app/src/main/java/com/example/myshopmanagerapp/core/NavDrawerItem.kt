package com.example.myshopmanagerapp.core

data class NavDrawerItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val badgeCount: String?,
    val route: String? = null,
)