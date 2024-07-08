package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.item_category.screen

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
import com.example.myshopmanagerapp.core.TypeConverters.toItemCategories
import com.example.myshopmanagerapp.core.TypeConverters.toItemCategoriesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ItemCategory
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.item_category.ItemCategoryContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import kotlinx.coroutines.launch


@Composable
fun ItemCategoryScreen(
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    var openItemCategories by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Item Categories") {
                navigateBack()
            }
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openItemCategories = !openItemCategories
            }
        }
    ){
        val itemCategoriesJson = userPreferences.getItemCategories.collectAsState(initial = emptyString).value
        val itemCategories = itemCategoriesJson.toItemCategories()
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ItemCategoryContent()
        }
        BasicTextFieldAlertDialog(
            openDialog = openItemCategories,
            title = "Add Item Category",
            textContent = emptyString,
            placeholder = "Eg: Groceries",
            label = "Add expense category",
            icon = R.drawable.ic_expense,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = "Item category not added",
            confirmedUpdatedToastText = "Successfully added",
            getValue = { _itemCategory ->
                val mutableItemCategories = mutableListOf<ItemCategory>()
                mutableItemCategories.addAll(itemCategories)
                val newItemCategory = ItemCategory(_itemCategory.trim())
                val newMutableItemCategory = mutableItemCategories.plus(newItemCategory)
                val newMutableItemCategoryJson = newMutableItemCategory.sortedBy { it.itemCategory.first() }.toSet().toList().toItemCategoriesJson()
                coroutineScope.launch {
                    userPreferences.saveItemCategories(newMutableItemCategoryJson)
                }
                Toast.makeText(context,"Item category successfully added", Toast.LENGTH_LONG).show()
            }
        ) {
            openItemCategories = false
        }
    }
}
