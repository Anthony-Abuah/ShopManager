package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.bank_personnel.screen

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
import com.example.myshopmanagerapp.core.TypeConverters.toBankPersonnelJson
import com.example.myshopmanagerapp.core.TypeConverters.toBankPersonnelList
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.BankPersonnel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.bank_personnel.BankPersonnelContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun BankPersonnelScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openSusuCollectors by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Bank Personnel") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openSusuCollectors = !openSusuCollectors
            }
        }
    ){
        val bankPersonnelJson = userPreferences.getBankPersonnel.collectAsState(initial = emptyString).value
        val bankPersonnel = bankPersonnelJson.toBankPersonnelList()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BankPersonnelContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openSusuCollectors,
            title = "Add Susu Collector",
            textContent = emptyString,
            placeholder = "Eg: Mary Mensah",
            label = "Add bank personnel",
            icon = R.drawable.ic_bank,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Bank personnel's name not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _bankPersonnel ->
                val mutableBankPersonnel = mutableListOf<BankPersonnel>()
                mutableBankPersonnel.addAll(bankPersonnel)
                val newBankPersonnel = BankPersonnel(_bankPersonnel.trim())
                val newMutableSusuCollector = mutableBankPersonnel.plus(newBankPersonnel)
                val newMutableSusuCollectorJson = newMutableSusuCollector.sortedBy { it.bankPersonnel.first() }.toSet().toList().toBankPersonnelJson()
                coroutineScope.launch {
                    userPreferences.saveBankPersonnel(newMutableSusuCollectorJson)
                }
                Toast.makeText(context,"Bank Personnel's name successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openSusuCollectors = false
        }
    }
}
