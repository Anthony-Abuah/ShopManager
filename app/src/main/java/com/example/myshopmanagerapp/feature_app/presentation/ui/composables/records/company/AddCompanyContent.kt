package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyEmailPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyOwnerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyPasswordPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyProductPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyEmail
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyLocation
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyOwner
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyProducts
import com.example.myshopmanagerapp.core.FormRelatedString.EnterPassword
import com.example.myshopmanagerapp.core.FormRelatedString.SaveCompany
import com.example.myshopmanagerapp.core.Functions.textIsInvalid
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AddCompanyContent(
    companyEntity: CompanyEntity,
    isSavingCompany: Boolean,
    companySavingMessage: String?,
    companySavingIsSuccessful: Boolean,
    addCompanyName: (String) -> Unit,
    addCompanyContact: (String) -> Unit,
    addCompanyLocation: (String) -> Unit,
    addCompanyOwners: (String) -> Unit,
    addCompanyEmail: (String) -> Unit,
    addCompanyPassword: (String) -> Unit,
    addCompanyProducts: (String) -> Unit,
    addCompanyOtherInfo: (String) -> Unit,
    addCompany: (CompanyEntity)-> Unit,
    navigateBack: ()-> Unit,
) {

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {

        // Company route
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var companyName by remember {
                mutableStateOf(companyEntity.companyName)
            }
            BasicTextFieldWithTrailingIconError(
                value = companyName,
                onValueChange = {
                    companyName = it
                    addCompanyName(companyName)
                },
                isError = textIsInvalid(companyName),
                readOnly = false,
                placeholder = CompanyNamePlaceholder,
                label = EnterCompanyName,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        // Company contact
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var companyContact by remember {
                mutableStateOf(companyEntity.companyContact)
            }
            BasicTextFieldWithTrailingIconError(
                value = companyContact,
                onValueChange = {
                    companyContact = it
                    addCompanyContact(companyContact)
                },
                isError = false,
                readOnly = false,
                placeholder = CompanyContactPlaceholder,
                label = EnterCompanyContact,
                icon = R.drawable.ic_contact,
                keyboardType = KeyboardType.Phone
            )
        }

        // Company location
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var companyLocation by remember {
                mutableStateOf(companyEntity.companyLocation.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = companyLocation,
                onValueChange = {
                    companyLocation = it
                    addCompanyLocation(companyLocation)
                },
                isError = textIsInvalid(companyLocation),
                readOnly = false,
                placeholder = CompanyLocationPlaceholder,
                label = EnterCompanyLocation,
                icon = R.drawable.ic_location,
                keyboardType = KeyboardType.Text
            )
        }

        // Company Owners
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var companyOwners by remember {
                mutableStateOf(companyEntity.companyOwners.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = companyOwners,
                onValueChange = {
                    companyOwners = it
                    addCompanyOwners(companyOwners)
                },
                isError = false,
                readOnly = false,
                placeholder = CompanyOwnerPlaceholder,
                label = EnterCompanyOwner,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        // Company products or services
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var companyProductsAndServices by remember {
                mutableStateOf(companyEntity.companyProductsAndServices.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = companyProductsAndServices,
                onValueChange = {
                    companyProductsAndServices = it
                    addCompanyProducts(companyProductsAndServices)
                },
                isError = false,
                readOnly = false,
                placeholder = CompanyProductPlaceholder,
                label = EnterCompanyProducts,
                icon = R.drawable.ic_product,
                keyboardType = KeyboardType.Text
            )
        }

        // Company Email
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var email by remember {
                mutableStateOf(companyEntity.email.toNotNull())
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
                keyboardType = KeyboardType.Text
            )
        }

        // Company Password
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var password by remember {
                mutableStateOf(companyEntity.password.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = password,
                onValueChange = {
                    password = it
                    addCompanyPassword(password)
                },
                isError = false,
                readOnly = false,
                placeholder = CompanyPasswordPlaceholder,
                label = EnterPassword,
                icon = R.drawable.ic_email,
                keyboardType = KeyboardType.Text
            )
        }


        // short notes/description
        Box(
            modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            var otherInfo by remember {
                mutableStateOf(companyEntity.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = otherInfo,
                onValueChange = {
                    otherInfo = it
                    addCompanyOtherInfo(otherInfo)
                },
                placeholder = CompanyShortNotesPlaceholder,
                label = CompanyShortNotes,
                icon = R.drawable.ic_short_notes,
                keyboardType = KeyboardType.Text
            )
        }

        // Save button
        Box(
            modifier = Modifier.padding(
                vertical = LocalSpacing.current.smallMedium,
                horizontal = LocalSpacing.current.small,
            ),
            contentAlignment = Alignment.Center
        ) {
            BasicButton(buttonName = SaveCompany) {
                addCompany(companyEntity)
                confirmationInfoDialog = !confirmationInfoDialog
            }

        }

    }
    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingCompany,
        title = null,
        textContent = companySavingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (companySavingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
