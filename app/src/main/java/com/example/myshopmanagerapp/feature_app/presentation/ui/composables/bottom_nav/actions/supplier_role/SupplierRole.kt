package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.supplier_role

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.Delete
import com.example.myshopmanagerapp.core.Constants.Edit
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.AddSupplierRole
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierRoleNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierRolePlaceholder
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toSupplierRolesJson
import com.example.myshopmanagerapp.core.TypeConverters.toSupplierRoles
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.SupplierRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun SupplierRoleContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var supplierRolesJson = userPreferences.getSupplierRole.collectAsState(initial = emptyString).value

    var openSupplierRoles by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedSupplierRole by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val supplierRoles = supplierRolesJson.toSupplierRoles()
            supplierRoles.forEachIndexed { index, supplierRole ->
                CategoryCard(number = "${index.plus(1)}",
                    name = supplierRole.supplierRole,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openSupplierRoles = !openSupplierRoles
                                selectedSupplierRole = supplierRole.supplierRole
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedSupplierRole = supplierRole.supplierRole
                            }
                            else -> {
                                selectedSupplierRole = supplierRole.supplierRole
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openSupplierRoles,
                    title = AddSupplierRole,
                    textContent = emptyString,
                    placeholder = SupplierRolePlaceholder,
                    label = AddSupplierRole,
                    icon = R.drawable.ic_person_outline,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = SupplierRoleNotAdded,
                    confirmedUpdatedToastText = null,
                    getValue = { _supplierRole ->
                        val newSupplierRole = SupplierRole(_supplierRole.trim())
                        val supplierRolesList = supplierRolesJson.toSupplierRoles()
                        val mutableSupplierRoles = mutableListOf<SupplierRole>()
                        if (_supplierRole.isBlank()){
                            Toast.makeText(context, FormRelatedString.EnterExpenseName, Toast.LENGTH_LONG).show()
                            openSupplierRoles = false
                        }
                        else if (supplierRolesList.map { it.supplierRole.trim().lowercase(Locale.ROOT) }.contains(_supplierRole.trim().lowercase(
                                Locale.ROOT))) {
                            Toast.makeText(context, "Supplier role: $_supplierRole already exists", Toast.LENGTH_LONG).show()
                            openSupplierRoles = false
                        } else {
                            mutableSupplierRoles.addAll(supplierRolesList)
                            mutableSupplierRoles.add(newSupplierRole)
                            supplierRolesJson = mutableSupplierRoles.toSet().sortedBy { it.supplierRole.first() }.toSupplierRolesJson()
                            coroutineScope.launch {
                                UserPreferences(context).saveExpenseNames(supplierRolesJson.toNotNull())
                            }
                            Toast.makeText(context, "Supplier Role: $_supplierRole successfully added", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    openSupplierRoles = false
                }

                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Supplier Role",
                    textContent = "Are you sure you want to remove this supplier role?",
                    unconfirmedDeletedToastText = "Did not remove supplier role",
                    confirmedDeleteToastText = "Supplier Role deleted successfully",
                    confirmDelete = {
                        val thisSupplierRole = SupplierRole(selectedSupplierRole)
                        val mutableSupplierRoles = mutableListOf<SupplierRole>()
                        mutableSupplierRoles.addAll(supplierRoles)
                        val deletedMutableSupplierRoles = mutableSupplierRoles.minus(thisSupplierRole)
                        val mutableSupplierRolesJson =
                            deletedMutableSupplierRoles.sortedBy { it.supplierRole }
                                .toSupplierRolesJson()
                        coroutineScope.launch {
                            userPreferences.saveSupplierRole(mutableSupplierRolesJson)
                        }
                        Toast.makeText(
                            context,
                            "Supplier role successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
