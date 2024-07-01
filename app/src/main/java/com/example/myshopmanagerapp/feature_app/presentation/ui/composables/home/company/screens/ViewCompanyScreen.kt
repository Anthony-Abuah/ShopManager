package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.company.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.company.ViewCompanyContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun ViewCompanyScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    uniqueCompanyId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        companyViewModel.getCompany(uniqueCompanyId)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Company") {
                navigateBack()
            }
        }
    ){it
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val company = companyViewModel.companyInfo
            ViewCompanyContent(
                companyId = company?.companyId ?: 0,
                uniqueCompanyId = uniqueCompanyId,
                companyName = company?.companyName ?: emptyString,
                companyContact = company?.companyContact ?: emptyString,
                companyLocation = company?.companyLocation ?: emptyString,
                companyOwner = company?.companyOwners ?: emptyString,
                companyProduct = company?.companyProductsAndServices ?: emptyString,
                otherInfo = company?.otherInfo ?: emptyString,
                getUpdatedCompanyName = {name->
                    companyViewModel.updateCompanyName(name)
                },
                getUpdatedCompanyContact = {contact->
                    companyViewModel.updateCompanyContact(contact)
                },
                getUpdatedCompanyLocation = {location->
                    companyViewModel.updateCompanyLocation(location)
                },
                getUpdatedCompanyOwners = {owners->
                    companyViewModel.updateCompanyOwners(owners)
                },
                getUpdatedCompanyProducts = {product->
                    companyViewModel.updateCompanyItemsSold(product)
                },
                getUpdatedCompanyOtherInfo = { otherInfo->
                    companyViewModel.updateCompanyOtherInfo(otherInfo)
                },
                updateCompany = {updatedCompany->
                    companyViewModel.updateCompany(updatedCompany)
                },
                navigateBack = {
                    navigateBack()
                }
            )
        }
    }
}
