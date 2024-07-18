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
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyOwnerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyProductPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyOwner
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyProducts
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldWithTrailingIconError
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DescriptionTextFieldWithTrailingIcon
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun RegisterCompanyMoreInfoContent(
    company: CompanyEntity,
    addCompanyOwners: (String) -> Unit,
    addCompanyProducts: (String) -> Unit,
    addCompanyOtherInfo: (String) -> Unit,
    navigateToNextScreen: () -> Unit,
) {


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
                painter = painterResource(id = R.drawable.logo),
                contentDescription = emptyString
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.large))

        // Company Owners
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var companyOwners by remember {
                mutableStateOf(company.companyOwners.toNotNull())
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
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var companyProductsAndServices by remember {
                mutableStateOf(company.companyProductsAndServices.toNotNull())
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

        // short notes/description
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var otherInfo by remember {
                mutableStateOf(company.otherInfo.toNotNull())
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
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            BasicButton(buttonName = "Next") {
                navigateToNextScreen()
            }

        }
    }
}
