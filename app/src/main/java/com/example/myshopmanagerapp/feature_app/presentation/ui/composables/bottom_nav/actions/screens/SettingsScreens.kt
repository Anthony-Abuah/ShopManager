package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.screens

sealed class SettingsScreens(val route: String){
    object MainSettingsScreen: SettingsScreens("to_main_home_screen")
    object ProfileScreen: SettingsScreens("to_profile_screen")
    object LoginScreen: SettingsScreens("to_login_screen")
    object RegisterScreen: SettingsScreens("to_register_screen")
    object ChangePasswordScreen: SettingsScreens("to_change_password_screen")
    object ExpenseTypeScreen: SettingsScreens("to_expense_type_screen")
    object ExpenseNameScreen: SettingsScreens("to_expense_name_screen")
    object ManufacturersScreen: SettingsScreens("to_manufacturers_screen")
    object ItemCategoryScreen: SettingsScreens("to_item_category_screen")
    object PersonnelRoleScreen: SettingsScreens("to_personnel_role_screen")
    object SupplierRoleScreen: SettingsScreens("to_supplier_role_screen")
    object SusuCollectorsScreen: SettingsScreens("to_susu_collectors_screen")
    object PreferenceScreen: SettingsScreens("to_preference_screen")
    object BackupAndRestoreScreen: SettingsScreens("to_backup_and_restore_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}