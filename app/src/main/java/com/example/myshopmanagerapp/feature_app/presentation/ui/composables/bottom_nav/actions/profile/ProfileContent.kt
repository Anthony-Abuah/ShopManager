package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.Email
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyEmailPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyOwnerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyProductPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Contact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyEmail
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyOwner
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyProducts
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCustomerLocation
import com.example.myshopmanagerapp.core.FormRelatedString.Location
import com.example.myshopmanagerapp.core.FormRelatedString.ProductsSold
import com.example.myshopmanagerapp.core.FormRelatedString.ShopName
import com.example.myshopmanagerapp.core.FormRelatedString.ShopOwners
import com.example.myshopmanagerapp.core.FormRelatedString.ShopPersonnel
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.TypeConverters.toCompanyOwners
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import java.util.*


@Composable
fun ProfileContent(
    isLoggedIn: Boolean,
    shopInfo: CompanyEntity?,
    logoutMessage: String?,
    isLoggingOut: Boolean,
    logout: ()-> Unit,
    openLoginPage: ()-> Unit,
    openRegisterPage: ()-> Unit,
) {
    val backgroundColor = if (isSystemInDarkTheme()) Grey5 else Color.White
    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90

    val shadowColor = if (isSystemInDarkTheme()) Grey5 else Grey80
    val descriptionColor = if (isSystemInDarkTheme()) Grey70 else Grey40
    val titleColor = if (isSystemInDarkTheme()) Grey99 else Grey10

    val greenContentLight = if (isSystemInDarkTheme()) Grey70 else Green30
    val greenContent = if (isSystemInDarkTheme()) Grey99 else Green20

    val logoutBackground = if (isSystemInDarkTheme()) Red10 else Red95
    val logoutContentLight = if (isSystemInDarkTheme()) Grey70 else Red30
    val logoutContent = if (isSystemInDarkTheme()) Grey99 else Red20

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var confirmLogout by remember {
        mutableStateOf(false)
    }

    if (isLoggedIn.not()) {
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
                .weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You have not logged in.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        fontWeight = FontWeight.Normal
                    )
                }
                Box(modifier = Modifier
                    .padding(LocalSpacing.current.extraSmall)
                    .clickable { openLoginPage() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tap to login",
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

            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = LocalSpacing.current.large),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(modifier = Modifier.padding(LocalSpacing.current.extraSmall),
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
                Box(modifier = Modifier
                    .padding(LocalSpacing.current.extraSmall)
                    .clickable { openRegisterPage() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Register",
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

    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            val shopName = shopInfo?.companyName?.toNotNull().toEllipses(30)
            val contact = "Contact: ${shopInfo?.companyContact?.toNotNull().toEllipses(25)}"
            val location = "Location: ${shopInfo?.companyLocation?.toNotNull().toEllipses(25)}"

            Box(modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(200.dp)
                .padding(LocalSpacing.current.default),
                contentAlignment = Alignment.Center
            ){
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

            Box(modifier = Modifier.weight(1f)) {
                BasicScreenColumnWithoutBottomBar {
                    Box(
                        modifier = Modifier
                            .padding(vertical = LocalSpacing.current.default)
                            .height(120.dp)
                            .background(Color.Transparent)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val registeredDate = shopInfo?.dateCreated?.toLocalDate()
                        val registeredDay = registeredDate?.dayOfWeek?.toString()?.lowercase()
                            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        val registeredDateString = registeredDate?.toDateString()
                        InfoDisplayCard(
                            image = R.drawable.days,
                            imageWidth = 45.dp,
                            bigText = "$registeredDay, $registeredDateString",
                            bigTextSize = 18.sp,
                            smallTextFontWeight = FontWeight.SemiBold,
                            smallText = "Date Registered",
                            smallTextSize = 14.sp,
                            smallTextColor = descriptionColor,
                            backgroundColor = cardBackgroundColor,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(vertical = LocalSpacing.current.default)
                            .background(alternateBackgroundColor),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .padding(LocalSpacing.current.default)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            VerticalDisplayAndEditTextValues(
                                firstText = ShopName,
                                firstTextColor = titleColor,
                                secondText = shopInfo?.companyName.toNotNull()
                                    .ifBlank { NotAvailable },
                                secondTextColor = descriptionColor,
                                value = shopInfo?.companyName.toNotNull().ifBlank { NotAvailable },
                                leadingIcon = R.drawable.ic_shop_name,
                                leadingIconWidth = 32.dp,
                                onBackgroundColor = titleColor,
                                keyboardType = KeyboardType.Text,
                                label = EnterCompanyName,
                                placeholder = CompanyNamePlaceholder,
                                textFieldIcon = R.drawable.ic_edit,
                                getUpdatedValue = {}
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
                                firstText = Contact,
                                firstTextColor = titleColor,
                                secondText = shopInfo?.companyContact.toNotNull()
                                    .ifBlank { NotAvailable },
                                secondTextColor = descriptionColor,
                                value = shopInfo?.companyContact.toNotNull()
                                    .ifBlank { NotAvailable },
                                leadingIcon = R.drawable.ic_contact,
                                leadingIconWidth = 32.dp,
                                onBackgroundColor = titleColor,
                                keyboardType = KeyboardType.Phone,
                                label = EnterCompanyContact,
                                placeholder = CompanyContactPlaceholder,
                                textFieldIcon = R.drawable.ic_edit,
                                getUpdatedValue = {}
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
                                firstText = Location,
                                firstTextColor = titleColor,
                                secondText = shopInfo?.companyLocation.toNotNull()
                                    .ifBlank { NotAvailable },
                                secondTextColor = descriptionColor,
                                value = shopInfo?.companyLocation.toNotNull()
                                    .ifBlank { NotAvailable },
                                leadingIcon = R.drawable.ic_location,
                                leadingIconWidth = 32.dp,
                                onBackgroundColor = titleColor,
                                keyboardType = KeyboardType.Text,
                                label = EnterCustomerLocation,
                                placeholder = CompanyLocationPlaceholder,
                                textFieldIcon = R.drawable.ic_edit,
                                getUpdatedValue = {}
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
                                firstText = Email,
                                firstTextColor = titleColor,
                                secondText = shopInfo?.email.toNotNull().ifBlank { NotAvailable },
                                secondTextColor = descriptionColor,
                                value = shopInfo?.email.toNotNull().ifBlank { NotAvailable },
                                leadingIcon = R.drawable.ic_email,
                                leadingIconWidth = 32.dp,
                                onBackgroundColor = titleColor,
                                label = EnterCompanyEmail,
                                placeholder = CompanyEmailPlaceholder,
                                keyboardType = KeyboardType.Email,
                                textFieldIcon = R.drawable.ic_edit,
                                getUpdatedValue = {}
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
                                firstText = ProductsSold,
                                firstTextColor = titleColor,
                                secondText = shopInfo?.companyProductsAndServices.toNotNull()
                                    .ifBlank { NotAvailable },
                                secondTextColor = descriptionColor,
                                value = emptyString,
                                leadingIcon = R.drawable.ic_product,
                                leadingIconWidth = 32.dp,
                                onBackgroundColor = titleColor,
                                keyboardType = KeyboardType.Text,
                                label = EnterCompanyProducts,
                                placeholder = CompanyProductPlaceholder,
                                textFieldIcon = R.drawable.ic_edit,
                                getUpdatedValue = {}
                            )
                        }

                    }

                    Box(
                        modifier = Modifier
                            .padding(vertical = LocalSpacing.current.default)
                            .height(120.dp)
                            .background(Color.Transparent)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val endDate = shopInfo?.subscriptionEndDate?.toLocalDate()
                        val endDateDay =
                            endDate?.dayOfWeek?.toString()?.take(3).toNotNull().lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                        val endDateString = endDate?.toDateString().toNotNull()
                        InfoDisplayCard(
                            image = R.drawable.days,
                            imageWidth = 32.dp,
                            bigText = if (shopInfo?.subscriptionEndDate.toNotNull() > Date().time ) "$endDateDay, $endDateString" else "Not Subscribed",
                            bigTextSize = 16.sp,
                            bigTextColor = greenContent,
                            smallTextFontWeight = FontWeight.SemiBold,
                            smallText =  if (shopInfo?.subscriptionEndDate.toNotNull() > Date().time ) "Subscription End date" else "Subscribe to access premium features",
                            smallTextSize = 14.sp,
                            smallTextColor = descriptionColor,
                            backgroundColor = cardBackgroundColor,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }


                    Column(
                        modifier = Modifier
                            .padding(vertical = LocalSpacing.current.default)
                            .background(alternateBackgroundColor),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.Transparent)
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
                                .background(mainBackgroundColor)
                                .padding(LocalSpacing.current.default)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            val owners = shopInfo?.companyOwners.toNotNull().toCompanyOwners().map { it.name }
                            val ownersString  = owners.joinToString(separator = ",\n")
                            VerticalDisplayAndEditTextValues(
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                firstText = ShopOwners,
                                firstTextColor = titleColor,
                                secondText = ownersString,
                                secondTextColor = descriptionColor,
                                value = emptyString,
                                leadingIcon = R.drawable.ic_person_filled,
                                leadingIconWidth = 32.dp,
                                onBackgroundColor = titleColor,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(mainBackgroundColor)
                                .padding(LocalSpacing.current.default)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            val personnel = shopInfo?.companyPersonnel.toNotNull().toPersonnelEntities().map { "${it.firstName} ${it.lastName}${if (!it.otherNames.isNullOrBlank()) ", ".plus(it.otherNames) else emptyString }" }
                            val personnelString  = if(personnel.isEmpty()) NotAvailable else personnel.joinToString(separator = ",\n")
                            VerticalDisplayAndEditTextValues(
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                firstText = ShopPersonnel,
                                firstTextColor = titleColor,
                                secondText = personnelString,
                                secondTextColor = descriptionColor,
                                value = emptyString,
                                leadingIcon = R.drawable.ic_person_filled,
                                leadingIconWidth = 32.dp,
                                onBackgroundColor = titleColor,
                            )
                        }

                    }

                    Box(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .padding(
                                vertical = LocalSpacing.current.medium,
                                horizontal = LocalSpacing.current.default
                            )
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
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

        }
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
        confirmationInfoDialog = false
    }
}
