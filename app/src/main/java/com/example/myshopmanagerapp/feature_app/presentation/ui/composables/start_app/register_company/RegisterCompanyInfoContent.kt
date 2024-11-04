package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.register_company

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CompanyNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyLocation
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCompanyName
import com.example.myshopmanagerapp.core.Functions.textIsInvalid
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldWithTrailingIconError
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Cyan30
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.scriptBold


@Composable
fun RegisterCompanyInfoContent(
    company: CompanyEntity,
    addCompanyName: (String) -> Unit,
    addCompanyContact: (String) -> Unit,
    addCompanyLocation: (String) -> Unit,
    navigateToNextScreen: () -> Unit,
) {
    /*
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight(0.45f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopCenter
        ){

            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = LocalSpacing.current.extraLarge),
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Shop Manager",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = scriptBold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(0.7f)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
                )
                .background(MaterialTheme.colorScheme.background),
        )

        Card(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth()
                .padding(
                    horizontal = LocalSpacing.current.smallMedium,
                    vertical = LocalSpacing.current.smallMedium
                )
                .align(Alignment.Center),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(LocalSpacing.current.default))
        {

            val context = LocalContext.current
            BasicScreenColumnWithoutBottomBar {
                */
    /*Spacer(modifier = Modifier.height(LocalSpacing.current.large))

                Box(
                    modifier = Modifier
                        .size(150.dp)
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
                }*/
    /*

                Spacer(modifier = Modifier.height(LocalSpacing.current.large))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = scriptBold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(LocalSpacing.current.large))

                // Company name
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.Center
                ) {
                    var companyName by remember {
                        mutableStateOf(company.companyName)
                    }
                    BasicTextFieldWithTrailingIconError(
                        value = companyName,
                        onValueChange = {
                            companyName = it
                            addCompanyName(companyName)
                        },
                        isError = textIsInvalid(companyName) || companyName.isEmpty(),
                        readOnly = false,
                        placeholder = CompanyNamePlaceholder,
                        label = EnterCompanyName,
                        icon = R.drawable.ic_shop,
                        keyboardType = KeyboardType.Text
                    )
                }

                // Company contact
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.Center
                ) {
                    var companyContact by remember {
                        mutableStateOf(company.companyContact)
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
                    modifier = Modifier.padding(LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.Center
                ) {
                    var companyLocation by remember {
                        mutableStateOf(company.companyLocation.toNotNull())
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

                Spacer(modifier = Modifier.weight(1f))



                // Save button
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.Center
                ) {
                    BasicButton(buttonName = "Next") {
                        when (true) {
                            company.companyName.isBlank() -> {
                                Toast.makeText(context, "Enter company name", Toast.LENGTH_LONG)
                                    .show()
                            }
                            (company.companyName.length < 3) -> {
                                Toast.makeText(
                                    context,
                                    "Enter valid company name",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else -> {
                                navigateToNextScreen()
                            }
                        }
                    }

                }

                }


        }
    }
    */

    val context = LocalContext.current
    BasicScreenColumnWithoutBottomBar {
        Spacer(modifier = Modifier.height(LocalSpacing.current.large))

        Box(
            modifier = Modifier
                .size(150.dp)
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

        // Company name
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var companyName by remember {
                mutableStateOf(company.companyName)
            }
            BasicTextFieldWithTrailingIconError(
                value = companyName,
                onValueChange = {
                    companyName = it
                    addCompanyName(companyName)
                },
                isError = textIsInvalid(companyName) || companyName.isEmpty() ,
                readOnly = false,
                placeholder = CompanyNamePlaceholder,
                label = EnterCompanyName,
                icon = R.drawable.ic_shop,
                keyboardType = KeyboardType.Text
            )
        }

        // Company contact
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var companyContact by remember {
                mutableStateOf(company.companyContact)
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
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            var companyLocation by remember {
                mutableStateOf(company.companyLocation.toNotNull())
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


        // Save button
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            BasicButton(buttonName = "Next") {
                when(true){
                    company.companyName.isBlank()->{
                        Toast.makeText(context, "Enter company name", Toast.LENGTH_LONG).show()
                    }
                    (company.companyName.length < 3 )->{
                        Toast.makeText(context, "Enter valid company name", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        navigateToNextScreen()
                    }
                }
            }

        }

    }

}
