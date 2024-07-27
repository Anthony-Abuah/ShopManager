package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.preferences

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.Currency
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.CurrencyPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.FormRelatedString.SelectCurrency
import com.example.myshopmanagerapp.core.FormRelatedString.listOfCurrencies
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SelectOnlyAutoCompleteTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.VerticalDisplayAndEditTextValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import kotlinx.coroutines.launch


@Composable
fun PreferencesContent() {

    var openCurrencyDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val currency = userPreferences.getCurrency.collectAsState(initial = "GHS").value?.take(3) ?: "GHS"

    val descriptionColor = if (isSystemInDarkTheme()) Grey70 else Grey40
    val titleColor = if (isSystemInDarkTheme()) Grey99 else Grey10


    BasicScreenColumnWithoutBottomBar {

        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(LocalSpacing.current.default)
                .fillMaxWidth()
                .clickable { openCurrencyDialog = !openCurrencyDialog },
            contentAlignment = Alignment.Center
        ) {
            VerticalDisplayAndEditTextValues(
                firstText = Currency,
                firstTextColor = titleColor,
                secondText = currency.ifBlank { GHS },
                secondTextColor = descriptionColor,
                value = currency.toNotNull(),
                readOnly = true,
                leadingIcon = R.drawable.ic_money_outline,
                leadingIconWidth = 32.dp,
                onBackgroundColor = titleColor,
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
                    userPreferences.saveCurrency(GHS)
                }
                Toast.makeText(context, "$GHS selected", Toast.LENGTH_LONG).show()
            }
        }
    ) {
        openCurrencyDialog = false
    }
}
