package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.No
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.Yes
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.PasswordResetSuccessful
import com.example.myshopmanagerapp.core.FormRelatedString.PasswordResetUnsuccessful
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelContact
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelFirstName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelHasAdminRights
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelInformation
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelLastName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelOtherNames
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRole
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.ResetPassword
import com.example.myshopmanagerapp.core.FormRelatedString.ResetPasswordMessage
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ViewPersonnelContent(
    personnel: PersonnelEntity,
    passwordResetMessage: String,
    resetPassword: (String)-> Unit,
){
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val thisPersonnel = userPreferences.getPersonnelInfo.collectAsState(initial = emptyString).value ?: emptyString
    val thisPersonnelHasAdminRights = thisPersonnel.toPersonnelEntity()?.hasAdminRights ?: false
    var openResetPasswordDialog by remember {
        mutableStateOf(false)
    }
    var openResetConfirmationDialog by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar {
        // Personnel Photo
        ViewPhoto(icon = R.drawable.ic_personnel)

        HorizontalDivider()

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewInfo(info = PersonnelInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewTextValueRow(
            viewTitle = PersonnelFirstName,
            viewValue = personnel.firstName
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = PersonnelLastName,
            viewValue = personnel.lastName
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = PersonnelOtherNames,
            viewValue = personnel.otherNames ?: NotAvailable
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = PersonnelContact,
            viewValue = personnel.contact
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = PersonnelContact,
            viewValue = personnel.contact
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = PersonnelRole,
            viewValue = personnel.role ?: NotAvailable
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = PersonnelHasAdminRights,
            viewValue = if (personnel.hasAdminRights == true) Yes else No
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = PersonnelShortNotes,
            viewValue = personnel.otherInfo ?: NotAvailable
        )

        HorizontalDivider()

        if (thisPersonnelHasAdminRights) {
            Box(modifier = Modifier.padding(LocalSpacing.current.smallMedium)) {
                BasicButton(buttonName = ResetPassword) {
                    openResetPasswordDialog = !openResetPasswordDialog
                }
            }
        }
    }

    DeleteConfirmationDialog(
        openDialog = openResetPasswordDialog,
        title = ResetPassword,
        textContent = ResetPasswordMessage,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            resetPassword(personnel.uniquePersonnelId)
            openResetConfirmationDialog = !openResetConfirmationDialog
        }
    ) {
        openResetPasswordDialog = false
    }

    ConfirmationInfoDialog(
        openDialog = openResetConfirmationDialog,
        isLoading = false,
        title = emptyString,
        textContent = passwordResetMessage,
        unconfirmedDeletedToastText = PasswordResetUnsuccessful,
        confirmedDeleteToastText = PasswordResetSuccessful
    ) {
        openResetConfirmationDialog = !openResetConfirmationDialog
    }

}