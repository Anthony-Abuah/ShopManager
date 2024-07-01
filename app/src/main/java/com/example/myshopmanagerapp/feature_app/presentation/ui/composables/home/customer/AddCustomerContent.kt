package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCustomerContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCustomerLocation
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCustomerName
import com.example.myshopmanagerapp.core.FormRelatedString.SaveCustomer
import com.example.myshopmanagerapp.core.Functions.nameIsValid
import com.example.myshopmanagerapp.core.Functions.generateUniqueCustomerId
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AddCustomerContent(
    customer: CustomerEntity,
    isSavingCustomer: Boolean,
    customerSavingMessage: String?,
    customerSavingIsSuccessful: Boolean,
    onTakePhoto: () -> Unit,
    addCustomerName: (String) -> Unit,
    addCustomerContact: (String) -> Unit,
    addCustomerLocation: (String) -> Unit,
    addAnyOtherInfo: (String) -> Unit,
    addCustomer: (customer: CustomerEntity) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var customerNameError by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisCustomerName by remember {
                mutableStateOf(customer.customerName)
            }
            BasicTextFieldWithTrailingIconError(
                value = thisCustomerName,
                onValueChange = {
                    thisCustomerName = it
                    addCustomerName(it)
                    customerNameError = nameIsValid(it)
                },
                readOnly = false,
                isError = customerNameError,
                placeholder = CustomerNamePlaceholder,
                label = EnterCustomerName,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisCustomerContact by remember {
                mutableStateOf(customer.customerContact)
            }
            BasicTextFieldWithTrailingIconError(
                value = thisCustomerContact,
                onValueChange = {
                    addCustomerContact(it)
                    thisCustomerContact = it
                },
                readOnly = false,
                isError = false,
                placeholder = CustomerContactPlaceholder,
                label = EnterCustomerContact,
                icon = R.drawable.ic_contact,
                keyboardType = KeyboardType.Phone
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisCustomerLocation by remember {
                mutableStateOf(customer.customerLocation.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = thisCustomerLocation,
                onValueChange = {
                    addCustomerLocation(it)
                    thisCustomerLocation = it
                },
                readOnly = false,
                isError = false,
                placeholder = CustomerLocationPlaceholder,
                label = EnterCustomerLocation,
                icon = R.drawable.ic_location,
                keyboardType = KeyboardType.Text
            )
        }


        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            PhotoTextField { onTakePhoto() }
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var thisCustomerOtherInfo by remember {
                mutableStateOf(customer.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = thisCustomerOtherInfo,
                onValueChange = {
                    addAnyOtherInfo(it)
                    thisCustomerOtherInfo = it
                },
                placeholder = CustomerShortNotesPlaceholder,
                label = CustomerShortNotes,
                icon = R.drawable.ic_short_notes,
                keyboardType = KeyboardType.Text
            )
        }

        Box(modifier = Modifier.padding(
            vertical = LocalSpacing.current.smallMedium,
            horizontal = LocalSpacing.current.small,
        ),
            contentAlignment = Alignment.Center
        ){
            BasicButton(buttonName = SaveCustomer) {
                when(true){
                    customer.customerName.isEmpty() ->{
                        Toast.makeText(context, "Please enter customer route", Toast.LENGTH_LONG).show()
                    }
                    customerNameError ->{
                        Toast.makeText(context, "Please enter valid customer route", Toast.LENGTH_LONG).show()
                    }
                    else ->{
                        val uniqueCustomerId = generateUniqueCustomerId(customer.customerName)
                        val thisCustomer = CustomerEntity(
                            customerId = 0,
                            uniqueCustomerId = uniqueCustomerId,
                            customerName = customer.customerName.trim(),
                            customerContact = customer.customerContact.trim(),
                            customerLocation = customer.customerLocation?.trim()?.ifEmpty { null },
                            customerPhoto = customer.customerPhoto?.ifEmpty { null },
                            otherInfo = customer.otherInfo?.ifEmpty { null },
                            debtAmount = 0.0,
                        )
                        addCustomer(thisCustomer)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingCustomer,
        title = null,
        textContent = customerSavingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (customerSavingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
