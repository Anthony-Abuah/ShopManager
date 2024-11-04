package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.SettingsContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SettingsScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun SettingsScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToExpenseTypeScreen: () -> Unit,
    navigateToPaymentMethodScreen: () -> Unit,
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
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    Scaffold(
        topBar = { SettingsScreenTopBar(topBarTitleText = "Actions", navigateBack = navigateBack) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val repositoryMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = emptyString).value
            SettingsContent(
                repositoryMessage = repositoryMessage,
                createCloudProfile = {
                    companyViewModel.createCloudProfile()
                },
                navigateToProfileScreen = navigateToProfileScreen,
                navigateToRegisterScreen = navigateToRegisterScreen,
                navigateToLoginScreen = navigateToLoginScreen,
                navigateToPaymentMethodScreen = navigateToPaymentMethodScreen,
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

