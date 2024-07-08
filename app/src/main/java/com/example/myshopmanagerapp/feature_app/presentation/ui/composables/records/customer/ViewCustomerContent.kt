package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerContact
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerDebt
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerInformation
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerLocation
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerName
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueCustomerId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateCustomerContact
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateCustomerLocation
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateCustomerName
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateShortNotes
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ViewCustomerContent(
    customer: CustomerEntity,
    currency: String,
    customerUpdatingMessage: String?,
    isUpdatingCustomer: Boolean,
    customerUpdatingIsSuccessful: Boolean,
    updateCustomerName: (customerName: String) -> Unit,
    updateCustomerContact: (customerContact: String) -> Unit,
    updateCustomerLocation: (customerLocation: String) -> Unit,
    updateShortNotes: (shortNotes: String) -> Unit,
    takePhoto: () -> Unit,
    updateCustomer: () -> Unit,
    navigateBack: () -> Unit,
){

    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        // Customer Photo
        ViewPhoto(icon = R.drawable.ic_person_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        //Customer Info
        ViewInfo(CustomerInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Customer Id
        ViewTextValueRow(
            viewTitle = UniqueCustomerId,
            viewValue = customer.uniqueCustomerId
        )

        HorizontalDivider()

        // Customer Name
        ViewOrUpdateTextValueRow(
            viewTitle = CustomerName,
            viewValue = customer.customerName,
            placeholder = CustomerNamePlaceholder,
            label = UpdateCustomerName,
            icon = R.drawable.ic_person_outline,
            getUpdatedValue = { updateCustomerName(it) }
        )

        HorizontalDivider()

        // Customer Contact
        ViewOrUpdateTextValueRow(
            viewTitle = CustomerContact,
            viewValue = customer.customerContact,
            placeholder = CustomerContactPlaceholder,
            label = UpdateCustomerContact,
            icon = R.drawable.ic_contact,
            getUpdatedValue = { updateCustomerContact(it) }
        )

        HorizontalDivider()


        // Customer Location
        ViewOrUpdateTextValueRow(
            viewTitle = CustomerLocation,
            viewValue = customer.customerLocation.toNotNull(),
            placeholder = CustomerLocationPlaceholder,
            label = UpdateCustomerLocation,
            icon = R.drawable.ic_location,
            getUpdatedValue = { updateCustomerLocation(it) }
        )

        HorizontalDivider()

        // Customer Debt
        ViewTextValueRow(viewTitle = CustomerDebt, viewValue = "$currency ${customer.debtAmount}")

        HorizontalDivider()

        // Customer Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = customer.otherInfo.toNotNull(),
            placeholder = CustomerShortNotesPlaceholder,
            label = UpdateShortNotes,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = { updateShortNotes(it) }
        )

        HorizontalDivider()

        Box(modifier = Modifier.padding(
            horizontal = LocalSpacing.current.small,
            vertical = LocalSpacing.current.medium)){
            BasicButton(buttonName = UpdateChanges) {
                if (customer.customerName.isEmpty()){
                    Toast.makeText(context, "Please enter customer route", Toast.LENGTH_LONG).show()
                }else {
                    updateCustomer()
                    confirmationInfoDialog = !confirmationInfoDialog
                }
            }
        }
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingCustomer,
        title = null,
        textContent = customerUpdatingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (customerUpdatingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }


}