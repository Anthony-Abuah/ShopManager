package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.preferences

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.CurrencyPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SelectCurrency
import com.example.myshopmanagerapp.core.FormRelatedString.listOfCurrencies
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.SettingsContentCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SelectOnlyAutoCompleteTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch


@Composable
fun PreferencesContent() {

    var openCurrencyDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)

    BasicScreenColumnWithoutBottomBar {

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = LocalSpacing.current.smallMedium,
                end = LocalSpacing.current.smallMedium,
                bottom = LocalSpacing.current.smallMedium,
            )
            .clickable { openCurrencyDialog = !openCurrencyDialog },
        ) {
            val currency = userPreferences.getCurrency.collectAsState(initial = "GHS").value?.take(3) ?: "GHS"
            SettingsContentCard(
                icon = R.drawable.ic_money_filled,
                title = "Currency",
                info = currency
            )
        }
    }
    SelectOnlyAutoCompleteTextFieldAlertDialog(
        openDialog = openCurrencyDialog,
        title = SelectCurrency,
        textContent = emptyString,
        placeholder = CurrencyPlaceholder,
        label = SelectCurrency,
        expandedIcon = R.drawable.ic_money_filled,
        unexpandedIcon = R.drawable.ic_money_outline,
        unconfirmedUpdatedToastText = emptyString,
        confirmedUpdatedToastText = null,
        listItems = listOfCurrencies,
        getSelectedItem = {_currency->
            if (_currency.isNotBlank()) {
                coroutineScope.launch {
                    userPreferences.saveCurrency(_currency.take(3))
                }
                Toast.makeText(context, "${_currency.take(3)} selected", Toast.LENGTH_LONG).show()
            }else{
                coroutineScope.launch {
                    userPreferences.saveCurrency("GHS")
                }
                Toast.makeText(context, "GHS selected", Toast.LENGTH_LONG).show()
            }
        }
    ) {
        openCurrencyDialog = false
    }
}
