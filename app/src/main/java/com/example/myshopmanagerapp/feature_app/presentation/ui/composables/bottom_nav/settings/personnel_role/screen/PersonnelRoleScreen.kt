package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.personnel_role.screen

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
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRoles
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRolesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PersonnelRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.personnel_role.PersonnelRoleContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun PersonnelRoleScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openPersonnelRoles by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Personnel Roles") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openPersonnelRoles = !openPersonnelRoles
            }
        }
    ){
        val personnelRolesJson = userPreferences.getPersonnelRoles.collectAsState(initial = emptyString).value
        val personnelRoles = personnelRolesJson.toPersonnelRoles()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PersonnelRoleContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openPersonnelRoles,
            title = "Add Personnel Role",
            textContent = emptyString,
            placeholder = "Eg: Sales Personnel",
            label = "Add personnel role",
            icon = R.drawable.ic_role,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Personnel role not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _personnelRole ->
                val mutablePersonnelRoles = mutableListOf<PersonnelRole>()
                mutablePersonnelRoles.addAll(personnelRoles)
                val newPersonnelRole = PersonnelRole(_personnelRole.trim())
                val newMutablePersonnelRole = mutablePersonnelRoles.plus(newPersonnelRole)
                val newMutablePersonnelRoleJson = newMutablePersonnelRole.sortedBy { it.personnelRole.first() }.toSet().toList().toPersonnelRolesJson()
                coroutineScope.launch {
                    userPreferences.savePersonnelRoles(newMutablePersonnelRoleJson)
                }
                Toast.makeText(context,"Personnel role successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openPersonnelRoles = false
        }
    }
}
