package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.change_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.ConfirmPassword
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCurrentPassword
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNewPassword
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ChangePasswordContent(
    changePasswordMessage: String?,
    changePasswordIsSuccessful: Boolean,
    changePassword: (String, String, String) -> Unit,
    navigateToProfileScreen: () -> Unit,
) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar {
        var currentPassword by remember {
            mutableStateOf(emptyString)
        }
        var newPassword by remember {
            mutableStateOf(emptyString)
        }
        var confirmedPassword by remember {
            mutableStateOf(emptyString)
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(LocalSpacing.current.medium),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LocalSpacing.current.small),
                painter = painterResource(id = R.drawable.shop),
                contentDescription = emptyString
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.large))

        // Current Password
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            PasswordTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                },
                placeholder = emptyString,
                label = EnterCurrentPassword,
                keyboardType = KeyboardType.Password
            )
        }


        Spacer(modifier = Modifier.height(LocalSpacing.current.large))

        // New Password
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            PasswordTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                },
                placeholder = emptyString,
                label = EnterNewPassword,
                keyboardType = KeyboardType.Password
            )
        }


        Spacer(modifier = Modifier.height(LocalSpacing.current.large))

        // Confirm Password
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
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


        Spacer(modifier = Modifier.height(LocalSpacing.current.large))

        // Save button
        Box(
            modifier = Modifier.padding(
                vertical = LocalSpacing.current.smallMedium,
                horizontal = LocalSpacing.current.small,
            ),
            contentAlignment = Alignment.Center
        ) {
            BasicButton(buttonName = "Change Password") {
                changePassword(currentPassword, newPassword, confirmedPassword)
                confirmationInfoDialog = !confirmationInfoDialog
            }

        }

    }
    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = changePasswordMessage.isNullOrEmpty(),
        title = null,
        textContent = changePasswordMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (changePasswordIsSuccessful){
            navigateToProfileScreen()
        }
        confirmationInfoDialog = false
    }
}
