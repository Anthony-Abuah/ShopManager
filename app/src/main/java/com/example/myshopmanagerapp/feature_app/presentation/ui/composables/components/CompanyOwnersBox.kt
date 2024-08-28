package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AddCompanyOwner
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyOwnerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyOwner
import com.example.myshopmanagerapp.core.TypeConverters.toCompanyOwners
import com.example.myshopmanagerapp.core.TypeConverters.toCompanyOwnersJson
import com.example.myshopmanagerapp.feature_app.domain.model.CompanyOwner
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*


@Composable
fun CompanyOwnersBox(
    ownersNames: String,
    getCompanyOwners: (String)-> Unit,
) {
    var companyOwners by remember {
        mutableStateOf(ownersNames)
    }
    var openCompanyOwnersNames by remember {
        mutableStateOf(false)
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .border(LocalSpacing.current.borderStroke,
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.shapes.small
        )
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
            openCompanyOwnersNames = isFocused
        }
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.default),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)
                .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ){
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    text = AddCompanyOwner,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (companyOwners.isBlank()) FontWeight.Normal else FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(modifier = Modifier.size(LocalSpacing.current.topBarIcon)
                .clickable { openCompanyOwnersNames = true },
                contentAlignment = Alignment.CenterEnd
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        companyOwners.toCompanyOwners().forEach {_companyOwner->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(LocalSpacing.current.topBarIcon),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.padding(LocalSpacing.current.small),
                        imageVector = Icons.Default.Person,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = _companyOwner.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier.size(LocalSpacing.current.topBarIcon)
                        .clickable {
                            val companyOwner = CompanyOwner(_companyOwner.name)
                            val newCompanyOwners = companyOwners.toCompanyOwners().minus(companyOwner)
                            companyOwners = newCompanyOwners.toCompanyOwnersJson()
                            getCompanyOwners(companyOwners)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            }
        }

    }

    BasicTextFieldAlertDialog(
        openDialog = openCompanyOwnersNames,
        title = AddCompanyOwner,
        textContent = emptyString,
        placeholder = CompanyOwnerPlaceholder,
        label = EnterCompanyOwner,
        icon = R.drawable.ic_person_filled,
        keyboardType = KeyboardType.Text,
        unconfirmedUpdatedToastText = null,
        confirmedUpdatedToastText = null,
        getValue = { _ownersName ->
            val ownersName = _ownersName.trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val companyOwner = CompanyOwner(ownersName)
            val mutableListOfOwners = companyOwners.toCompanyOwners().toMutableList()
            if (ownersName.isNotBlank()) { mutableListOfOwners.add(companyOwner) }
            companyOwners = mutableListOfOwners.toSet().toList().toCompanyOwnersJson()
            getCompanyOwners(companyOwners)
        }
    ) {
        openCompanyOwnersNames = false
    }
}
