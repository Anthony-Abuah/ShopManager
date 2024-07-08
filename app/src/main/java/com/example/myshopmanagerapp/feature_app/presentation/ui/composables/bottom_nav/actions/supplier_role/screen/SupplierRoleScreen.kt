package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.supplier_role.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.TypeConverters.toSupplierRoles
import com.example.myshopmanagerapp.core.TypeConverters.toSupplierRolesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.SupplierRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.supplier_role.SupplierRoleContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun SupplierRoleScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openSupplierRoles by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Supplier Roles") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openSupplierRoles = !openSupplierRoles
            }
        }
    ){
        val supplierRolesJson = userPreferences.getSupplierRole.collectAsState(initial = emptyString).value
        val supplierRoles = supplierRolesJson.toSupplierRoles()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SupplierRoleContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openSupplierRoles,
            title = "Add Supplier Role",
            textContent = emptyString,
            placeholder = "Eg: Sales Supplier",
            label = "Add supplier role",
            icon = R.drawable.ic_role,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Supplier role not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _supplierRole ->
                val mutableSupplierRoles = mutableListOf<SupplierRole>()
                mutableSupplierRoles.addAll(supplierRoles)
                val newSupplierRole = SupplierRole(_supplierRole.trim())
                val newMutableSupplierRole = mutableSupplierRoles.plus(newSupplierRole)
                val newMutableSupplierRoleJson = newMutableSupplierRole.sortedBy { it.supplierRole.first() }.toSet().toList().toSupplierRolesJson()
                coroutineScope.launch {
                    userPreferences.saveSupplierRole(newMutableSupplierRoleJson)
                }
                Toast.makeText(context,"Supplier role successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openSupplierRoles = false
        }
    }
}
