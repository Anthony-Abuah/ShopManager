package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.supplier

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.SupplierRole
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.AddSupplierRole
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierContact
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierInformation
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierLocation
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierName
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierRoleNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierRolePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueSupplierId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateSupplierContact
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateSupplierLocation
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateSupplierName
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toSupplierRoles
import com.example.myshopmanagerapp.core.TypeConverters.toSupplierRolesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity
import com.example.myshopmanagerapp.feature_app.domain.model.SupplierRole
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ViewSupplierContent(
    supplier: SupplierEntity,
    isUpdatingSupplier: Boolean,
    supplierUpdateMessage: String?,
    supplierUpdatingIsSuccessful: Boolean,
    getUpdatedSupplierName: (supplierName: String)-> Unit,
    getUpdatedSupplierContact: (supplierContact: String)-> Unit,
    getUpdatedSupplierRole: (supplierRole: String)-> Unit,
    getUpdatedSupplierLocation: (supplierLocation: String)-> Unit,
    getUpdatedSupplierOtherInfo: (shortNotes: String)-> Unit,
    updateSupplier: (SupplierEntity)-> Unit,
    navigateBack: ()-> Unit,
){

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var supplierRoles = UserPreferences(context).getSupplierRole.collectAsState(initial = null).value

    var openSupplierRoleDialog by remember {
        mutableStateOf(false)
    }
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        // Supplier Photo
        ViewPhoto(icon = R.drawable.ic_person_filled)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        //Supplier Info
        ViewInfo(SupplierInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Supplier Id
        ViewTextValueRow(
            viewTitle = UniqueSupplierId,
            viewValue = supplier.uniqueSupplierId
        )

        HorizontalDivider()

        // Supplier Name
        ViewOrUpdateTextValueRow(
            viewTitle = SupplierName,
            viewValue = supplier.supplierName,
            placeholder = SupplierNamePlaceholder,
            label = UpdateSupplierName,
            icon = R.drawable.ic_person_outline,
            getUpdatedValue = { getUpdatedSupplierName(it) }
        )

        HorizontalDivider()


        // Supplier Contact
        ViewOrUpdateTextValueRow(
            viewTitle = SupplierContact,
            viewValue = supplier.supplierContact,
            placeholder = SupplierContactPlaceholder,
            label = UpdateSupplierContact,
            icon = R.drawable.ic_contact,
            getUpdatedValue = { getUpdatedSupplierContact(it) }
        )

        HorizontalDivider()

        // Supplier PersonnelRole
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = SupplierRole,
            viewValue = supplier.supplierRole ?: NotAvailable,
            placeholder = SupplierRolePlaceholder,
            label = AddSupplierRole,
            expandedIcon = R.drawable.ic_money_filled,
            unexpandedIcon = R.drawable.ic_money_outline,
            listItems = supplierRoles.toSupplierRoles().map { it.supplierRole },
            onClickAddButton = { openSupplierRoleDialog = !openSupplierRoleDialog },
            getUpdatedValue = { getUpdatedSupplierRole(it) }
        )

        HorizontalDivider()

        // Supplier Location
        ViewOrUpdateTextValueRow(
            viewTitle = SupplierLocation,
            viewValue = supplier.supplierLocation.toNotNull(),
            placeholder = SupplierLocationPlaceholder,
            label = UpdateSupplierLocation,
            icon = R.drawable.ic_location,
            getUpdatedValue = { getUpdatedSupplierLocation(it) }
        )

        HorizontalDivider()

        // Supplier Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = supplier.otherInfo.toNotNull(),
            placeholder = SupplierShortNotesPlaceholder,
            label = UpdateShortNotes,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = { getUpdatedSupplierOtherInfo(it) }
        )

        HorizontalDivider()

        Box(modifier = Modifier.padding(vertical = LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                updateSupplier(supplier)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        }
    }
    BasicTextFieldAlertDialog(
        openDialog = openSupplierRoleDialog,
        title = AddSupplierRole,
        textContent = emptyString,
        placeholder = SupplierRolePlaceholder,
        label = AddSupplierRole,
        icon = R.drawable.ic_role,
        keyboardType = KeyboardType.Text,
        unconfirmedUpdatedToastText = SupplierRoleNotAdded,
        confirmedUpdatedToastText = null,
        getValue = { _supplierRole ->
            val newSupplierRole = SupplierRole(_supplierRole.trim())
            val supplierRolesList = supplierRoles.toSupplierRoles()
            val mutableSupplierRoles = mutableListOf<SupplierRole>()
            if (_supplierRole.isBlank()){
                Toast.makeText(context, FormRelatedString.EnterExpenseName, Toast.LENGTH_LONG).show()
                openSupplierRoleDialog = false
            }
            else if (supplierRolesList.map { it.supplierRole.trim().lowercase(Locale.ROOT) }.contains(_supplierRole.trim().lowercase(
                    Locale.ROOT))) {
                Toast.makeText(context, "Supplier role: $_supplierRole already exists", Toast.LENGTH_LONG).show()
                openSupplierRoleDialog = false
            } else {
                mutableSupplierRoles.addAll(supplierRolesList)
                mutableSupplierRoles.add(newSupplierRole)
                supplierRoles = mutableSupplierRoles.toSet().sortedBy { it.supplierRole.first() }.toSupplierRolesJson()
                coroutineScope.launch {
                    UserPreferences(context).saveExpenseNames(supplierRoles.toNotNull())
                }
                Toast.makeText(context, "Supplier Role: $_supplierRole successfully added", Toast.LENGTH_LONG).show()
            }
        }
    ) {
        openSupplierRoleDialog = false
    }

    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isUpdatingSupplier,
        title = null,
        textContent = supplierUpdateMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (supplierUpdatingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }

}