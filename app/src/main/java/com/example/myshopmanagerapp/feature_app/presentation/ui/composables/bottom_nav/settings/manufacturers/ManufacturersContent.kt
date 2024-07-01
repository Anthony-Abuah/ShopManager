package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.manufacturers

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.Delete
import com.example.myshopmanagerapp.core.Constants.Edit
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.TypeConverters.toManufacturers
import com.example.myshopmanagerapp.core.TypeConverters.toManufacturersJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.Manufacturer
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch


@Composable
fun ManufacturerContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val manufacturersJson = userPreferences.getManufacturers.collectAsState(initial = emptyString).value

    var openManufacturers by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedManufacturer by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val manufacturers = manufacturersJson.toManufacturers()
            manufacturers.forEachIndexed { index, manufacturer ->
                CategoryCard(number = "${index.plus(1)}",
                    name = manufacturer.manufacturer,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openManufacturers = !openManufacturers
                                selectedManufacturer = manufacturer.manufacturer
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedManufacturer = manufacturer.manufacturer
                            }
                            else -> {
                                selectedManufacturer = manufacturer.manufacturer
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openManufacturers,
                    title = "Edit Manufacturer",
                    textContent = emptyString,
                    placeholder = "Eg: Nataraj Ltd",
                    label = "Add manufacturer's  route",
                    icon = R.drawable.ic_manufacturer,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = "Manufacturer's route not edited",
                    confirmedUpdatedToastText = "Successfully changed",
                    getValue = { _manufacturer ->
                        val editedManufacturer = Manufacturer(_manufacturer.trim())
                        val mutableManufacturers = mutableListOf<Manufacturer>()
                        mutableManufacturers.addAll(manufacturers)
                        if (mutableManufacturers.remove(Manufacturer(selectedManufacturer.trim()))) {
                            mutableManufacturers.add(editedManufacturer)
                            val mutableManufacturerJson =
                                mutableManufacturers.sortedBy { it.manufacturer.first() }.toSet().toList().toManufacturersJson()
                            coroutineScope.launch {
                                userPreferences.saveManufacturers(mutableManufacturerJson)
                            }
                            Toast.makeText(
                                context,
                                "Manufacturer's route successfully edited",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    openManufacturers = false
                }
                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Manufacturer's Name",
                    textContent = "Are you sure you want to remove this manufacturer's route?",
                    unconfirmedDeletedToastText = "Did not remove manufacturer's route",
                    confirmedDeleteToastText = "Expense route deleted successfully",
                    confirmDelete = {
                        val thisManufacturer = Manufacturer(selectedManufacturer)
                        val mutableManufacturers = mutableListOf<Manufacturer>()
                        mutableManufacturers.addAll(manufacturers)
                        val deletedMutableManufacturers = mutableManufacturers.minus(thisManufacturer)
                        val mutableManufacturersJson =
                            deletedMutableManufacturers.sortedBy { it.manufacturer.first() }.toManufacturersJson()
                        coroutineScope.launch {
                            userPreferences.saveManufacturers(mutableManufacturersJson)
                        }
                        Toast.makeText(
                            context,
                            "Manufacturer's route successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
