package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.manufacturers.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.TypeConverters.toManufacturers
import com.example.myshopmanagerapp.core.TypeConverters.toManufacturersJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.Manufacturer
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.manufacturers.ManufacturerContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun ManufacturerScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openManufacturers by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Manufacturers") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openManufacturers = !openManufacturers
            }
        }
    ){
        val manufacturersJson = userPreferences.getManufacturers.collectAsState(initial = emptyString).value
        val manufacturers = manufacturersJson.toManufacturers()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ManufacturerContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openManufacturers,
            title = "Add Manufacturer's Name",
            textContent = emptyString,
            placeholder = "Eg: Transportation",
            label = "Add manufacturer's route",
            icon = R.drawable.ic_manufacturer,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Manufacturer's route not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _manufacturer ->
                val mutableManufacturers = mutableListOf<Manufacturer>()
                mutableManufacturers.addAll(manufacturers)
                val newManufacturer = Manufacturer(_manufacturer.trim())
                val newMutableManufacturer = mutableManufacturers.plus(newManufacturer)
                val newMutableManufacturerJson = newMutableManufacturer.sortedBy { it.manufacturer.first() }.toSet().toList().toManufacturersJson()
                coroutineScope.launch {
                    userPreferences.saveManufacturers(newMutableManufacturerJson)
                }
                Toast.makeText(context,"Manufacturer's route successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openManufacturers = false
        }
    }
}
