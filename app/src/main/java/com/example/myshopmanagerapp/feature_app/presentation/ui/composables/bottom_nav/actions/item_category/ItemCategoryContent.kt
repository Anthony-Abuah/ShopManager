package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.item_category

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
import com.example.myshopmanagerapp.core.TypeConverters.toItemCategories
import com.example.myshopmanagerapp.core.TypeConverters.toItemCategoriesJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.domain.model.ItemCategory
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicTextFieldAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CategoryCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import kotlinx.coroutines.launch


@Composable
fun ItemCategoryContent(
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)
    val itemCategoriesJson = userPreferences.getItemCategories.collectAsState(initial = emptyString).value

    var openItemCategories by remember {
        mutableStateOf(false)
    }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedItemCategory by remember {
        mutableStateOf(emptyString)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.25.dp,
            color = MaterialTheme.colorScheme.onBackground
        )


        BasicScreenColumnWithoutBottomBar {
            val itemCategories = itemCategoriesJson.toItemCategories()
            itemCategories.forEachIndexed { index, itemCategory ->
                CategoryCard(
                    number = "${index.plus(1)}",
                    name = itemCategory.itemCategory,
                    onClickItem = { item ->
                        when (item) {
                            Edit -> {
                                openItemCategories = !openItemCategories
                                selectedItemCategory = itemCategory.itemCategory
                            }
                            Delete -> {
                                openDeleteDialog = !openDeleteDialog
                                selectedItemCategory = itemCategory.itemCategory
                            }
                            else -> {
                                selectedItemCategory = itemCategory.itemCategory
                            }
                        }
                    }
                )
                BasicTextFieldAlertDialog(
                    openDialog = openItemCategories,
                    title = "Edit Item Category",
                    textContent = emptyString,
                    placeholder = "Eg: Groceries",
                    label = "Add item category",
                    icon = R.drawable.ic_category,
                    keyboardType = KeyboardType.Text,
                    unconfirmedUpdatedToastText = "Item category not edited",
                    confirmedUpdatedToastText = "Successfully changed",
                    getValue = { _itemCategory ->
                        val editedItemCategory = ItemCategory(_itemCategory.trim())
                        val mutableItemCategories = mutableListOf<ItemCategory>()
                        mutableItemCategories.addAll(itemCategories)
                        if (mutableItemCategories.remove(ItemCategory(selectedItemCategory.trim()))) {
                            mutableItemCategories.add(editedItemCategory)
                            val mutableItemCategoryJson =
                                mutableItemCategories.sortedBy { it.itemCategory }.toSet().toList().toItemCategoriesJson()
                            coroutineScope.launch {
                                userPreferences.saveItemCategories(mutableItemCategoryJson)
                            }
                            Toast.makeText(
                                context,
                                "Item category successfully edited",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    openItemCategories = false
                }
                DeleteConfirmationDialog(
                    openDialog = openDeleteDialog,
                    title = "Remove Item Category",
                    textContent = "Are you sure you want to remove this item category?",
                    unconfirmedDeletedToastText = "Did not remove item category",
                    confirmedDeleteToastText = "Item category deleted successfully",
                    confirmDelete = {
                        val thisItemCategory = ItemCategory(selectedItemCategory)
                        val mutableItemCategories = mutableListOf<ItemCategory>()
                        mutableItemCategories.addAll(itemCategories)
                        val deletedMutableItemCategories = mutableItemCategories.minus(thisItemCategory)
                        val mutableItemCategoriesJson =
                            deletedMutableItemCategories.sortedBy { it.itemCategory }
                                .toItemCategoriesJson()
                        coroutineScope.launch {
                            userPreferences.saveItemCategories(mutableItemCategoriesJson)
                        }
                        Toast.makeText(
                            context,
                            "Item category successfully removed",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    openDeleteDialog = false
                }
            }
        }
    }
}
