package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.susu_collectors

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
import com.example.myshopmanagerapp.core.TypeConverters.toBankPersonnelJson
import com.example.myshopmanagerapp.core.TypeConverters.toBankPersonnelList
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.BankPersonnel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch


@Composable
fun BankPersonnelContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val susuCollectorsJson = userPreferences.getBankPersonnel.collectAsState(initial = emptyString).value

    var openSusuCollectors by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedSusuCollector by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val susuCollectors = susuCollectorsJson.toBankPersonnelList()
            susuCollectors.forEachIndexed { index, susuCollector ->
                CategoryCard(number = "${index.plus(1)}",
                    name = susuCollector.bankPersonnel,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openSusuCollectors = !openSusuCollectors
                                selectedSusuCollector = susuCollector.bankPersonnel
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedSusuCollector = susuCollector.bankPersonnel
                            }
                            else -> {
                                selectedSusuCollector = susuCollector.bankPersonnel
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openSusuCollectors,
                    title = "Edit Susu Collector's route",
                    textContent = emptyString,
                    placeholder = "Eg: Cristiano Ronaldo",
                    label = "Add susu collector",
                    icon = R.drawable.ic_bank,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = "Susu collector's route not edited",
                    confirmedUpdatedToastText = "Successfully changed",
                    getValue = { _susuCollector ->
                        val editedBankPersonnel = BankPersonnel(_susuCollector.trim())
                        val mutableBankPersonnels = mutableListOf<BankPersonnel>()
                        mutableBankPersonnels.addAll(susuCollectors)
                        if (mutableBankPersonnels.remove(BankPersonnel(selectedSusuCollector.trim()))) {
                            mutableBankPersonnels.add(editedBankPersonnel)
                            val mutableSusuCollectorJson =
                                mutableBankPersonnels.sortedBy { it.bankPersonnel.first() }.toSet().toList().toBankPersonnelJson()
                            coroutineScope.launch {
                                userPreferences.saveBankPersonnel(mutableSusuCollectorJson)
                            }
                            Toast.makeText(
                                context,
                                "Susu collector's route successfully edited",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    openSusuCollectors = false
                }
                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Susu Collector",
                    textContent = "Are you sure you want to remove this susu collector's route?",
                    unconfirmedDeletedToastText = "Did not remove susu collector's route",
                    confirmedDeleteToastText = "Susu collector's route deleted successfully",
                    confirmDelete = {
                        val thisBankPersonnel = BankPersonnel(selectedSusuCollector)
                        val mutableBankPersonnels = mutableListOf<BankPersonnel>()
                        mutableBankPersonnels.addAll(susuCollectors)
                        val deletedMutableSusuCollectors = mutableBankPersonnels.minus(thisBankPersonnel)
                        val mutableSusuCollectorsJson =
                            deletedMutableSusuCollectors.sortedBy { it.bankPersonnel.first() }
                                .toBankPersonnelJson()
                        coroutineScope.launch {
                            userPreferences.saveBankPersonnel(mutableSusuCollectorsJson)
                        }
                        Toast.makeText(
                            context,
                            "Susu collector's route successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
