package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.personnel_role

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
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRolesJson
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRoles
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PersonnelRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch


@Composable
fun PersonnelRoleContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val personnelRolesJson = userPreferences.getPersonnelRoles.collectAsState(initial = emptyString).value

    var openPersonnelRoles by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedPersonnelRole by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val personnelRoles = personnelRolesJson.toPersonnelRoles()
            personnelRoles.forEachIndexed { index, personnelRole ->
                CategoryCard(number = "${index.plus(1)}",
                    name = personnelRole.personnelRole,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openPersonnelRoles = !openPersonnelRoles
                                selectedPersonnelRole = personnelRole.personnelRole
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedPersonnelRole = personnelRole.personnelRole
                            }
                            else -> {
                                selectedPersonnelRole = personnelRole.personnelRole
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openPersonnelRoles,
                    title = "Edit Personnel Role",
                    textContent = emptyString,
                    placeholder = "Eg: Sales Personnel",
                    label = "Add personnel role",
                    icon = R.drawable.ic_role,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = "Personnel role not edited",
                    confirmedUpdatedToastText = "Successfully changed",
                    getValue = { _personnelRole ->
                        val editedPersonnelPersonnelRole = PersonnelRole(_personnelRole.trim())
                        val mutablePersonnelRoles = mutableListOf<PersonnelRole>()
                        mutablePersonnelRoles.addAll(personnelRoles)
                        if (mutablePersonnelRoles.remove(PersonnelRole(selectedPersonnelRole.trim()))) {
                            mutablePersonnelRoles.add(editedPersonnelPersonnelRole)
                            val mutablePersonnelRoleJson =
                                mutablePersonnelRoles.sortedBy { it.personnelRole }.toSet().toList().toPersonnelRolesJson()
                            coroutineScope.launch {
                                userPreferences.savePersonnelRoles(mutablePersonnelRoleJson)
                            }
                            Toast.makeText(
                                context,
                                "Personnel Role successfully edited",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    openPersonnelRoles = false
                }
                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Personnel Role",
                    textContent = "Are you sure you want to remove this personnel role?",
                    unconfirmedDeletedToastText = "Did not remove personnel role",
                    confirmedDeleteToastText = "Personnel Role deleted successfully",
                    confirmDelete = {
                        val thisPersonnelRole = PersonnelRole(selectedPersonnelRole)
                        val mutablePersonnelRoles = mutableListOf<PersonnelRole>()
                        mutablePersonnelRoles.addAll(personnelRoles)
                        val deletedMutablePersonnelRoles = mutablePersonnelRoles.minus(thisPersonnelRole)
                        val mutablePersonnelRolesJson =
                            deletedMutablePersonnelRoles.sortedBy { it.personnelRole }
                                .toPersonnelRolesJson()
                        coroutineScope.launch {
                            userPreferences.savePersonnelRoles(mutablePersonnelRolesJson)
                        }
                        Toast.makeText(
                            context,
                            "Personnel role successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
