package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.rememberWindowInfo

@Composable
fun ViewCompanyContent(
    companyId: Int,
    uniqueCompanyId: String,
    companyName: String,
    companyContact: String,
    companyLocation: String,
    companyOwner: String,
    companyProduct: String,
    otherInfo: String,
    getUpdatedCompanyName: (companyName: String) -> Unit,
    getUpdatedCompanyContact: (companyContact: String) -> Unit,
    getUpdatedCompanyLocation: (companyLocation: String) -> Unit,
    getUpdatedCompanyOwners: (String) -> Unit,
    getUpdatedCompanyProducts: (String) -> Unit,
    getUpdatedCompanyOtherInfo: (shortNotes: String) -> Unit,
    updateCompany: (CompanyEntity) -> Unit,
    navigateBack: () -> Unit,
){

    val context = LocalContext.current

    val windowInfo = rememberWindowInfo()

    var expandCompanyName by remember {
        mutableStateOf(false)
    }
    var expandCompanyContact by remember {
        mutableStateOf(false)
    }
    var expandCompanyLocation by remember {
        mutableStateOf(false)
    }
    var expandCompanyOwner by remember {
        mutableStateOf(false)
    }
    var expandCompanyProduct by remember {
        mutableStateOf(false)
    }
    var expandCompanyShortNotes by remember {
        mutableStateOf(false)
    }
/*

    BasicScreenColumnWithoutBottomBar {
        // Company Photo
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .size(125.dp)
                    .padding(LocalSpacing.current.small)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_company),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = emptyString
                )
            }
        }

        Divider(
            modifier = Modifier.padding(
                top = LocalSpacing.current.default,
                bottom = LocalSpacing.current.smallMedium
            ),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = LocalSpacing.current.divider
        )

        // Company Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = CompanyInformation,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
        }


        Divider(
            modifier = Modifier.padding(
                top = LocalSpacing.current.smallMedium,
                bottom = LocalSpacing.current.small
            ),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Company Name
        Column(modifier = Modifier.fillMaxWidth(),
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
                            text = CompanyName,
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
                            text = companyName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = LocalSpacing.current.small,
                            vertical = LocalSpacing.current.default
                        )
                        .clickable {
                            expandCompanyName = !expandCompanyName
                        },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expandCompanyName
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedCompanyName by remember {
                            mutableStateOf(companyName)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIconError(
                                        value = updatedCompanyName,
                                        onValueChange = {
                                            updatedCompanyName = it
                                        },
                                        isError = false,
                                        readOnly = false,
                                        placeholder = CompanyNamePlaceholder,
                                        label = UpdateCompanyName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyName(updatedCompanyName)
                                        expandCompanyName = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(12f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIconError(
                                        value = updatedCompanyName,
                                        onValueChange = {
                                            updatedCompanyName = it
                                        },
                                        isError = false,
                                        readOnly = false,
                                        placeholder = CompanyNamePlaceholder,
                                        label = UpdateCompanyName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyName(updatedCompanyName)
                                        expandCompanyName = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            else -> {
                                Box(modifier = Modifier.weight(15f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIconError(
                                        value = updatedCompanyName,
                                        onValueChange = {
                                            updatedCompanyName = it
                                        },
                                        isError = false,
                                        readOnly = false,
                                        placeholder = CompanyNamePlaceholder,
                                        label = UpdateCompanyName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyName(updatedCompanyName)
                                        expandCompanyName = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Company Contact
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small)
            ) {
                Column(
                    modifier = Modifier.weight(7f),
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
                            text = CompanyContact,
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
                            text = companyContact,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = LocalSpacing.current.small,
                            vertical = LocalSpacing.current.default
                        )
                        .clickable {
                            expandCompanyContact = !expandCompanyContact
                        },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expandCompanyContact
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedCompanyContact by remember {
                            mutableStateOf(companyContact)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyContact,
                                        onValueChange = {
                                            updatedCompanyContact = it
                                        },
                                        placeholder = CompanyContactPlaceholder,
                                        label = UpdateCompanyContact,
                                        icon = R.drawable.ic_contact,
                                        keyboardType = KeyboardType.Phone
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyContact(updatedCompanyContact)
                                        expandCompanyContact = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(12f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyContact,
                                        onValueChange = {
                                            updatedCompanyContact = it
                                        },
                                        placeholder = CompanyContactPlaceholder,
                                        label = UpdateCompanyContact,
                                        icon = R.drawable.ic_contact,
                                        keyboardType = KeyboardType.Phone
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyContact(updatedCompanyContact)
                                        expandCompanyContact = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            else -> {
                                Box(modifier = Modifier.weight(15f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyContact,
                                        onValueChange = {
                                            updatedCompanyContact = it
                                        },
                                        placeholder = CompanyContactPlaceholder,
                                        label = UpdateCompanyContact,
                                        icon = R.drawable.ic_contact,
                                        keyboardType = KeyboardType.Phone
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyContact(updatedCompanyContact)
                                        expandCompanyContact = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Company Owner
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small)
            ) {
                Column(
                    modifier = Modifier.weight(7f),
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
                            text = CompanyOwners,
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
                            text = companyOwner,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = LocalSpacing.current.small,
                            vertical = LocalSpacing.current.default
                        )
                        .clickable {
                            expandCompanyOwner = !expandCompanyOwner
                        },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expandCompanyOwner
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedCompanyOwner by remember {
                            mutableStateOf(companyOwner)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyOwner,
                                        onValueChange = {
                                            updatedCompanyOwner = it
                                        },
                                        placeholder = CompanyOwnerPlaceholder,
                                        label = UpdateCompanyOwner,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyOwners(updatedCompanyOwner)
                                        expandCompanyOwner = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(12f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyOwner,
                                        onValueChange = {
                                            updatedCompanyOwner = it
                                        },
                                        placeholder = CompanyOwnerPlaceholder,
                                        label = UpdateCompanyOwner,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyOwners(updatedCompanyOwner)
                                        expandCompanyOwner = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            else -> {
                                Box(modifier = Modifier.weight(15f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyOwner,
                                        onValueChange = {
                                            updatedCompanyOwner = it
                                        },
                                        placeholder = CompanyOwnerPlaceholder,
                                        label = UpdateCompanyOwner,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyOwners(updatedCompanyOwner)
                                        expandCompanyOwner = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Company Products and services
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small)
            ) {
                Column(
                    modifier = Modifier.weight(7f),
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
                            text = CompanyProductsAndServices,
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
                            text = companyProduct,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = LocalSpacing.current.small,
                            vertical = LocalSpacing.current.default
                        )
                        .clickable {
                            expandCompanyProduct = !expandCompanyProduct
                        },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expandCompanyProduct
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedCompanyProduct by remember {
                            mutableStateOf(companyProduct)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyProduct,
                                        onValueChange = {
                                            updatedCompanyProduct = it
                                        },
                                        placeholder = CompanyProductPlaceholder,
                                        label = UpdateCompanyProduct,
                                        icon = R.drawable.ic_product,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyProducts(updatedCompanyProduct)
                                        expandCompanyProduct = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(12f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyProduct,
                                        onValueChange = {
                                            updatedCompanyProduct = it
                                        },
                                        placeholder = CompanyProductPlaceholder,
                                        label = UpdateCompanyProduct,
                                        icon = R.drawable.ic_product,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyProducts(updatedCompanyProduct)
                                        expandCompanyProduct = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            else -> {
                                Box(modifier = Modifier.weight(15f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyProduct,
                                        onValueChange = {
                                            updatedCompanyProduct = it
                                        },
                                        placeholder = CompanyProductPlaceholder,
                                        label = UpdateCompanyProduct,
                                        icon = R.drawable.ic_product,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyProducts(updatedCompanyProduct)
                                        expandCompanyProduct = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Company Location
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small)
            ) {
                Column(
                    modifier = Modifier.weight(7f),
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
                            text = CompanyLocation,
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
                            text = companyLocation,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = LocalSpacing.current.small,
                            vertical = LocalSpacing.current.default
                        )
                        .clickable {
                            expandCompanyLocation = !expandCompanyLocation
                        },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expandCompanyLocation
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedCompanyLocation by remember {
                            mutableStateOf(companyLocation)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyLocation,
                                        onValueChange = {
                                            updatedCompanyLocation = it
                                        },
                                        placeholder = CompanyLocationPlaceholder,
                                        label = UpdateCompanyLocation,
                                        icon = R.drawable.ic_location,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyLocation(updatedCompanyLocation)
                                        expandCompanyLocation = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(12f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyLocation,
                                        onValueChange = {
                                            updatedCompanyLocation = it
                                        },
                                        placeholder = CompanyLocationPlaceholder,
                                        label = UpdateCompanyLocation,
                                        icon = R.drawable.ic_location,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyLocation(updatedCompanyLocation)
                                        expandCompanyLocation = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            else -> {
                                Box(modifier = Modifier.weight(15f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIcon(
                                        value = updatedCompanyLocation,
                                        onValueChange = {
                                            updatedCompanyLocation = it
                                        },
                                        placeholder = CompanyLocationPlaceholder,
                                        label = UpdateCompanyLocation,
                                        icon = R.drawable.ic_location,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyLocation(updatedCompanyLocation)
                                        expandCompanyLocation = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Company Short Notes
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small)
            ) {
                Column(
                    modifier = Modifier.weight(7f),
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
                            text = CompanyShortNotes,
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
                            text = otherInfo,
                            textAlign = TextAlign.Justify,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 4,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = LocalSpacing.current.small,
                            vertical = LocalSpacing.current.default
                        )
                        .clickable {
                            expandCompanyShortNotes = !expandCompanyShortNotes
                        },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expandCompanyShortNotes
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedShortNotes by remember {
                            mutableStateOf(otherInfo)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DescriptionTextFieldWithTrailingIcon(
                                        value = updatedShortNotes,
                                        onValueChange = {
                                            updatedShortNotes = it
                                        },
                                        placeholder = CompanyShortNotesPlaceholder,
                                        label = CompanyShortNotes,
                                        icon = R.drawable.ic_short_notes,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyOtherInfo(updatedShortNotes)
                                        expandCompanyShortNotes = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(12f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DescriptionTextFieldWithTrailingIcon(
                                        value = updatedShortNotes,
                                        onValueChange = {
                                            updatedShortNotes = it
                                        },
                                        placeholder = CompanyShortNotesPlaceholder,
                                        label = CompanyShortNotes,
                                        icon = R.drawable.ic_short_notes,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyOtherInfo(updatedShortNotes)
                                        expandCompanyShortNotes = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            else -> {
                                Box(modifier = Modifier.weight(15f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DescriptionTextFieldWithTrailingIcon(
                                        value = updatedShortNotes,
                                        onValueChange = {
                                            updatedShortNotes = it
                                        },
                                        placeholder = CompanyShortNotesPlaceholder,
                                        label = CompanyShortNotes,
                                        icon = R.drawable.ic_short_notes,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedCompanyOtherInfo(updatedShortNotes)
                                        expandCompanyShortNotes = false
                                    },
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = emptyString,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Update Button
        Box(modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                val company = CompanyEntity(
                    companyId = companyId,
                    uniqueCompanyId = uniqueCompanyId,
                    companyName = companyName.trimEnd(),
                    companyContact = companyContact.trimEnd(),
                    companyLocation = companyLocation.ifEmpty { null },
                    anyOtherInfo = otherInfo.ifEmpty { null },
                    owners = companyOwner,
                    productOrServices = companyProduct,
                )
                updateCompany(company)
                Toast.makeText(context, "$companyName has been added", Toast.LENGTH_LONG).show()
                navigateBack()
            }
        }



    }
*/

}