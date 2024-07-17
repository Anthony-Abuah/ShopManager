package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_personnel

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.ConfirmPassword
import com.example.myshopmanagerapp.core.FormRelatedString.DoesPersonnelHaveAdminRights
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPassword
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelFirstName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelLastName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelOtherName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelRole
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelUserName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelDescription
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRolePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SavePersonnel
import com.example.myshopmanagerapp.core.Functions.generateUniquePersonnelId
import com.example.myshopmanagerapp.core.Functions.textIsInvalid
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRoles
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRolesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.domain.model.PersonnelRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun RegisterPersonnelContent(
    personnel: PersonnelEntity,
    isSavingPersonnel: Boolean,
    personnelSavingIsSuccessful: Boolean,
    personnelSavingMessage: String?,
    addUserName: (String) -> Unit,
    addFirstName: (String) -> Unit,
    addLastName: (String) -> Unit,
    addOtherNames: (String) -> Unit,
    addContact: (String) -> Unit,
    addRole: (String) -> Unit,
    addPassword: (String) -> Unit,
    addAdminRights: (Boolean) -> Unit,
    addOtherInfo: (String) -> Unit,
    addPersonnel: (personnel: PersonnelEntity) -> Unit,
    onTakePhoto: () -> Unit,
    navigateToBottomNav: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var confirmedPassword by remember {
        mutableStateOf(emptyString)
    }
    var userNameError by remember {
        mutableStateOf(false)
    }
    var firstNameError by remember {
        mutableStateOf(false)
    }
    var lastNameError by remember {
        mutableStateOf(false)
    }
    var otherNameError by remember {
        mutableStateOf(false)
    }
    var openRolesDialog by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    var personnelRoles = UserPreferences(context).getPersonnelRoles.collectAsState(initial = null).value

    BasicScreenColumnWithoutBottomBar {
        // First Name
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisPersonnelFirstName by remember {
                mutableStateOf(personnel.firstName)
            }
            BasicTextFieldWithTrailingIconError(
                value = thisPersonnelFirstName,
                onValueChange = {
                    thisPersonnelFirstName = it
                    addFirstName(it)
                    firstNameError = textIsInvalid(it)
                },
                isError = firstNameError,
                readOnly = false,
                placeholder = PersonnelNamePlaceholder,
                label = EnterPersonnelFirstName,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        // Last Name
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisPersonnelLastName by remember {
                mutableStateOf(personnel.lastName)
            }
            BasicTextFieldWithTrailingIconError(
                value = thisPersonnelLastName,
                onValueChange = {
                    thisPersonnelLastName = it
                    lastNameError = textIsInvalid(it)
                    addLastName(it)
                },
                isError = lastNameError,
                readOnly = false,
                placeholder = PersonnelNamePlaceholder,
                label = EnterPersonnelLastName,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        // Other Name
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisPersonnelOtherNames by remember {
                mutableStateOf(personnel.otherNames)
            }
            BasicTextFieldWithTrailingIconError(
                value = thisPersonnelOtherNames.toNotNull(),
                onValueChange = {
                    thisPersonnelOtherNames = it
                    otherNameError =
                        if (thisPersonnelOtherNames.isNullOrEmpty()) false else textIsInvalid(
                            it
                        )
                    addOtherNames(it)
                },
                isError = otherNameError,
                readOnly = false,
                placeholder = PersonnelNamePlaceholder,
                label = EnterPersonnelOtherName,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        // Username
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisPersonnelUsername by remember {
                mutableStateOf(personnel.userName)
            }
            BasicTextFieldWithTrailingIconError(
                value = thisPersonnelUsername,
                onValueChange = {
                    thisPersonnelUsername = it
                    addUserName(it)
                    userNameError = textIsInvalid(it)
                },
                isError = userNameError,
                readOnly = false,
                placeholder = PersonnelNamePlaceholder,
                label = EnterPersonnelUserName,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        // Contact
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisPersonnelContact by remember {
                mutableStateOf(personnel.contact)
            }
            BasicTextFieldWithTrailingIconError(
                value = thisPersonnelContact,
                onValueChange = {
                    thisPersonnelContact = it
                    addContact(it)
                },
                isError = false,
                readOnly = false,
                placeholder = PersonnelContactPlaceholder,
                label = EnterPersonnelContact,
                icon = R.drawable.ic_contact,
                keyboardType = KeyboardType.Phone
            )
        }

        // PersonnelRole
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisPersonnelRole by remember {
                mutableStateOf(personnel.role.toNotNull())
            }
            val roles = personnelRoles.toPersonnelRoles()
            AutoCompleteWithAddButton(
                label = EnterPersonnelRole,
                placeholder = PersonnelRolePlaceholder,
                listItems = roles.map { it.personnelRole },
                readOnly = false,
                expandedIcon = R.drawable.ic_role,
                unexpandedIcon = R.drawable.ic_role,
                onClickAddButton = { openRolesDialog = !openRolesDialog },
                getSelectedItem = { _role ->
                    thisPersonnelRole = _role.trim()
                    addRole(thisPersonnelRole.toNotNull())
                }
            )
        }

        // Photo
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            PhotoTextField {
                onTakePhoto()
            }
        }

        // Company Password
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var password by remember {
                mutableStateOf(personnel.password.toNotNull())
            }
            PasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    addPassword(password)
                },
                placeholder = emptyString,
                label = EnterPassword,
                keyboardType = KeyboardType.Password
            )
        }

        // Confirm Company Password
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            PasswordTextField(
                value = confirmedPassword,
                onValueChange = {
                    confirmedPassword = it
                },
                placeholder = emptyString,
                label = ConfirmPassword,
                keyboardType = KeyboardType.Password
            )
        }

        // Admin Rights?
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisPersonnelHasAdminRights by remember {
                mutableStateOf(personnel.hasAdminRights)
            }
            AutoCompleteTextField(
                label = DoesPersonnelHaveAdminRights,
                placeholder = DoesPersonnelHaveAdminRights,
                readOnly = true,
                expandedIcon = R.drawable.ic_admin,
                unexpandedIcon = R.drawable.ic_admin,
                listItems = listOf("Yes", "No"),
                getSelectedItem = { _adminRights ->
                    thisPersonnelHasAdminRights = _adminRights == "Yes"
                    addAdminRights(thisPersonnelHasAdminRights!!)
                }
            )
        }

        // Any Other Description
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var thisAnyOtherInfo by remember {
                mutableStateOf(personnel.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = thisAnyOtherInfo,
                onValueChange = {
                    thisAnyOtherInfo = it
                    addOtherInfo(it) },
                placeholder = PersonnelDescription,
                label = PersonnelDescription,
                icon = R.drawable.ic_short_notes,
                keyboardType = KeyboardType.Text
            )
        }

        // Save
        Box(
            modifier = Modifier.padding(
                vertical = LocalSpacing.current.smallMedium,
                horizontal = LocalSpacing.current.small,
            ),
            contentAlignment = Alignment.Center
        ) {
            BasicButton(buttonName = SavePersonnel) {
                when(true){
                    userNameError->{
                        Toast.makeText(context, "${personnel.userName} is not valid", Toast.LENGTH_LONG).show()
                    }
                    firstNameError->{
                        Toast.makeText(context, "${personnel.firstName} is not valid", Toast.LENGTH_LONG).show()
                    }
                    lastNameError->{
                        Toast.makeText(context, "${personnel.lastName} is not valid", Toast.LENGTH_LONG).show()
                    }
                    otherNameError->{
                        Toast.makeText(context, "${personnel.otherNames} is not valid", Toast.LENGTH_LONG).show()
                    }
                    (personnel.password != confirmedPassword)->{
                        Toast.makeText(context, "Password is not valid", Toast.LENGTH_LONG).show()
                    }
                    (personnel.password.length < 4)->{
                        Toast.makeText(context, "Password is not too short", Toast.LENGTH_LONG).show()
                    }
                    (personnel.userName.isBlank()) ->{
                        Toast.makeText(context, "Please enter personnel username", Toast.LENGTH_LONG).show()
                    }
                    (personnel.contact.isBlank()) ->{
                        Toast.makeText(context, "Please enter personnel contact", Toast.LENGTH_LONG).show()
                    }
                    (personnel.role.isNullOrBlank())->{
                        Toast.makeText(context, "Please select personnel role", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val uniquePersonnelId = generateUniquePersonnelId("${personnel.firstName} ${personnel.lastName}")
                        addPersonnel(personnel.copy(uniquePersonnelId = uniquePersonnelId))
                        confirmationInfoDialog = !confirmationInfoDialog
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
                if (roles.map { it.personnelRole.trim().lowercase(Locale.ROOT) }.contains(_newRole.trim().lowercase(Locale.ROOT))) {
                    Toast.makeText(context, "PersonnelRole: $_newRole already exists", Toast.LENGTH_LONG).show()
                    openRolesDialog = false
                } else {
                    mutablePersonnelRoles.addAll(roles)
                    mutablePersonnelRoles.add(newPersonnelRole)
                    personnelRoles = mutablePersonnelRoles.toPersonnelRolesJson()
                    coroutineScope.launch {
                        UserPreferences(context).savePersonnelRoles(personnelRoles ?: emptyString)
                    }
                    Toast.makeText(context, "PersonnelRole: $_newRole successfully added", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            openRolesDialog = false
        }

    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingPersonnel,
        title = null,
        textContent = personnelSavingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (personnelSavingIsSuccessful){
            navigateToBottomNav()
        }
        confirmationInfoDialog = false
    }
}
