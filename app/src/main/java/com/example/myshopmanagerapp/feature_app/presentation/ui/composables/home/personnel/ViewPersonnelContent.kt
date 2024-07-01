package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.DoesPersonnelHaveAdminRights
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelContact
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelFirstName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelHasAdminRights
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelInformation
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelLastName
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelOtherNames
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRole
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelRolePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.PersonnelShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.FormRelatedString.UpdatePersonnelContact
import com.example.myshopmanagerapp.core.FormRelatedString.UpdatePersonnelFirstName
import com.example.myshopmanagerapp.core.FormRelatedString.UpdatePersonnelLastName
import com.example.myshopmanagerapp.core.FormRelatedString.UpdatePersonnelOtherName
import com.example.myshopmanagerapp.core.FormRelatedString.UpdatePersonnelRole
import com.example.myshopmanagerapp.core.Functions
import com.example.myshopmanagerapp.core.Functions.nameIsValid
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.domain.model.PersonnelRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ViewPersonnelContent(
    personnelPhoto: String?,
    personnelId: Int,
    uniquePersonnelId: String,
    personnelFirstName: String,
    personnelLastName: String,
    personnelOtherNames: String?,
    personnelContact: String,
    personnelRole: String,
    personnelHasAdminRight: Boolean,
    anyOtherInfo: String?,
    listOfPersonnelNames: List<String>,
    getUpdatedPersonnelFirstName: (personnelFirstName: String) -> Unit,
    getUpdatedPersonnelLastName: (personnelFirstName: String) -> Unit,
    getUpdatedPersonnelOtherNames: (personnelFirstName: String?) -> Unit,
    getUpdatedPersonnelContact: (personnelContact: String) -> Unit,
    getUpdatedPersonnelRole: (personnelLocation: String) -> Unit,
    getUpdatedPersonnelAdminRight: (adminRight: Boolean) -> Unit,
    getUpdatePersonnelOtherInfo: (shortNotes: String?) -> Unit,
    onTakePhoto: (String?, String?, String?, String?, String?, String?, Boolean?) -> Unit,
    updatePersonnel: (PersonnelEntity) -> Unit,
    navigateBack: () -> Unit,
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val windowInfo = rememberWindowInfo()

    var expandPersonnelFirstName by remember {
        mutableStateOf(false)
    }
    var expandPersonnelLastName by remember {
        mutableStateOf(false)
    }
    var expandPersonnelOtherNames by remember {
        mutableStateOf(false)
    }
    var expandPersonnelContact by remember {
        mutableStateOf(false)
    }
    var expandPersonnelRole by remember {
        mutableStateOf(false)
    }
    var expandPersonnelAdminRights by remember {
        mutableStateOf(false)
    }
    var expandPersonnelShortNotes by remember {
        mutableStateOf(false)
    }
    var firstNameError by remember {
        mutableStateOf(false)
    }
    var lastNameError by remember {
        mutableStateOf(false)
    }
    var otherNamesError by remember {
        mutableStateOf(false)
    }
    var openRolesDialog by remember {
        mutableStateOf(false)
    }
    var personnelRoles = UserPreferences(context).getPersonnelRoles.collectAsState(initial = null).value


    BasicScreenColumnWithoutBottomBar {
        // Personnel Photo
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
                    .padding(LocalSpacing.current.noPadding),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(LocalSpacing.current.noPadding)
                        .background(MaterialTheme.colorScheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(LocalSpacing.current.small),
                        imageVector = Icons.Default.Person,
                        contentDescription = emptyString
                    )
                }
                Icon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = LocalSpacing.current.small)
                        .clickable {
                            onTakePhoto(
                                personnelFirstName,
                                personnelLastName,
                                personnelOtherNames,
                                personnelContact,
                                personnelRole,
                                anyOtherInfo,
                                personnelHasAdminRight
                            )
                        },
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        vertical = LocalSpacing.current.medium,
                        horizontal = LocalSpacing.current.default,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Personnel Id: $uniquePersonnelId",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = PersonnelInformation,
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

        // Personnel First Name
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
                            text = PersonnelFirstName,
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
                            text = personnelFirstName,
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
                            expandPersonnelFirstName = !expandPersonnelFirstName
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
                visible = expandPersonnelFirstName
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedPersonnelFirstName by remember {
                            mutableStateOf(personnelFirstName)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIconError(
                                        value = updatedPersonnelFirstName,
                                        onValueChange = {
                                            updatedPersonnelFirstName = it
                                            firstNameError = nameIsValid(it)
                                        },
                                        isError = firstNameError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelFirstName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelFirstName(updatedPersonnelFirstName)
                                        expandPersonnelFirstName = firstNameError
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
                                        value = updatedPersonnelFirstName,
                                        onValueChange = {
                                            updatedPersonnelFirstName = it
                                            firstNameError = nameIsValid(it)
                                        },
                                        isError = firstNameError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelFirstName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelFirstName(updatedPersonnelFirstName)
                                        expandPersonnelFirstName = firstNameError
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
                                        value = updatedPersonnelFirstName,
                                        onValueChange = {
                                            updatedPersonnelFirstName = it
                                            firstNameError = nameIsValid(it)
                                        },
                                        isError = firstNameError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelFirstName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelFirstName(updatedPersonnelFirstName)
                                        expandPersonnelFirstName = firstNameError
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

        // Personnel Last Name
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
                            text = PersonnelLastName,
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
                            text = personnelLastName,
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
                            expandPersonnelLastName = !expandPersonnelLastName
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
                visible = expandPersonnelLastName
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        var updatedPersonnelLastName by remember {
                            mutableStateOf(personnelLastName)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIconError(
                                        value = updatedPersonnelLastName,
                                        onValueChange = {
                                            updatedPersonnelLastName = it
                                            lastNameError = nameIsValid(it)
                                        },
                                        isError = lastNameError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelLastName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelLastName(updatedPersonnelLastName)
                                        expandPersonnelLastName = lastNameError
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
                                        value = updatedPersonnelLastName,
                                        onValueChange = {
                                            updatedPersonnelLastName = it
                                            lastNameError = nameIsValid(it)
                                        },
                                        isError = lastNameError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelLastName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelLastName(updatedPersonnelLastName)
                                        expandPersonnelLastName = lastNameError
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
                                        value = updatedPersonnelLastName,
                                        onValueChange = {
                                            updatedPersonnelLastName = it
                                            lastNameError = nameIsValid(it)
                                        },
                                        isError = lastNameError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelLastName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelLastName(updatedPersonnelLastName)
                                        expandPersonnelLastName = lastNameError
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

        // Personnel Other Name
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
                            text = PersonnelOtherNames,
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
                            text = personnelOtherNames ?: emptyString,
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
                            expandPersonnelOtherNames = !expandPersonnelOtherNames
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
                visible = expandPersonnelOtherNames
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedPersonnelOtherNames by remember {
                            mutableStateOf(personnelOtherNames)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIconError(
                                        value = updatedPersonnelOtherNames ?: emptyString,
                                        onValueChange = {
                                            updatedPersonnelOtherNames = it
                                            otherNamesError = if (updatedPersonnelOtherNames.isNullOrEmpty()) false else nameIsValid(it)
                                        },
                                        isError = otherNamesError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelOtherName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelOtherNames(updatedPersonnelOtherNames)
                                        expandPersonnelOtherNames = false
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
                                        value = updatedPersonnelOtherNames ?: emptyString,
                                        onValueChange = {
                                            updatedPersonnelOtherNames = it
                                            otherNamesError = if (updatedPersonnelOtherNames.isNullOrEmpty()) false else nameIsValid(it)
                                        },
                                        isError = otherNamesError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelOtherName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelOtherNames(updatedPersonnelOtherNames)
                                        expandPersonnelOtherNames = false
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
                                        value = updatedPersonnelOtherNames ?: emptyString,
                                        onValueChange = {
                                            updatedPersonnelOtherNames = it
                                            otherNamesError = if (updatedPersonnelOtherNames.isNullOrEmpty()) false else nameIsValid(it)
                                        },
                                        isError = otherNamesError,
                                        readOnly = false,
                                        placeholder = PersonnelNamePlaceholder,
                                        label = UpdatePersonnelOtherName,
                                        icon = R.drawable.ic_person_filled,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelOtherNames(updatedPersonnelOtherNames)
                                        expandPersonnelOtherNames = false
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

        // Personnel Contact
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
                            text = PersonnelContact,
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
                            text = personnelContact,
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
                            expandPersonnelContact = !expandPersonnelContact
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
                visible = expandPersonnelContact
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedPersonnelContact by remember {
                            mutableStateOf(personnelContact)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextFieldWithTrailingIconError(
                                        value = updatedPersonnelContact,
                                        onValueChange = {
                                            updatedPersonnelContact = it
                                        },
                                        isError = false,
                                        readOnly = false,
                                        placeholder = PersonnelContactPlaceholder,
                                        label = UpdatePersonnelContact,
                                        icon = R.drawable.ic_contact,
                                        keyboardType = KeyboardType.Phone
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelContact(updatedPersonnelContact)
                                        expandPersonnelContact = false
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
                                        value = updatedPersonnelContact,
                                        onValueChange = {
                                            updatedPersonnelContact = it
                                        },
                                        isError = false,
                                        readOnly = false,
                                        placeholder = PersonnelContactPlaceholder,
                                        label = UpdatePersonnelContact,
                                        icon = R.drawable.ic_contact,
                                        keyboardType = KeyboardType.Phone
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelContact(updatedPersonnelContact)
                                        expandPersonnelContact = false
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
                                        value = updatedPersonnelContact,
                                        onValueChange = {
                                            updatedPersonnelContact = it
                                        },
                                        isError = false,
                                        readOnly = false,
                                        placeholder = PersonnelContactPlaceholder,
                                        label = UpdatePersonnelContact,
                                        icon = R.drawable.ic_contact,
                                        keyboardType = KeyboardType.Phone
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelContact(updatedPersonnelContact)
                                        expandPersonnelContact = false
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

        // Personnel PersonnelRole
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
                            text = PersonnelRole,
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
                            text = personnelRole,
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
                            expandPersonnelRole = !expandPersonnelRole
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
                visible = expandPersonnelRole
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedPersonnelRole by remember {
                            mutableStateOf(personnelRole)
                        }
                        val roles = Functions.fromRolesJson(personnelRoles)
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AutoCompleteWithAddButton(
                                        label = UpdatePersonnelRole,
                                        placeholder = PersonnelRolePlaceholder,
                                        listItems = roles.map { it.personnelRole },
                                        readOnly = false,
                                        expandedIcon = R.drawable.ic_role,
                                        unexpandedIcon = R.drawable.ic_role,
                                        onClickAddButton = {
                                            openRolesDialog = !openRolesDialog
                                        },
                                        getSelectedItem = {_role->
                                            updatedPersonnelRole = _role
                                        }
                                    )
                                }

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelRole(updatedPersonnelRole)
                                        expandPersonnelRole = false
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

                                    AutoCompleteWithAddButton(
                                        label = UpdatePersonnelRole,
                                        placeholder = PersonnelRolePlaceholder,
                                        listItems = roles.map { it.personnelRole },
                                        readOnly = false,
                                        expandedIcon = R.drawable.ic_role,
                                        unexpandedIcon = R.drawable.ic_role,
                                        onClickAddButton = {
                                            openRolesDialog = !openRolesDialog
                                        },
                                        getSelectedItem = {_role->
                                            updatedPersonnelRole = _role
                                        }
                                    )
                                }

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelRole(updatedPersonnelRole)
                                        expandPersonnelRole = false
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
                                    AutoCompleteWithAddButton(
                                        label = UpdatePersonnelRole,
                                        placeholder = PersonnelRolePlaceholder,
                                        listItems = roles.map { it.personnelRole },
                                        readOnly = false,
                                        expandedIcon = R.drawable.ic_role,
                                        unexpandedIcon = R.drawable.ic_role,
                                        onClickAddButton = {
                                            openRolesDialog = !openRolesDialog
                                        },
                                        getSelectedItem = {_role->
                                            updatedPersonnelRole = _role
                                        }
                                    )
                                }

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelRole(updatedPersonnelRole)
                                        expandPersonnelRole = false
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
                        BasicTextFieldAlertDialog(
                            openDialog = openRolesDialog,
                            title = "Add PersonnelRole",
                            textContent = emptyString,
                            placeholder = "Eg: Manager",
                            label = "Add personnel role",
                            icon = R.drawable.ic_role,
                            keyboardType = KeyboardType.Text,
                            unconfirmedUpdatedToastText = "PersonnelRole not added",
                            confirmedUpdatedToastText = null,
                            getValue = { _newRole ->
                                val newPersonnelRole = PersonnelRole(_newRole)
                                val thisRoles = Functions.fromRolesJson(personnelRoles)
                                val mutablePersonnelRoles = mutableListOf<PersonnelRole>()
                                if (thisRoles.map { it.personnelRole.trim().lowercase(Locale.ROOT) }.contains(_newRole.trim().lowercase(Locale.ROOT))) {
                                    Toast.makeText(context, "PersonnelRole: $_newRole already exists", Toast.LENGTH_LONG).show()
                                    openRolesDialog = false
                                } else {
                                    mutablePersonnelRoles.addAll(thisRoles)
                                    mutablePersonnelRoles.add(newPersonnelRole)
                                    personnelRoles = Functions.toRolesJson(mutablePersonnelRoles)
                                    coroutineScope.launch {
                                        UserPreferences(context).savePersonnelRoles(personnelRoles ?: emptyString)
                                    }
                                    Toast.makeText(context, "PersonnelRole: $_newRole successfully added", Toast.LENGTH_LONG).show()
                                }
                            }
                        ) {
                            openRolesDialog = false
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

        // Personnel Has Admin Right
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
                            text = PersonnelHasAdminRights,
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
                            text = if (personnelHasAdminRight) "Yes" else "No",
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
                            expandPersonnelAdminRights = !expandPersonnelAdminRights
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
                visible = expandPersonnelAdminRights
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedPersonnelHasAdminRights by remember {
                            mutableStateOf(personnelHasAdminRight)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AutoCompleteTextField(
                                        label = DoesPersonnelHaveAdminRights,
                                        placeholder = DoesPersonnelHaveAdminRights,
                                        readOnly = true,
                                        expandedIcon = R.drawable.ic_admin,
                                        unexpandedIcon = R.drawable.ic_admin,
                                        listItems = listOf("Yes", "No"),
                                        getSelectedItem = {_adminRights->
                                            updatedPersonnelHasAdminRights = _adminRights == "Yes"
                                        }
                                    )
                                }

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelAdminRight(updatedPersonnelHasAdminRights)
                                        expandPersonnelAdminRights = false
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

                                    AutoCompleteTextField(
                                        label = DoesPersonnelHaveAdminRights,
                                        placeholder = DoesPersonnelHaveAdminRights,
                                        readOnly = true,
                                        expandedIcon = R.drawable.ic_admin,
                                        unexpandedIcon = R.drawable.ic_admin,
                                        listItems = listOf("Yes", "No"),
                                        getSelectedItem = {_adminRights->
                                            updatedPersonnelHasAdminRights = _adminRights == "Yes"
                                        }
                                    )
                                }

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelAdminRight(updatedPersonnelHasAdminRights)
                                        expandPersonnelAdminRights = false
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
                                    AutoCompleteTextField(
                                        label = DoesPersonnelHaveAdminRights,
                                        placeholder = DoesPersonnelHaveAdminRights,
                                        readOnly = true,
                                        expandedIcon = R.drawable.ic_admin,
                                        unexpandedIcon = R.drawable.ic_admin,
                                        listItems = listOf("Yes", "No"),
                                        getSelectedItem = {_adminRights->
                                            updatedPersonnelHasAdminRights = _adminRights == "Yes"
                                        }
                                    )
                                }

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatedPersonnelAdminRight(updatedPersonnelHasAdminRights)
                                        expandPersonnelAdminRights = false
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

        // Personnel Short Notes
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
                            text = PersonnelShortNotes,
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
                            text = anyOtherInfo ?: emptyString,
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
                            expandPersonnelShortNotes = !expandPersonnelShortNotes
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
                visible = expandPersonnelShortNotes
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedOtherInfo by remember {
                            mutableStateOf(anyOtherInfo)
                        }
                        when (windowInfo.screenWidthInfo) {
                            WindowInfo.WindowType.Compact -> {
                                Box(modifier = Modifier.weight(7f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DescriptionTextFieldWithTrailingIcon(
                                        value = updatedOtherInfo ?: emptyString,
                                        onValueChange = {
                                            updatedOtherInfo = it
                                        },
                                        placeholder = PersonnelShortNotesPlaceholder,
                                        label = PersonnelShortNotes,
                                        icon = R.drawable.ic_short_notes,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatePersonnelOtherInfo(updatedOtherInfo)
                                        expandPersonnelShortNotes = false
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
                                        value = updatedOtherInfo ?: emptyString,
                                        onValueChange = {
                                            updatedOtherInfo = it
                                        },
                                        placeholder = PersonnelShortNotesPlaceholder,
                                        label = PersonnelShortNotes,
                                        icon = R.drawable.ic_short_notes,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatePersonnelOtherInfo(updatedOtherInfo)
                                        expandPersonnelShortNotes = false
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
                                        value = updatedOtherInfo ?: emptyString,
                                        onValueChange = {
                                            updatedOtherInfo = it
                                        },
                                        placeholder = PersonnelShortNotesPlaceholder,
                                        label = PersonnelShortNotes,
                                        icon = R.drawable.ic_short_notes,
                                        keyboardType = KeyboardType.Text
                                    )
                                }
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .padding(LocalSpacing.current.small)
                                    .clickable {
                                        getUpdatePersonnelOtherInfo(updatedOtherInfo)
                                        expandPersonnelShortNotes = false
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

        Box(modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                if (personnelFirstName.isEmpty()){
                    Toast.makeText(context, "Please enter first route", Toast.LENGTH_LONG).show()
                }else if (firstNameError){
                    Toast.makeText(context, "$personnelFirstName is not valid", Toast.LENGTH_LONG).show()
                }else if (personnelLastName.isEmpty()){
                    Toast.makeText(context, "Please enter last route", Toast.LENGTH_LONG).show()
                }else if (lastNameError){
                    Toast.makeText(context, "$personnelLastName is not valid", Toast.LENGTH_LONG).show()
                }else if (otherNamesError){
                    Toast.makeText(context, "$personnelOtherNames is not valid", Toast.LENGTH_LONG).show()
                }else if (listOfPersonnelNames.any{
                        val firstname = personnelFirstName.lowercase(Locale.ROOT)
                        val lastname = personnelLastName.lowercase(Locale.ROOT)
                        val otherNames = personnelOtherNames?.lowercase(Locale.ROOT)
                        it.trim() =="$firstname$lastname$otherNames"
                    }){
                    Toast.makeText(context, "Personnel with $personnelFirstName and $personnelLastName already exists", Toast.LENGTH_LONG).show()
                }else if (personnelContact.isEmpty()){
                    Toast.makeText(context, "Please enter personnel contact", Toast.LENGTH_LONG).show()
                }else if (personnelRole.isEmpty()){
                    Toast.makeText(context, "Please select personnel role", Toast.LENGTH_LONG).show()
                }else{
                    val personnel = PersonnelEntity(
                        personnelId,
                        uniquePersonnelId,
                        personnelFirstName,
                        personnelLastName,
                        personnelOtherNames,
                        personnelContact,
                        "1234",
                        "1234",
                        personnelPhoto,
                        anyOtherInfo,
                        personnelRole,
                        personnelHasAdminRight
                    )
                    updatePersonnel(personnel)
                    navigateBack()
                }
            }
        }


    }

}