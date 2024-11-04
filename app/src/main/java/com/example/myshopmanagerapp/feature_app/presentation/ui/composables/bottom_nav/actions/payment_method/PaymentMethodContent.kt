package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.payment_method

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
import com.example.myshopmanagerapp.core.FormRelatedString.PaymentMethodPlaceholder
import com.example.myshopmanagerapp.core.TypeConverters.toListOfPaymentMethods
import com.example.myshopmanagerapp.core.TypeConverters.toListOfPaymentMethodsJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.PaymentMethod
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch


@Composable
fun PaymentMethodContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val paymentMethodsJson = userPreferences.getPaymentMethod.collectAsState(initial = emptyString).value

    var openPaymentMethods by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedPaymentMethod by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val paymentMethods = paymentMethodsJson.toListOfPaymentMethods()
            paymentMethods.forEachIndexed { index, paymentMethod ->
                CategoryCard(number = "${index.plus(1)}",
                    name = paymentMethod.paymentMethod,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openPaymentMethods = !openPaymentMethods
                                selectedPaymentMethod = paymentMethod.paymentMethod
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedPaymentMethod = paymentMethod.paymentMethod
                            }
                            else -> {
                                selectedPaymentMethod = paymentMethod.paymentMethod
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openPaymentMethods,
                    title = "Edit Payment Methods",
                    textContent = emptyString,
                    placeholder = PaymentMethodPlaceholder,
                    label = "Add payment method",
                    icon = R.drawable.ic_money_filled,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = "Payment method not changed",
                    confirmedUpdatedToastText = "Successfully changed",
                    getValue = { _paymentMethod ->
                        val editedPaymentMethod = PaymentMethod(_paymentMethod.trim())
                        val mutableListOfPaymentMethods = mutableListOf<PaymentMethod>()
                        mutableListOfPaymentMethods.addAll(paymentMethods)
                        if (mutableListOfPaymentMethods.remove(PaymentMethod(selectedPaymentMethod.trim()))) {
                            mutableListOfPaymentMethods.add(editedPaymentMethod)
                            val mutableExpenseTypeJson =
                                mutableListOfPaymentMethods.sortedBy { it.paymentMethod }.toSet().toList().toListOfPaymentMethodsJson()
                            coroutineScope.launch {
                                userPreferences.saveExpenseTypes(mutableExpenseTypeJson)
                            }
                            Toast.makeText(
                                context,
                                "Payment method successfully edited",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    openPaymentMethods = false
                }
                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Payment Method",
                    textContent = "Are you sure you want to remove this payment method?",
                    unconfirmedDeletedToastText = "Did not remove payment method",
                    confirmedDeleteToastText = "Payment method deleted successfully",
                    confirmDelete = {
                        val thisPaymentMethod = PaymentMethod(selectedPaymentMethod)
                        val mutableListOfPaymentMethods = mutableListOf<PaymentMethod>()
                        mutableListOfPaymentMethods.addAll(paymentMethods)
                        val deletedMutablePaymentMethod = mutableListOfPaymentMethods.minus(thisPaymentMethod)
                        val mutablePaymentMethodsJson =
                            deletedMutablePaymentMethod.sortedBy { it.paymentMethod }
                                .toListOfPaymentMethodsJson()
                        coroutineScope.launch {
                            userPreferences.savePaymentMethod(mutablePaymentMethodsJson)
                        }
                        Toast.makeText(
                            context,
                            "Payment method successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
