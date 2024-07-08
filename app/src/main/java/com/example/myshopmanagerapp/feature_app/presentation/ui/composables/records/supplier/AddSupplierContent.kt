package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.supplier

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.AddSupplierRole
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.EnterSupplierContact
import com.example.myshopmanagerapp.core.FormRelatedString.EnterSupplierLocation
import com.example.myshopmanagerapp.core.FormRelatedString.EnterSupplierName
import com.example.myshopmanagerapp.core.FormRelatedString.Save
import com.example.myshopmanagerapp.core.FormRelatedString.SelectSupplierRole
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierContactPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierLocationPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierRoleNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierRolePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SupplierShortNotesPlaceholder
import com.example.myshopmanagerapp.core.Functions.generateUniqueSupplierId
import com.example.myshopmanagerapp.core.Functions.nameIsValid
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
fun AddSupplierContent(
    supplier: SupplierEntity,
    isSavingSupplier: Boolean,
    supplierSavingMessage: String?,
    supplierSavingIsSuccessful: Boolean,
    addSupplierName: (String) -> Unit,
    addSupplierContact: (String) -> Unit,
    addSupplierLocation: (String) -> Unit,
    addSupplierRole: (String) -> Unit,
    addSupplierOtherInfo: (String) -> Unit,
    addSupplier: (SupplierEntity)-> Unit,
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var supplierRoles = UserPreferences(context).getSupplierRole.collectAsState(initial = null).value

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openSupplierRolesDialog by remember {
        mutableStateOf(false)
    }
    var supplierNameError by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar {

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var supplierName by remember {
                mutableStateOf(supplier.supplierName)
            }
            BasicTextFieldWithTrailingIconError(
                value = supplierName,
                onValueChange = {
                    supplierName = it
                    addSupplierName(it)
                    supplierNameError = nameIsValid(it)
                },
                isError = false,
                readOnly = false,
                placeholder = SupplierNamePlaceholder,
                label = EnterSupplierName,
                icon = R.drawable.ic_person_filled,
                keyboardType = KeyboardType.Text
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var supplierContact by remember {
                mutableStateOf(supplier.supplierContact)
            }
            BasicTextFieldWithTrailingIcon(
                value = supplierContact,
                onValueChange = {
                    supplierContact = it
                    addSupplierContact(it)
                },
                placeholder = SupplierContactPlaceholder,
                label = EnterSupplierContact,
                icon = R.drawable.ic_contact,
                keyboardType = KeyboardType.Phone
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var supplierLocation by remember {
                mutableStateOf(supplier.supplierLocation.toNotNull())
            }
            BasicTextFieldWithTrailingIconError(
                value = supplierLocation,
                onValueChange = {
                    supplierLocation = it
                    addSupplierLocation(it)
                },
                isError = false,
                readOnly = false,
                placeholder = SupplierLocationPlaceholder,
                label = EnterSupplierLocation,
                icon = R.drawable.ic_location,
                keyboardType = KeyboardType.Text
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            AutoCompleteWithAddButton(
                label = SelectSupplierRole,
                placeholder = SupplierRolePlaceholder,
                listItems = supplierRoles.toSupplierRoles().map { it.supplierRole },
                readOnly = false,
                expandedIcon = R.drawable.ic_role,
                unexpandedIcon = R.drawable.ic_role,
                onClickAddButton = {
                    openSupplierRolesDialog = !openSupplierRolesDialog
                },
                getSelectedItem = { addSupplierRole(it) }
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var anyOtherInfo by remember {
                mutableStateOf(supplier.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = anyOtherInfo,
                onValueChange = {
                    anyOtherInfo = it
                    addSupplierOtherInfo(it)
                },
                placeholder = SupplierShortNotesPlaceholder,
                label = EnterShortDescription,
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
            BasicButton(buttonName = Save) {
                val uniqueSupplierId = generateUniqueSupplierId(supplier.supplierName)
                val newSupplier = supplier.copy(uniqueSupplierId = uniqueSupplierId)
                addSupplier(newSupplier)
                confirmationInfoDialog = !confirmationInfoDialog
            }
        }

    }
    BasicTextFieldAlertDialog(
        openDialog = openSupplierRolesDialog,
        title = AddSupplierRole,
        textContent = emptyString,
        placeholder = SupplierRolePlaceholder,
        label = AddSupplierRole,
        icon = R.drawable.ic_person_outline,
        keyboardType = KeyboardType.Text,
        unconfirmedUpdatedToastText = SupplierRoleNotAdded,
        confirmedUpdatedToastText = null,
        getValue = { _supplierRole ->
            val newSupplierRole = SupplierRole(_supplierRole.trim())
            val supplierRolesList = supplierRoles.toSupplierRoles()
            val mutableSupplierRoles = mutableListOf<SupplierRole>()
            if (_supplierRole.isBlank()){
                Toast.makeText(context, FormRelatedString.EnterExpenseName, Toast.LENGTH_LONG).show()
                openSupplierRolesDialog = false
            }
            else if (supplierRolesList.map { it.supplierRole.trim().lowercase(Locale.ROOT) }.contains(_supplierRole.trim().lowercase(
                    Locale.ROOT))) {
                Toast.makeText(context, "Supplier role: $_supplierRole already exists", Toast.LENGTH_LONG).show()
                openSupplierRolesDialog = false
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
        openSupplierRolesDialog = false
    }


    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingSupplier,
        title = null,
        textContent = supplierSavingMessage.toNotNull(),
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (supplierSavingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }

}
