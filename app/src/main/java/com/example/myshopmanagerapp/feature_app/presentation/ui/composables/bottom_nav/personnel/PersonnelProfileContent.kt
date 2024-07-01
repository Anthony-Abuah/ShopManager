package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelFirstName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelLastName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelOtherName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelRole
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelUserName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelContact
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelFirstName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelHasAdminRights
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelInformation
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelIsActive
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelLastName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelOtherNames
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRole
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRolePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelUserName
import com.example.myshopmanagerapp.core.FormRelatedString.UniquePersonnelId
import com.example.myshopmanagerapp.core.Functions
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRoles
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.domain.model.PersonnelRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PersonnelProfileContent(
    personnel: PersonnelEntity,
    isUpdatingPersonnel: Boolean,
    updateIsSuccessful: Boolean,
    isLoggingOut: Boolean,
    logoutMessage: String,
    updatePersonnelMessage: String,
    logoutIsSuccessful: Boolean,
    updateUsername: (userName: String) -> Unit,
    updateFirstName: (personnelFirstName: String) -> Unit,
    updateLastName: (personnelFirstName: String) -> Unit,
    updateOtherNames: (personnelFirstName: String?) -> Unit,
    updateContact: (personnelContact: String) -> Unit,
    updatePersonnelRole: (personnelLocation: String) -> Unit,
    reloadPersonnel: () -> Unit,
    logout: () -> Unit,
    updateOtherInfo: (shortNotes: String?) -> Unit,
    navigateBack: () -> Unit,
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    var updateConfirmationInfo by remember {
        mutableStateOf(false)
    }
    var confirmLogout by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openRolesDialog by remember {
        mutableStateOf(false)
    }
    var personnelRoles = UserPreferences(context).getPersonnelRoles.collectAsState(initial = null).value


    BasicScreenColumnWithoutBottomBar {
        // Personnel Photo
        ViewPhoto(icon = R.drawable.ic_personnel)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        //Personnel Info
        ViewInfo(PersonnelInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique PersonnelId Id
        ViewTextValueRow(
            viewTitle = UniquePersonnelId,
            viewValue = personnel.uniquePersonnelId
        )

        HorizontalDivider()

        // Personnel User Name
        ViewOrUpdateTextValueRow(
            viewTitle = PersonnelUserName,
            viewValue = personnel.userName,
            placeholder = PersonnelNamePlaceholder,
            label = EnterPersonnelUserName,
            icon = R.drawable.ic_edit,
            getUpdatedValue = {
                updateUsername  (it)
                updateConfirmationInfo = !updateConfirmationInfo
            }
        )

        HorizontalDivider()

        // Personnel First Name
        ViewOrUpdateTextValueRow(
            viewTitle = PersonnelFirstName,
            viewValue = personnel.firstName,
            placeholder = PersonnelNamePlaceholder,
            label = EnterPersonnelFirstName,
            icon = R.drawable.ic_edit,
            getUpdatedValue = {
                updateFirstName(it)
                updateConfirmationInfo = !updateConfirmationInfo}
        )

        HorizontalDivider()

        // Personnel Last Name
        ViewOrUpdateTextValueRow(
            viewTitle = PersonnelLastName,
            viewValue = personnel.lastName,
            placeholder = PersonnelNamePlaceholder,
            label = EnterPersonnelLastName,
            icon = R.drawable.ic_edit,
            getUpdatedValue = {updateLastName(it)
                updateConfirmationInfo = !updateConfirmationInfo}
        )

        HorizontalDivider()

        // Personnel Other Names
        ViewOrUpdateTextValueRow(
            viewTitle = PersonnelOtherNames,
            viewValue = personnel.otherNames.toNotNull(),
            placeholder = PersonnelNamePlaceholder,
            label = EnterPersonnelOtherName,
            icon = R.drawable.ic_edit,
            getUpdatedValue = {updateOtherNames(it)
                updateConfirmationInfo = !updateConfirmationInfo}
        )

        HorizontalDivider()

        // Personnel Contact
        ViewOrUpdateNumberValueRow(
            viewTitle = PersonnelContact,
            viewValue = personnel.contact,
            placeholder = PersonnelContactPlaceholder,
            label = EnterPersonnelContact,
            icon = R.drawable.ic_edit,
            getUpdatedValue = {updateContact(it)
                updateConfirmationInfo = !updateConfirmationInfo}
        )

        HorizontalDivider()

        // Personnel PersonnelRole
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = PersonnelRole,
            viewValue = personnel.role.toNotNull(),
            placeholder = PersonnelRolePlaceholder,
            label = EnterPersonnelRole,
            expandedIcon = R.drawable.ic_person_filled,
            unexpandedIcon = R.drawable.ic_person_outline,
            listItems = personnelRoles.toPersonnelRoles().map { it.personnelRole },
            onClickAddButton = { openRolesDialog = !openRolesDialog },
            getUpdatedValue = { updatePersonnelRole(it)
                updateConfirmationInfo = !updateConfirmationInfo }
        )

        HorizontalDivider()

        // Personnel Has Admin Right
        ViewTextValueRow(
            viewTitle = PersonnelHasAdminRights,
            viewValue = if (personnel.hasAdminRights == true) "Yes" else "No"
        )

        HorizontalDivider()

        // Personnel Is Active
        ViewTextValueRow(
            viewTitle = PersonnelIsActive,
            viewValue = if (personnel.isActive == true) "Yes" else "No"
        )

        HorizontalDivider()

        // Personnel Short Notes
        ViewOrUpdateNumberValueRow(
            viewTitle = PersonnelShortNotes,
            viewValue = personnel.otherInfo.toNotNull(),
            placeholder = emptyString,
            label = EnterShortDescription,
            icon = R.drawable.ic_edit,
            getUpdatedValue = {updateOtherInfo(it)
                updateConfirmationInfo = !updateConfirmationInfo
            }
        )

        HorizontalDivider()

        Box(modifier = Modifier
            .padding(LocalSpacing.current.smallMedium)
            .fillMaxWidth()
            .height(LocalSpacing.current.buttonHeight)
            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
            .clickable { confirmLogout = true },
            contentAlignment = Alignment.Center
        ){
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.padding(LocalSpacing.current.small)){
                    androidx.compose.material.Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Box(modifier = Modifier.padding(LocalSpacing.current.small)){
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
    BasicTextFieldAlertDialog(
        openDialog = openRolesDialog,
        title = "Add PersonnelRole",
        textContent = emptyString,
        placeholder = "Eg: Manager",
        label = "Add personnel role",
        icon = R.drawable.ic_role,
        keyboardType = KeyboardType.Text,
        unconfirmedUpdatedToastText = "PersonnelRole not added",
        confirmedUpdatedToastText = null,
        getValue = { _newRole ->
            val newPersonnelRole = PersonnelRole(_newRole)
            val roles = personnelRoles.toPersonnelRoles()
            val mutablePersonnelRoles = mutableListOf<PersonnelRole>()
            if (roles.map { it.personnelRole.trim().lowercase(Locale.ROOT) }.contains(_newRole.trim().lowercase(
                    Locale.ROOT))) {
                Toast.makeText(context, "PersonnelRole: $_newRole already exists", Toast.LENGTH_LONG).show()
                openRolesDialog = false
            } else {
                mutablePersonnelRoles.addAll(roles)
                mutablePersonnelRoles.add(newPersonnelRole)
                personnelRoles = Functions.toRolesJson(mutablePersonnelRoles)
                coroutineScope.launch {
                    UserPreferences(context).savePersonnelRoles(personnelRoles ?: emptyString)
                }
                Toast.makeText(context, "PersonnelRole: $_newRole successfully added", Toast.LENGTH_LONG).show()
            }
        }
    ) {
        openRolesDialog = false
    }

    DeleteConfirmationDialog(
        openDialog = confirmLogout,
        title = "Logout",
        textContent = "Are you sure you want to log out?",
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            confirmationInfoDialog = true
            logout()
        }) {
        confirmLogout = false
    }
    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isLoggingOut,
        title = null,
        textContent = logoutMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = logoutMessage.toNotNull()
    ) {
        if (logoutIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
    ConfirmationInfoDialog(
        openDialog = updateConfirmationInfo,
        isLoading = isUpdatingPersonnel,
        title = null,
        textContent = updatePersonnelMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = updatePersonnelMessage.toNotNull()
    ) {
        if (updateIsSuccessful){
            reloadPersonnel()
        }
        updateConfirmationInfo = false
    }
}