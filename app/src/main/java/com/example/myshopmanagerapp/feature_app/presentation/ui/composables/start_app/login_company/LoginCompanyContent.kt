package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.login_company

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyEmailPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyEmail
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPassword
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun LoginCompanyContent(
    login: (String, String) -> Unit,
    createNewAccount: () -> Unit,
    restartApp: () -> Unit,
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val loginMessage = userPreferences.getRepositoryJobMessage.collectAsState(initial = emptyString).value
    val loginIsSuccessful = userPreferences.getRepositoryJobSuccessState.collectAsState(initial = false).value ?: false

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var email by remember {
            mutableStateOf(emptyString)
        }
        var password by remember {
            mutableStateOf(emptyString)
        }

        Column(modifier = Modifier
            .weight(3f)
            .fillMaxWidth()
            .padding(LocalSpacing.current.medium)
        ) {


            Spacer(modifier = Modifier.height(LocalSpacing.current.large))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(LocalSpacing.current.medium),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(150.dp)
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

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

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

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

            // Login button
            Box(
                modifier = Modifier
                    .padding(LocalSpacing.current.small)
                    .fillMaxWidth()
                    .height(LocalSpacing.current.textFieldHeight)
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
                    .border(
                        LocalSpacing.current.extraSmall,
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.shapes.large
                    )
                    .clickable {
                        login(email, password)
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
                modifier = Modifier
                    .padding(LocalSpacing.current.smallMedium)
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
        Box(modifier = Modifier
            .fillMaxWidth()
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
                        .border(
                            LocalSpacing.current.extraSmall,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.large
                        )
                        .padding(LocalSpacing.current.extraSmall)
                        .clickable { createNewAccount() }
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create New Account",
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
        isLoading = false,
        title = null,
        textContent = loginMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (loginIsSuccessful){
            restartApp()
        }
        confirmationInfoDialog = false
    }

}
