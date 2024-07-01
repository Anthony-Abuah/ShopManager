package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.ListOfHours
import com.example.myshopmanagerapp.core.Functions.toIntegerHours
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AutoCompleteTextField(
    label: String,
    placeholder: String,
    readOnly: Boolean,
    expandedIcon: Int,
    unexpandedIcon: Int,
    listItems: List<String>,
    getSelectedItem: (listItem: String) -> Unit
) {
    var item by remember {
        mutableStateOf("")
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .requiredHeight(LocalSpacing.current.textFieldHeight)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        expanded = isFocused
                    },
                value = item,
                readOnly = readOnly,
                onValueChange = { _item ->
                    item = _item
                    getSelectedItem(item)
                    expanded = true
                },
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorIndicatorColor = MaterialTheme.colorScheme.onErrorContainer,
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                placeholder = {
                    Text(
                        text = placeholder,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                label = {
                    Text(
                        text = label,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { expanded = !expanded },
                        painter = painterResource(id = if (expanded) expandedIcon else unexpandedIcon),
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expanded
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        if (item.isNotEmpty()) {
                            items(
                                if (readOnly) listItems else listItems
                                    .filter {
                                    it.lowercase()
                                        .contains(item.lowercase()) || it.lowercase()
                                        .contains("others")
                                }.sorted()
                            ) {
                                DropDownItems(title = it) { title ->
                                    item = title
                                    getSelectedItem(title)
                                    expanded = false
                                }
                            }
                        } else {
                            items(
                                if (readOnly) listItems else
                                listItems.sorted()
                            ) {
                                DropDownItems(title = it) { title ->
                                    item = title
                                    getSelectedItem(title)
                                    expanded = false
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun AutoCompleteTextFieldForHours(
    label: String,
    getSelectedHourValue: (hour: Int) -> Unit
) {
    var hourValue by remember {
        mutableStateOf("")
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .requiredHeight(LocalSpacing.current.textFieldHeight)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        expanded = isFocused
                    },
                value = hourValue,
                onValueChange = { _item ->
                    hourValue = _item
                    getSelectedHourValue(toIntegerHours(hourValue))
                    expanded = true
                },
                readOnly = true,
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorIndicatorColor = MaterialTheme.colorScheme.onErrorContainer,
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                label = {
                    Text(
                        text = label,
                        textAlign = TextAlign.Start,
                        color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { expanded = !expanded },
                        painter = painterResource(id = if (expanded) R.drawable.ic_time_filled else R.drawable.ic_time_outline),
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                visible = expanded
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 175.dp),
                    ) {
                        if (hourValue.isNotEmpty()) {
                            items(
                                ListOfHours
                            ) {
                                DropDownItems(it) { _hour ->
                                    hourValue = _hour
                                    getSelectedHourValue(toIntegerHours(hourValue))
                                    expanded = false
                                }
                            }
                        } else {
                            items(
                                ListOfHours
                            ) {
                                DropDownItems(it) { _hour ->
                                    hourValue = _hour
                                    getSelectedHourValue(toIntegerHours(hourValue))
                                    expanded = false
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}




