package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_personnel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPassword
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelUserName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelNamePlaceholder
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldWithTrailingIconError
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.PasswordTextField
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun LoginPersonnelContent(
    isLoggingIn: Boolean,
    LoginMessage: String?,
    loginSuccessful: Boolean,
    login: (String, String) -> Unit,
    createNewAccount: () -> Unit,
    navigateToBottomNav: () -> Unit,
) {
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var userName by remember {
            mutableStateOf(emptyString)
        }
        var password by remember {
            mutableStateOf(emptyString)
        }

        Column(modifier = Modifier.weight(4f)
            .fillMaxWidth()
            .padding(LocalSpacing.current.medium)
        ) {

            Spacer(modifier = Modifier.height(LocalSpacing.current.semiLarge))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(LocalSpacing.current.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(LocalSpacing.current.small),
                    painter = painterResource(id = R.drawable.ic_personnel),
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.large))

            // Personnel UserName Email
            Box(
                modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                BasicTextFieldWithTrailingIconError(
                    value = userName,
                    onValueChange = {
                        userName = it
                    },
                    isError = false,
                    readOnly = false,
                    placeholder = PersonnelNamePlaceholder,
                    label = EnterPersonnelUserName,
                    icon = R.drawable.ic_personnel,
                    keyboardType = KeyboardType.Text
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

            // Personnel Password
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

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

            // Login button
            Box(
                modifier = Modifier.padding(LocalSpacing.current.small)
                    .fillMaxWidth()
                    .height(LocalSpacing.current.textFieldHeight)
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
                    .border(LocalSpacing.current.extraSmall, MaterialTheme.colorScheme.onPrimary, MaterialTheme.shapes.large)
                    .clickable {
                        login(userName, password)
                        confirmationInfoDialog = !confirmationInfoDialog
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Login",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier.padding(LocalSpacing.current.smallMedium)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Forgot Password?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth()
            .weight(1f)
            .padding(LocalSpacing.current.medium),
            contentAlignment = Alignment.BottomCenter
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalSpacing.current.buttonHeight)
                        .border(LocalSpacing.current.extraSmall, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
                        .padding(LocalSpacing.current.extraSmall)
                        .clickable { createNewAccount() }
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create New Personnel",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
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
            navigateToBottomNav()
        }
        confirmationInfoDialog = false
    }

}
