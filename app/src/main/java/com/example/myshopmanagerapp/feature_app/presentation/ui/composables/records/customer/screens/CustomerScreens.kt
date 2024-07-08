package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.screens

sealed class CustomerScreens(val route: String){
    object CustomerScreen: CustomerScreens("to_customer_screen")
    object CustomerListScreen: CustomerScreens("to_customer_list_screen")
    object AddCustomerScreen: CustomerScreens("to_add_customer_screen")
    object ViewCustomerScreen: CustomerScreens("to_view_customer_screen")
    object CustomerCameraScreen: CustomerScreens("to_customer_camera_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}