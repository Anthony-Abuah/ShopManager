package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company

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
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyEmailPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ConfirmPassword
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyEmail
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPassword
import com.example.myshopmanagerapp.core.FormRelatedString.SaveCompany
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun RegisterCompanyPasswordContent(
    company: CompanyEntity,
    companySavingMessage: String?,
    companySavingIsSuccessful: Boolean,
    addCompanyEmail: (String) -> Unit,
    addCompanyPassword: (String) -> Unit,
    addPasswordConfirmation: (String) -> Unit,
    addCompany: (CompanyEntity) -> Unit,
    navigateToRegisterPersonnelScreen: () -> Unit,
) {

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        Spacer(modifier = Modifier.height(LocalSpacing.current.large))
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(LocalSpacing.current.small),
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
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var email by remember {
                mutableStateOf(company.email.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = email,
                onValueChange = {
                    email = it
                    addCompanyEmail(email)
                },
                isError = false,
                readOnly = false,
                placeholder = CompanyEmailPlaceholder,
                label = EnterCompanyEmail,
                icon = R.drawable.ic_email,
                keyboardType = KeyboardType.Email
            )
        }

        // Company Password
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var password by remember {
                mutableStateOf(company.password.toNotNull())
            }
            PasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    addCompanyPassword(password)
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
            var confirmedPassword by remember {
                mutableStateOf(emptyString)
            }
            PasswordTextField(
                value = confirmedPassword,
                onValueChange = {
                    confirmedPassword = it
                    addPasswordConfirmation(confirmedPassword)
                },
                placeholder = emptyString,
                label = ConfirmPassword,
                keyboardType = KeyboardType.Password
            )
        }

        // Save button
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            BasicButton(buttonName = SaveCompany) {
                if (!(company.email.isEmpty() || company.password.isEmpty())) {
                    addCompany(company)
                    confirmationInfoDialog = !confirmationInfoDialog
                }
            }

        }

    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = companySavingMessage.isNullOrEmpty(),
        title = null,
        textContent = companySavingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (companySavingIsSuccessful){
            navigateToRegisterPersonnelScreen()
        }
        confirmationInfoDialog = false
    }

}
