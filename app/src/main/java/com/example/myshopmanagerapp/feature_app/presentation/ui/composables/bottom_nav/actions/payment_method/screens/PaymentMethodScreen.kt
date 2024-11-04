package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.payment_method.screens

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
import com.example.myshopmanagerapp.core.FormRelatedString.PaymentMethodPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SelectPaymentMethod
import com.example.myshopmanagerapp.core.TypeConverters.toListOfPaymentMethods
import com.example.myshopmanagerapp.core.TypeConverters.toListOfPaymentMethodsJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PaymentMethod
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.payment_method.PaymentMethodContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun PaymentMethodScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openPaymentMethods by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Payment Method") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openPaymentMethods = !openPaymentMethods
            }
        }
    ){
        val paymentMethodJson = userPreferences.getPaymentMethod.collectAsState(initial = emptyString).value
        val paymentMethod = paymentMethodJson.toListOfPaymentMethods()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PaymentMethodContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openPaymentMethods,
            title = "Add Payment Method",
            textContent = emptyString,
            placeholder = PaymentMethodPlaceholder,
            label = SelectPaymentMethod,
            icon = R.drawable.ic_money_filled,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Payment method not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _paymentMethod ->
                val mutablePaymentMethods = mutableListOf<PaymentMethod>()
                mutablePaymentMethods.addAll(paymentMethod)
                val newPaymentMethod = PaymentMethod(_paymentMethod.trim())
                val newMutablePaymentMethod = mutablePaymentMethods.plus(newPaymentMethod)
                val newMutablePaymentMethodJson = newMutablePaymentMethod.sortedBy { it.paymentMethod.first() }.toSet().toList().toListOfPaymentMethodsJson()
                coroutineScope.launch {
                    userPreferences.savePaymentMethod(newMutablePaymentMethodJson)
                }
                Toast.makeText(context,"Payment method successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openPaymentMethods = false
        }
    }
}
