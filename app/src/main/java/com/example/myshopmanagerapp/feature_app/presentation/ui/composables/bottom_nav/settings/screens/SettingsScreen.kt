package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.domain.model.ReceiptInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.SettingsContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SettingsScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun SettingsScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
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
    navigateBack: () -> Unit,
) {

    val context = LocalContext.current
    val receiptInfo = ReceiptInfo(
        "Receipt_12323",
        "Dedee Ventures",
        "Takoradi",
        "0500912348",
        1715251678257,
        "Janita Ida Osei",
        "0500912348",
        "Reginald Abuah",
        "Shop Manager",
        listOf(
            ItemQuantityInfo("Jay Kay Exercise books and drawers", 100.0, 30000000.0),
            ItemQuantityInfo("Nataraj Pencil", 2000000.0, 0.50),
            ItemQuantityInfo("Marshal Maths set", 50.0, 10.0),
            ItemQuantityInfo("Big Sketch book", 75.0, 8.0),
        ),

    )
    Scaffold(
        topBar = { SettingsScreenTopBar(topBarTitleText = "Settings", navigateBack = navigateBack) },
        //bottomBar = { BottomBar(navHostController) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SettingsContent(
                isGeneratingPDF = companyViewModel.generateInvoiceState.value.isLoading,
                generatePDFMessage = companyViewModel.generateInvoiceState.value.data.toNotNull(),
                navigateToProfileScreen = navigateToProfileScreen,
                navigateToRegisterScreen = navigateToRegisterScreen,
                navigateToLoginScreen = navigateToLoginScreen,
                navigateToExpenseTypeScreen = navigateToExpenseTypeScreen,
                navigateToExpenseNameScreen = navigateToExpenseNameScreen,
                navigateToItemCategoryScreen = navigateToItemCategoryScreen,
                navigateToManufacturersScreen = navigateToManufacturersScreen,
                navigateToPersonnelRolesScreen = navigateToPersonnelRolesScreen,
                navigateToSusuCollectorsScreen = navigateToSusuCollectorsScreen,
                navigateToPreferencesScreen = navigateToPreferencesScreen,
                navigateToSupplierRoleScreen = navigateToSupplierRoleScreen,
                navigateToBackupAndRestoreScreen = navigateToBackupAndRestoreScreen,
            ){
                companyViewModel.generateReceipt(context, receiptInfo)
            }
        }
    }

}

