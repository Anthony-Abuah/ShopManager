package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.domain.model.ReceiptInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.SettingsContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SettingsScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun SettingsScreen(
    navigateToProfileScreen: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToExpenseTypeScreen: () -> Unit,
    navigateToExpenseNameScreen: () -> Unit,
    navigateToManufacturersScreen: () -> Unit,
    navigateToSusuCollectorsScreen: () -> Unit,
    navigateToPersonnelRolesScreen: () -> Unit,
    navigateToItemCategoryScreen: () -> Unit,
    navigateToSupplierRoleScreen: () -> Unit,
    navigateToPreferencesScreen: () -> Unit,
    navigateToBackupAndRestoreScreen: () -> Unit,
    navigateToGenerateInvoiceScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = { SettingsScreenTopBar(topBarTitleText = "Actions", navigateBack = navigateBack) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SettingsContent(
                navigateToProfileScreen = navigateToProfileScreen,
                navigateToRegisterScreen = navigateToRegisterScreen,
                navigateToLoginScreen = navigateToLoginScreen,
                navigateToExpenseTypeScreen = navigateToExpenseTypeScreen,
                navigateToExpenseNameScreen = navigateToExpenseNameScreen,
                navigateToManufacturersScreen = navigateToManufacturersScreen,
                navigateToItemCategoryScreen = navigateToItemCategoryScreen,
                navigateToPersonnelRolesScreen = navigateToPersonnelRolesScreen,
                navigateToSusuCollectorsScreen = navigateToSusuCollectorsScreen,
                navigateToBackupAndRestoreScreen = navigateToBackupAndRestoreScreen,
                navigateToSupplierRoleScreen = navigateToSupplierRoleScreen,
                navigateToPreferencesScreen = navigateToPreferencesScreen,
            ){
                navigateToGenerateInvoiceScreen()
            }
        }
    }

}

