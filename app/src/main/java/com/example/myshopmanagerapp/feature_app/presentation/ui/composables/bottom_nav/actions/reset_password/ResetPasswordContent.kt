package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.reset_password

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyEmailPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ConfirmPassword
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyEmail
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNewPassword
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPersonnelPassword
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Grey40
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Grey70
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ResetPasswordContent(
    resetPasswordMessage: String?,
    resetPasswordIsSuccessful: Boolean,
    resetPassword: (String, String, String, String) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    val shopInfoJson = userPreferences.getShopInfo.collectAsState(initial = emptyString).value.toNotNull()
    val shopInfo = shopInfoJson.toCompanyEntity()
    val descriptionColor = if (isSystemInDarkTheme()) Grey70 else Grey40


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .height(200.dp)
                .padding(LocalSpacing.current.default),
            contentAlignment = Alignment.Center
        ) {
            val shopName = shopInfo?.companyName?.toNotNull().toEllipses(30)
            val contact = "Contact: ${shopInfo?.companyContact?.toNotNull().toEllipses(25)}"
            val location = "Location: ${shopInfo?.companyLocation?.toNotNull().toEllipses(25)}"
            InfoDisplayCard(
                image = R.drawable.shop,
                imageWidth = 75.dp,
                bigText = shopName,
                bigTextSize = 20.sp,
                smallTextFontWeight = FontWeight.Normal,
                smallText = "$contact\n$location",
                smallTextSize = 15.sp,
                smallTextColor = descriptionColor,
                backgroundColor = Color.Transparent,
                elevation = LocalSpacing.current.noElevation,
                isAmount = false
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            BasicScreenColumnWithoutBottomBar {
                var email by remember {
                    mutableStateOf(emptyString)
                }
                var newPassword by remember {
                    mutableStateOf(emptyString)
                }
                var confirmedPassword by remember {
                    mutableStateOf(emptyString)
                }

                var personnelPassword by remember {
                    mutableStateOf(emptyString)
                }

                Spacer(modifier = Modifier.height(LocalSpacing.current.textFieldHeight))

                // Company Email
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.smallMedium),
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


                Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

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


                Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

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

                Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

                // Personnel Password
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    PasswordTextField(
                        value = personnelPassword,
                        onValueChange = {
                            personnelPassword = it
                        },
                        placeholder = emptyString,
                        label = EnterPersonnelPassword,
                        keyboardType = KeyboardType.Password
                    )
                }


                Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

                // Save button
                Box(
                    modifier = Modifier.padding(
                        vertical = LocalSpacing.current.smallMedium,
                        horizontal = LocalSpacing.current.small,
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    BasicButton(buttonName = "Reset Password") {
                        resetPassword(email, newPassword, confirmedPassword, personnelPassword)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }

                }

            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = resetPasswordMessage.isNullOrEmpty(),
        title = null,
        textContent = resetPasswordMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (resetPasswordIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
