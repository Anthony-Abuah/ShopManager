package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.No
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.Yes
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
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelHasAdminRights
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelIsActive
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRole
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRolePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotesPlaceholder
import com.example.myshopmanagerapp.core.Functions
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRoles
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.domain.model.PersonnelRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
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

    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90

    val shadowColor = if (isSystemInDarkTheme()) Grey5 else Grey80
    val descriptionColor = if (isSystemInDarkTheme()) Grey70 else Grey40
    val titleColor = if (isSystemInDarkTheme()) Grey99 else Grey10

    val greenBackground = if (isSystemInDarkTheme()) Green5 else Green95
    val greenContentLight = if (isSystemInDarkTheme()) Grey70 else Green30
    val greenContent = if (isSystemInDarkTheme()) Grey99 else Green20

    val logoutBackground = if (isSystemInDarkTheme()) Red5 else Red95
    val logoutContentLight = if (isSystemInDarkTheme()) Grey70 else Red30
    val logoutContent = if (isSystemInDarkTheme()) Grey99 else Red20


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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
            .padding(LocalSpacing.current.noPadding)
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = LocalSpacing.current.small)
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            val userName = personnel.userName.toEllipses(30)
            val contact = "Contact: ${personnel.contact}"
            val uniqueId = "Id: ${personnel.uniquePersonnelId}"
            Box(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(200.dp)
                .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                InfoDisplayCard(
                    icon = R.drawable.personnel,
                    imageWidth = 75.dp,
                    bigText = userName,
                    bigTextSize = 20.sp,
                    smallTextFontWeight = FontWeight.Normal,
                    smallText = "${contact.toEllipses(30)}\n${uniqueId.toEllipses(40)}",
                    smallTextSize = 15.sp,
                    smallTextColor = descriptionColor,
                    backgroundColor = cardBackgroundColor,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }
        }

        Box(
            modifier = Modifier
                .background(alternateBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(
                    horizontal = LocalSpacing.current.smallMedium,
                    vertical = LocalSpacing.current.default,
                ),
                leadingIcon = null,
                firstText = "Personal Information",
                firstTextSize = 14.sp,
                secondText = emptyString,
                readOnly = true
            )
        }

        Box(
            modifier = Modifier
                .background(mainBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                firstText = personnel.firstName,
                secondText = "First name",
                secondTextFontWeight = FontWeight.Normal,
                secondTextColor = MaterialTheme.colorScheme.primary,
                value = personnel.firstName,
                trailingIcon = R.drawable.ic_keyboard_arrow_right,
                trailingIconWidth = 32.dp,
                onBackgroundColor = titleColor,
                label = EnterPersonnelFirstName,
                placeholder = PersonnelNamePlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                getUpdatedValue = {
                    updateFirstName(it)
                    updateConfirmationInfo = !updateConfirmationInfo
                }
            )
        }


        Box(
            modifier = Modifier
                .background(alternateBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                firstText = personnel.lastName,
                secondText = "Last name",
                secondTextFontWeight = FontWeight.Normal,
                secondTextColor = MaterialTheme.colorScheme.primary,
                value = personnel.lastName,
                trailingIcon = R.drawable.ic_keyboard_arrow_right,
                trailingIconWidth = 32.dp,
                onBackgroundColor = titleColor,
                label = EnterPersonnelLastName,
                placeholder = PersonnelNamePlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                getUpdatedValue = {
                    updateLastName(it)
                    updateConfirmationInfo = !updateConfirmationInfo
                }
            )
        }

        Box(
            modifier = Modifier
                .background(mainBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                firstText = personnel.otherNames.toNotNull().ifBlank { NotAvailable },
                secondText = "Other names",
                secondTextFontWeight = FontWeight.Normal,
                secondTextColor = MaterialTheme.colorScheme.primary,
                value = personnel.otherNames.toNotNull(),
                trailingIcon = R.drawable.ic_keyboard_arrow_right,
                trailingIconWidth = 32.dp,
                onBackgroundColor = titleColor,
                label = EnterPersonnelOtherName,
                placeholder = PersonnelNamePlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                getUpdatedValue = {
                    updateOtherNames(it)
                    updateConfirmationInfo = !updateConfirmationInfo
                }
            )
        }

        Box(
            modifier = Modifier
                .background(alternateBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                firstText = personnel.userName.toNotNull().ifBlank { NotAvailable },
                secondText = "User name",
                secondTextFontWeight = FontWeight.Normal,
                secondTextColor = MaterialTheme.colorScheme.primary,
                value = personnel.userName,
                trailingIcon = R.drawable.ic_keyboard_arrow_right,
                trailingIconWidth = 32.dp,
                onBackgroundColor = titleColor,
                label = EnterPersonnelUserName,
                placeholder = PersonnelNamePlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                getUpdatedValue = {
                    updateUsername(it)
                    updateConfirmationInfo = !updateConfirmationInfo
                }
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = LocalSpacing.current.noPadding)
                .background(mainBackgroundColor),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Box(modifier = Modifier
                .background(Color.Transparent)
                .weight(1f)
                .height(120.dp)
                .padding(
                    horizontal = LocalSpacing.current.small,
                    vertical = LocalSpacing.current.medium,
                ),
                contentAlignment = Alignment.Center
            ){
                val isAdmin = personnel.hasAdminRights == true
                InfoDisplayCard(
                    icon = if (isAdmin) R.drawable.ic_check else R.drawable.ic_cancel,
                    imageWidth = 32.dp,
                    bigText = if (isAdmin) Yes else No,
                    bigTextSize = 18.sp,
                    bigTextColor = greenContent,
                    smallTextFontWeight = FontWeight.SemiBold,
                    smallText = PersonnelHasAdminRights,
                    smallTextSize = 14.sp,
                    smallTextColor = greenContentLight,
                    backgroundColor = greenBackground,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }

            Box(modifier = Modifier
                .background(Color.Transparent)
                .weight(1f)
                .height(120.dp)
                .padding(
                    horizontal = LocalSpacing.current.small,
                    vertical = LocalSpacing.current.medium
                ),
                contentAlignment = Alignment.Center
            ){
                val isActive = personnel.isActive == true
                InfoDisplayCard(
                    icon = if (isActive) R.drawable.ic_check else R.drawable.ic_cancel,
                    imageWidth = 32.dp,
                    bigText = if (isActive) Yes else No,
                    bigTextSize = 18.sp,
                    bigTextColor = greenContent,
                    smallTextFontWeight = FontWeight.SemiBold,
                    smallText = PersonnelIsActive,
                    smallTextSize = 14.sp,
                    smallTextColor = greenContentLight,
                    backgroundColor = greenBackground,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }
        }


        Column(
            modifier = Modifier
                .padding(vertical = LocalSpacing.current.noPadding)
                .background(mainBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Box(
                modifier = Modifier
                    .background(alternateBackgroundColor)
                    .padding(LocalSpacing.current.noPadding)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HorizontalDisplayAndEditTextValues(
                    modifier = Modifier.padding(
                        horizontal = LocalSpacing.current.smallMedium,
                        vertical = LocalSpacing.current.default,
                    ),
                    leadingIcon = null,
                    firstText = "Other Information",
                    firstTextSize = 14.sp,
                    secondText = emptyString,
                    readOnly = true
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(LocalSpacing.current.default)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                VerticalDisplayAndEditTextValues(
                    firstText = PersonnelRole,
                    firstTextColor = titleColor,
                    secondText = personnel.role ?: NotAvailable,
                    secondTextColor = descriptionColor,
                    value = personnel.role.toNotNull(),
                    leadingIcon = R.drawable.ic_role,
                    trailingIconWidth = 30.dp,
                    onBackgroundColor = titleColor,
                    label = EnterPersonnelRole,
                    placeholder = PersonnelRolePlaceholder,
                    textFieldIcon = R.drawable.ic_edit,
                    isAutoCompleteTextField = true,
                    listItems = personnelRoles.toPersonnelRoles().map { it.personnelRole },
                    addNewItem = { openRolesDialog = !openRolesDialog },
                    getUpdatedValue = {
                        updatePersonnelRole(it)
                        updateConfirmationInfo = !updateConfirmationInfo
                    }
                )
            }

            HorizontalDivider()

            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(LocalSpacing.current.default,)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                VerticalDisplayAndEditTextValues(
                    firstText = PersonnelContact,
                    firstTextColor = titleColor,
                    secondText = personnel.contact,
                    secondTextColor = descriptionColor,
                    value = personnel.contact.toNotNull(),
                    leadingIcon = R.drawable.ic_contact,
                    leadingIconWidth = 32.dp,
                    onBackgroundColor = titleColor,
                    keyboardType = KeyboardType.Phone,
                    label = EnterPersonnelContact,
                    placeholder = PersonnelContactPlaceholder,
                    textFieldIcon = R.drawable.ic_edit,
                    getUpdatedValue = {
                        updateContact(it)
                        updateConfirmationInfo = !updateConfirmationInfo
                    }
                )
            }

            HorizontalDivider()

            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(LocalSpacing.current.default)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val otherInfo = personnel.otherInfo
                VerticalDisplayAndEditTextValues(
                    firstText = ShortNotes,
                    firstTextColor = titleColor,
                    secondText = if (otherInfo.isNullOrBlank()) NotAvailable else otherInfo,
                    secondTextColor = descriptionColor,
                    value = personnel.otherInfo.toNotNull(),
                    leadingIcon = R.drawable.ic_short_notes,
                    leadingIconWidth = 32.dp,
                    onBackgroundColor = titleColor,
                    label = EnterShortDescription,
                    placeholder = ShortNotesPlaceholder,
                    textFieldIcon = R.drawable.ic_edit,
                    getUpdatedValue = {
                        updateOtherInfo(it)
                        updateConfirmationInfo = !updateConfirmationInfo
                    }
                )
            }
            HorizontalDivider()
        }

        Column(
            modifier = Modifier
                .padding(vertical = LocalSpacing.current.noPadding)
                .background(mainBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ){
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(
                        vertical = LocalSpacing.current.medium,
                        horizontal = LocalSpacing.current.default
                    )
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                HomeCard(
                    title = "Logout",
                    description = "Click here to log out",
                    icon = R.drawable.ic_logout,
                    titleColor = logoutContent,
                    descriptionColor = logoutContentLight,
                    cardContainerColor = logoutBackground,
                    cardShadowColor = shadowColor
                ) { confirmLogout = !confirmLogout }
            }
        }
    }

    /*
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
                updateConfirmationInfo = !updateConfirmationInfo }
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
    */

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