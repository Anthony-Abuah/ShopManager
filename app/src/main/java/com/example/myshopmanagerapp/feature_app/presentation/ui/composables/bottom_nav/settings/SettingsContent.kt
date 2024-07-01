package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun SettingsContent(
    isGeneratingPDF: Boolean,
    generatePDFMessage: String,
    navigateToProfileScreen: ()-> Unit,
    navigateToRegisterScreen: ()-> Unit,
    navigateToLoginScreen: ()-> Unit,
    navigateToExpenseTypeScreen: ()-> Unit,
    navigateToExpenseNameScreen: ()-> Unit,
    navigateToManufacturersScreen: ()-> Unit,
    navigateToItemCategoryScreen: ()-> Unit,
    navigateToPersonnelRolesScreen: ()-> Unit,
    navigateToSusuCollectorsScreen: ()-> Unit,
    navigateToBackupAndRestoreScreen: ()-> Unit,
    navigateToSupplierRoleScreen: ()-> Unit,
    navigateToPreferencesScreen: ()-> Unit,
    generateInvoice: ()-> Unit,
    ) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = LocalSpacing.current.smallMedium,
                end = LocalSpacing.current.smallMedium,
                bottom = LocalSpacing.current.smallMedium,
            )
            .clickable { navigateToProfileScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_shop,
                title = "My Account",
                info = "View your shop info here"
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium,)
            .clickable { navigateToRegisterScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_register,
                title = "Register",
                info = null
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToLoginScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_login,
                title = "Login",
                info = null
            )
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.default),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.default)
        ) {
            Text(
                text = "Configurations",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                generateInvoice()
                confirmationInfoDialog = !confirmationInfoDialog
                       },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_money_filled,
                title = "Generate Invoice",
                info = "Click here to generate invoice"
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToPreferencesScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_preferences,
                title = "Preferences",
                info = null
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToBackupAndRestoreScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_backup,
                title = "Backup And Restore",
                info = null
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.default),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.default)
        ) {
            Text(
                text = "Saved Names And Categories",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable {
                navigateToExpenseTypeScreen()
            },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_expense_type,
                title = "Expense Types",
                info = null
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToExpenseNameScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_expense,
                title = "Expense Names",
                info = null
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToItemCategoryScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_category,
                title = "Item Categories",
                info = null
            )
        }


        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToManufacturersScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_manufacturer,
                title = "Manufacturers",
                info = null
            )
        }


        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToPersonnelRolesScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_role,
                title = "Personnel Roles",
                info = null
            )
        }


        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToSusuCollectorsScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_bank,
                title = "Susu Collectors",
                info = null
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.smallMedium)
            .clickable { navigateToSupplierRoleScreen() },
        ) {
            SettingsContentCard(
                icon = R.drawable.ic_role,
                title = "Supplier Role",
                info = null
            )
        }

    }
    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isGeneratingPDF,
        title = null,
        textContent = generatePDFMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        confirmationInfoDialog = false
    }
}
