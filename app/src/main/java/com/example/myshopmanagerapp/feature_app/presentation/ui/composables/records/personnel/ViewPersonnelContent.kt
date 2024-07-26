package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.Contact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelFirstName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelLastName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelOtherName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelUserName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelHasAdminRights
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelIsActive
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRole
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelRoles
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.domain.model.PersonnelRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun ViewPersonnelContent(
    personnel: PersonnelEntity,
    updateFirstName: (String)-> Unit,
    updateContact: (String)-> Unit,
    updateLastName: (String)-> Unit,
    updateOtherNames: (String)-> Unit,
    updateUsername: (String)-> Unit,
    updatePersonnelRole: (String)-> Unit,
    updateOtherInfo: (String)-> Unit,
    updatePersonnelHasAdminRights: (Boolean) -> Unit,
    updatePersonnelIsActive: (Boolean) -> Unit,
    updateIsSuccessful: Boolean,
    updatePersonnelMessage: String,
    isUpdatingPersonnel: Boolean,
    reloadPersonnel: () -> Unit,
){
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val thisPersonnel = userPreferences.getPersonnelInfo.collectAsState(initial = emptyString).value ?: emptyString
    val isPrincipalAdmin = personnel.isPrincipalAdmin
    val thisPersonnelUniqueId = thisPersonnel.toPersonnelEntity()?.uniquePersonnelId
    val canBeUpdated = thisPersonnelUniqueId == personnel.uniquePersonnelId

    var openConfirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    // val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90

    val descriptionColor = if (isSystemInDarkTheme()) Grey70 else Grey40
    val titleColor = if (isSystemInDarkTheme()) Grey99 else Grey10

    val greenBackground = if (isSystemInDarkTheme()) Grey15 else Green95
    val greenContentLight = if (isSystemInDarkTheme()) Grey70 else Green30
    val greenContent = if (isSystemInDarkTheme()) Grey99 else Green20


    var updateConfirmationInfo by remember {
        mutableStateOf(false)
    }
    var openRolesDialog by remember {
        mutableStateOf(false)
    }
    var confirmationInfoMessage by remember {
        mutableStateOf(emptyString)
    }
    var personnelRoles = UserPreferences(context).getPersonnelRoles.collectAsState(initial = null).value

    BasicScreenColumnWithoutBottomBar {

        val userName = personnel.userName.toEllipses(30)
        val contact = "Contact: ${personnel.contact}"
        val uniqueId = "Id: ${personnel.uniquePersonnelId}"
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .height(200.dp)
            .padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            InfoDisplayCard(
                image = R.drawable.personnel,
                imageWidth = 75.dp,
                bigText = userName,
                bigTextSize = 20.sp,
                smallTextFontWeight = FontWeight.Normal,
                smallText = "${contact.toEllipses(30)}\n${uniqueId.toEllipses(40)}",
                smallTextSize = 15.sp,
                smallTextColor = descriptionColor,
                backgroundColor = MaterialTheme.colorScheme.background,
                elevation = LocalSpacing.current.noElevation,
                isAmount = false
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
                    if (canBeUpdated || isPrincipalAdmin) {
                        updateFirstName(it)
                        updateConfirmationInfo = !updateConfirmationInfo
                    }else{
                        confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                        openConfirmationInfoDialog = !openConfirmationInfoDialog
                    }
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
                    if (canBeUpdated || isPrincipalAdmin) {
                        updateLastName(it)
                        updateConfirmationInfo = !updateConfirmationInfo
                    }else{
                        confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                        openConfirmationInfoDialog = !openConfirmationInfoDialog
                    }
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
                    if (canBeUpdated || isPrincipalAdmin) {
                        updateOtherNames(it)
                        updateConfirmationInfo = !updateConfirmationInfo
                    }else{
                        confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                        openConfirmationInfoDialog = !openConfirmationInfoDialog
                    }
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
                    if (canBeUpdated || isPrincipalAdmin) {
                        updateUsername(it)
                        updateConfirmationInfo = !updateConfirmationInfo
                    }else{
                        confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                        openConfirmationInfoDialog = !openConfirmationInfoDialog
                    }
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
                .height(130.dp)
                .padding(
                    horizontal = LocalSpacing.current.small,
                    vertical = LocalSpacing.current.medium,
                ),
                contentAlignment = Alignment.Center
            ){
                val isAdmin = personnel.hasAdminRights == true
                InfoDisplayCard(
                    image = if (isAdmin) R.drawable.ic_check else R.drawable.ic_cancel,
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
                    isAmount = false,
                    isBoolean = true,
                    isChecked = isAdmin,
                    isEnabled = true,
                    getCheckedValue = {
                        if (canBeUpdated || isPrincipalAdmin) {
                            updatePersonnelHasAdminRights(it)
                            updateConfirmationInfo = !updateConfirmationInfo
                        }else{
                            confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                            openConfirmationInfoDialog = !openConfirmationInfoDialog
                        }
                    }
                )
            }

            Box(modifier = Modifier
                .background(Color.Transparent)
                .weight(1f)
                .height(130.dp)
                .padding(
                    horizontal = LocalSpacing.current.small,
                    vertical = LocalSpacing.current.medium
                ),
                contentAlignment = Alignment.Center
            ){
                val isActive = personnel.isActive == true
                InfoDisplayCard(
                    image = if (isActive) R.drawable.ic_check else R.drawable.ic_cancel,
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
                    isAmount = false,
                    isBoolean = true,
                    isChecked = isActive,
                    isEnabled = true,
                    getCheckedValue = {
                        if (isPrincipalAdmin) {
                            updatePersonnelIsActive(it)
                            updateConfirmationInfo = !updateConfirmationInfo
                        }else{
                            confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                            openConfirmationInfoDialog = !openConfirmationInfoDialog
                        }
                    }
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
                    label = FormRelatedString.EnterPersonnelRole,
                    placeholder = FormRelatedString.PersonnelRolePlaceholder,
                    textFieldIcon = R.drawable.ic_edit,
                    isAutoCompleteTextField = true,
                    listItems = personnelRoles.toPersonnelRoles().map { it.personnelRole },
                    addNewItem = { openRolesDialog = !openRolesDialog },
                    getUpdatedValue = {
                        if (canBeUpdated || isPrincipalAdmin) {
                            updatePersonnelRole(it)
                            updateConfirmationInfo = !updateConfirmationInfo
                        }else{
                            confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                            openConfirmationInfoDialog = !openConfirmationInfoDialog
                        }
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
                VerticalDisplayAndEditTextValues(
                    firstText = Contact,
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
                        if (canBeUpdated || isPrincipalAdmin) {
                            updateContact(it)
                            updateConfirmationInfo = !updateConfirmationInfo
                        }else{
                            confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                            openConfirmationInfoDialog = !openConfirmationInfoDialog
                        }
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
                    firstText = FormRelatedString.ShortNotes,
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
                        if (canBeUpdated || isPrincipalAdmin) {
                            updateOtherInfo(it)
                            updateConfirmationInfo = !updateConfirmationInfo
                        }else{
                            confirmationInfoMessage = "You do not have administrative rights to update this personnel's info"
                            openConfirmationInfoDialog = !openConfirmationInfoDialog
                        }
                    }
                )
            }
        }

    }

    BasicTextFieldAlertDialog(
        openDialog = openRolesDialog,
        title = "Add Personnel Role",
        textContent = emptyString,
        placeholder = "Eg: Manager",
        label = "Add personnel role",
        icon = R.drawable.ic_role,
        keyboardType = KeyboardType.Text,
        unconfirmedUpdatedToastText = "Personnel role not added",
        confirmedUpdatedToastText = null,
        getValue = { _newRole ->
            val newPersonnelRole = PersonnelRole(_newRole)
            val roles = personnelRoles.toPersonnelRoles()
            val mutablePersonnelRoles = mutableListOf<PersonnelRole>()
            if (roles.map { it.personnelRole.trim().lowercase(Locale.ROOT) }.contains(_newRole.trim().lowercase(
                    Locale.ROOT))) {
                Toast.makeText(context, "Personnel role: $_newRole already exists", Toast.LENGTH_LONG).show()
                openRolesDialog = false
            } else {
                mutablePersonnelRoles.addAll(roles)
                mutablePersonnelRoles.add(newPersonnelRole)
                personnelRoles = Functions.toRolesJson(mutablePersonnelRoles)
                coroutineScope.launch {
                    UserPreferences(context).savePersonnelRoles(personnelRoles ?: emptyString)
                }
                Toast.makeText(context, "Personnel role: $_newRole successfully added", Toast.LENGTH_LONG).show()
            }
        }
    ) {
        openRolesDialog = false
    }

    ConfirmationInfoDialog(
        openDialog = openConfirmationInfoDialog,
        isLoading = false,
        title = null,
        textContent = confirmationInfoMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        openConfirmationInfoDialog = false
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