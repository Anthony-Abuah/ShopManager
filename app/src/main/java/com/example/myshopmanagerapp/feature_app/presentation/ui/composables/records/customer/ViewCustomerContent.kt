package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.CustomerNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.DebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCustomerContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCustomerLocation
import com.example.myshopmanagerapp.core.FormRelatedString.EnterCustomerName
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*

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
    updateCustomer: () -> Unit,
    navigateBack: () -> Unit,
){

    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90

    val mainContentColor = if (isSystemInDarkTheme()) Grey90 else Grey10
    val descriptionColor = if (isSystemInDarkTheme()) Grey70 else Grey30

    BasicScreenColumnWithoutBottomBar {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = LocalSpacing.current.default,
                    vertical = LocalSpacing.current.medium
                ),
                contentAlignment = Alignment.Center
            ){
                InfoDisplayCard(
                    image = R.drawable.customer,
                    imageWidth = 100.dp,
                    currency = currency,
                    currencySize = 36.sp,
                    bigText = customer.customerName.toEllipses(25),
                    bigTextSize = 22.sp,
                    smallText = "Unique Id: ${customer.uniqueCustomerId}".toEllipses(40),
                    smallTextSize = 16.sp,
                    backgroundColor = cardBackgroundColor,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }
        }

        Box(
            modifier = Modifier
                .background(alternateBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(
                    horizontal = LocalSpacing.current.smallMedium,
                    vertical = LocalSpacing.current.default,
                ),
                leadingIcon = null,
                firstText = "Customer Information",
                firstTextSize = 14.sp,
                secondText = emptyString,
                readOnly = true
            )
        }


        // Name
        Box(
            modifier = Modifier
                .background(mainBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                firstText = customer.customerName.toEllipses(25),
                secondText = "Name",
                secondTextFontWeight = FontWeight.Normal,
                secondTextColor = MaterialTheme.colorScheme.primary,
                value = customer.customerName,
                trailingIcon = R.drawable.ic_keyboard_arrow_right,
                trailingIconWidth = 32.dp,
                onBackgroundColor = mainContentColor,
                label = EnterCustomerName,
                placeholder = CustomerNamePlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                getUpdatedValue = {
                    updateCustomerName(it)
                }
            )
        }

        // Contact
        Box(
            modifier = Modifier
                .background(alternateBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                firstText = customer.customerContact.toEllipses(25),
                secondText = "Contact",
                secondTextFontWeight = FontWeight.Normal,
                secondTextColor = MaterialTheme.colorScheme.primary,
                value = customer.customerContact,
                trailingIcon = R.drawable.ic_keyboard_arrow_right,
                trailingIconWidth = 32.dp,
                onBackgroundColor = mainContentColor,
                label = EnterCustomerContact,
                placeholder = CustomerContactPlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                keyboardType = KeyboardType.Phone,
                getUpdatedValue = {
                    updateCustomerContact(it)
                }
            )
        }

        // Location
        Column(
            modifier = Modifier
                .background(mainBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalDisplayAndEditTextValues(
                modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium),
                firstText = customer.customerLocation?.toEllipses(25) ?: NotAvailable,
                secondText = "Location",
                secondTextFontWeight = FontWeight.Normal,
                secondTextColor = MaterialTheme.colorScheme.primary,
                value = customer.customerLocation.toNotNull(),
                trailingIcon = R.drawable.ic_keyboard_arrow_right,
                trailingIconWidth = 32.dp,
                onBackgroundColor = mainContentColor,
                label = EnterCustomerLocation,
                placeholder = CustomerLocationPlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                getUpdatedValue = {
                    updateCustomerLocation(it)
                }
            )
            HorizontalDivider()
        }

        // Debt
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = LocalSpacing.current.default,
                    vertical = LocalSpacing.current.medium
                ),
                contentAlignment = Alignment.Center
            ){
                InfoDisplayCard(
                    image = R.drawable.debt,
                    imageWidth = 40.dp,
                    currency = currency,
                    currencySize = 36.sp,
                    bigText = "$currency ${customer.debtAmount?.toTwoDecimalPlaces() ?: NotAvailable}",
                    bigTextSize = 20.sp,
                    smallText = DebtAmount,
                    smallTextSize = 14.sp,
                    backgroundColor = cardBackgroundColor,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }
        }

        // Short Notes
        Box(
            modifier = Modifier
                .background(alternateBackgroundColor)
                .padding(LocalSpacing.current.default)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val otherInfo = customer.otherInfo
            VerticalDisplayAndEditTextValues(
                firstText = ShortNotes,
                firstTextColor = mainContentColor,
                secondText = if (otherInfo.isNullOrBlank()) NotAvailable else otherInfo,
                secondTextColor = descriptionColor,
                value = otherInfo.toNotNull(),
                leadingIcon = R.drawable.ic_short_notes,
                leadingIconWidth = 32.dp,
                onBackgroundColor = mainContentColor,
                label = FormRelatedString.EnterShortDescription,
                placeholder = FormRelatedString.ShortNotesPlaceholder,
                textFieldIcon = R.drawable.ic_edit,
                getUpdatedValue = {
                    updateShortNotes(it)
                }
            )
        }



        /*

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
        */

        // Update
        Box(modifier = Modifier.padding(
            horizontal = LocalSpacing.current.default,
            vertical = LocalSpacing.current.medium)){
            BasicButton(buttonName = UpdateChanges) {
                if (customer.customerName.isEmpty()){
                    Toast.makeText(context, "Please enter customer name", Toast.LENGTH_LONG).show()
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