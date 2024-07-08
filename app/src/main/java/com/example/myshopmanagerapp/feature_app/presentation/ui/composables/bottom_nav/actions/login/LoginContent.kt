package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyEmailPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyEmail
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPassword
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Blue40
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Blue90
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun LoginContent(
    isLoggingIn: Boolean,
    isLoggedIn: Boolean,
    logoutMessage: String,
    LoginMessage: String?,
    loginSuccessful: Boolean,
    login: (String, String) -> Unit,
    openSignUpPage: () -> Unit,
    logout: () -> Unit,
    navigateToProfileScreen: () -> Unit,
) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var logoutConfirmation by remember {
        mutableStateOf(false)
    }
    if (isLoggedIn) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalSpacing.current.noPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(LocalSpacing.current.large))

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LocalSpacing.current.large),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You are already logged in",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(modifier = Modifier
                    .padding(LocalSpacing.current.extraSmall)
                    .clickable { logoutConfirmation = !logoutConfirmation },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Log out to register or sign in to a different account",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSystemInDarkTheme()) Blue90 else Blue40,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        maxLines = 2,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

        }
    }else {
        BasicScreenColumnWithoutBottomBar {
            var email by remember {
                mutableStateOf(emptyString)
            }
            var password by remember {
                mutableStateOf(emptyString)
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.large))

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

            // Company Email
            Box(
                modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                BasicTextFieldWithTrailingIconError(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    isError = false,
                    readOnly = false,
                    placeholder = CompanyEmailPlaceholder,
                    label = EnterCompanyEmail,
                    icon = R.drawable.ic_email,
                    keyboardType = KeyboardType.Email
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.large))

            // Company Password
            Box(
                modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                PasswordTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    placeholder = emptyString,
                    label = EnterPassword,
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
                BasicButton(buttonName = "Login") {
                    login(email, password)
                    confirmationInfoDialog = !confirmationInfoDialog
                }

            }

            // Sign up instead
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Don't have an account yet?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(LocalSpacing.current.extraSmall)
                        .clickable { openSignUpPage() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sign up",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSystemInDarkTheme()) Blue90 else Blue40,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        maxLines = 1,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

        }
    }
    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isLoggingIn,
        title = null,
        textContent = LoginMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (loginSuccessful){
            navigateToProfileScreen()
        }
        confirmationInfoDialog = false
    }
    DeleteConfirmationDialog(
        openDialog = logoutConfirmation,
        title = "Logout",
        textContent = "Are you sure you want to logout?",
        unconfirmedDeletedToastText = "You did not log out",
        confirmedDeleteToastText = logoutMessage,
        confirmDelete = { logout() }) {
        logoutConfirmation = false
    }


}
