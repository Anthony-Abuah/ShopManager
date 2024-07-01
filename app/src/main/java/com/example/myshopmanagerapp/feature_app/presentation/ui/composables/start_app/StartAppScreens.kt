package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app


sealed class StartAppScreens(val route: String) {
    object StartScreen: StartAppScreens("to_start_Screen")
    object RegisterNavigation: StartAppScreens("to_login_Nav_Graph")
    object RegisterCompanyInfoScreen: StartAppScreens("to_register_company_info_screen")
    object RegisterCompanyPasswordScreen: StartAppScreens("to_register_company_password_screen")
    object RegisterCompanyMoreInfoScreen: StartAppScreens("to_register_company_more_info_screen")
    object RegisterCompanyNavigation: StartAppScreens("to_register_company_navigation")
    object RegisterCompanyScreen: StartAppScreens("to_register_company_screen")
    object RegisterPersonnelScreen: StartAppScreens("to_register_personnel_screen")
    object LogInCompanyScreen: StartAppScreens("to_login_Company_screen")
    object LogInPersonnelScreen: StartAppScreens("to_login_personnel_screen")
    object BottomNavGraph: StartAppScreens("to_bottom_Nav_Graph")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}