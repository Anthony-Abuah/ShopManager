package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.AccountInformation
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
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
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
    openSignUpPage: ()-> Unit,
) {
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

    else {
        BasicScreenColumnWithoutBottomBar {
            val shopName = shopInfo?.companyName?.toNotNull().toEllipses(30)
            val contact = "Contact: ${shopInfo?.companyContact?.toNotNull().toEllipses(25)}"
            val location = "Location: ${shopInfo?.companyLocation?.toNotNull().toEllipses(25)}"
            Box(modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(200.dp)
                .padding(LocalSpacing.current.noPadding),
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
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = LocalSpacing.current.medium)
                    .background(alternateBackgroundColor, MaterialTheme.shapes.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {

                Box(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .padding(LocalSpacing.current.default,)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    VerticalDisplayAndEditTextValues(
                        firstText = ShopName,
                        firstTextColor = titleColor,
                        secondText = shopInfo?.companyName.toNotNull().ifBlank { NotAvailable },
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
                        secondText = shopInfo?.companyContact.toNotNull().ifBlank { NotAvailable },
                        secondTextColor = descriptionColor,
                        value = shopInfo?.companyContact.toNotNull().ifBlank { NotAvailable },
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
                        secondText = shopInfo?.companyLocation.toNotNull().ifBlank { NotAvailable },
                        secondTextColor = descriptionColor,
                        value = shopInfo?.companyLocation.toNotNull().ifBlank { NotAvailable },
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
            }


            Column(
                modifier = Modifier
                    .padding(vertical = LocalSpacing.current.large)
                    .background(alternateBackgroundColor, MaterialTheme.shapes.medium),
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
                        .padding(LocalSpacing.current.small)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDisplayAndEditTextValues(
                        modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                        firstText = shopInfo?.email.toNotNull().ifBlank { NotAvailable },
                        secondText = Email,
                        secondTextFontWeight = FontWeight.Normal,
                        secondTextColor = MaterialTheme.colorScheme.primary,
                        value = shopInfo?.email.toNotNull().ifBlank { NotAvailable },
                        trailingIcon = R.drawable.ic_keyboard_arrow_right,
                        trailingIconWidth = 32.dp,
                        leadingIcon = R.drawable.ic_email,
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
                        .padding(LocalSpacing.current.default,)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    VerticalDisplayAndEditTextValues(
                        firstText = ProductsSold,
                        firstTextColor = titleColor,
                        secondText = shopInfo?.companyProductsAndServices.toNotNull().ifBlank { NotAvailable },
                        secondTextColor = descriptionColor,
                        value = shopInfo?.companyProductsAndServices.toNotNull().ifBlank { NotAvailable },
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

                Box(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .padding(LocalSpacing.current.default,)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    VerticalDisplayAndEditTextValues(
                        firstText = ShopOwners,
                        firstTextColor = titleColor,
                        secondText = shopInfo?.companyOwners.toNotNull().ifBlank { NotAvailable },
                        secondTextColor = descriptionColor,
                        value = shopInfo?.companyOwners.toNotNull().ifBlank { NotAvailable },
                        leadingIcon = R.drawable.ic_person_filled,
                        leadingIconWidth = 32.dp,
                        onBackgroundColor = titleColor,
                        keyboardType = KeyboardType.Text,
                        label = EnterCompanyOwner,
                        placeholder = CompanyOwnerPlaceholder,
                        textFieldIcon = R.drawable.ic_edit,
                        getUpdatedValue = {}
                    )
                }

            }


            // Shop name
            if (shopInfo != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = LocalSpacing.current.default),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .padding(LocalSpacing.current.noPadding),
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Shop name: ${shopInfo.companyName}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = LocalSpacing.current.default,
                        bottom = LocalSpacing.current.smallMedium
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = LocalSpacing.current.divider
                )

                // Account Info Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = AccountInformation,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                }



                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = LocalSpacing.current.default,
                        bottom = LocalSpacing.current.small
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = LocalSpacing.current.divider
                )

                // Date Registered
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Date registered",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                val dateRegistered = shopInfo.dateCreated
                                val localDate = dateRegistered.toLocalDate()
                                val dayOfWeek = localDate.dayOfWeek.toString().lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                                Text(
                                    text = "$dayOfWeek, $localDate",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )

                // Shop Email
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Shop Email",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = shopInfo.email,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )

                // Shop Contact
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Shop Contact",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = shopInfo.companyContact,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )


                // Shop Location
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Shop Location",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = shopInfo.companyLocation.toNotNull(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )

                // Shop Products
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Shop Products",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = shopInfo.companyProductsAndServices.toNotNull(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )

                // Shop Owners
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Shop Owner(s)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = shopInfo.companyOwners.toNotNull(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )

                // Shop Subscription Package
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Shop Subscription package",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = if (shopInfo.subscriptionPackage.isNullOrEmpty()) "Not subscribed to any package" else shopInfo.subscriptionPackage.toNotNull(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )

                // Shop Subscription End date
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small)
                    ) {
                        Column(
                            modifier = Modifier.weight(6f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Shop subscription end date",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = LocalSpacing.current.small),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                val subscriptionEndDate = shopInfo.subscriptionEndDate?.toDate()
                                val localDate = subscriptionEndDate?.toLocalDate()
                                val dayOfWeek = localDate?.dayOfWeek.toString().lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                                Text(
                                    text = if (shopInfo.subscriptionPackage.isNullOrEmpty()) "Not Subscribed" else "$dayOfWeek, $localDate",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.25.dp
                )

                Box(modifier = Modifier
                    .padding(LocalSpacing.current.small)
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
                            Icon(painter = painterResource(id = R.drawable.ic_logout),
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

                Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

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
