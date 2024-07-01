package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AccountInformation
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Blue40
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Blue90
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
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
    }else {
        BasicScreenColumnWithoutBottomBar {
            // Shop route
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
                            text = "Shop route: ${shopInfo.companyName}",
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
