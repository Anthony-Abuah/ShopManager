package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AutoCompleteWithAddButton(
    value: String = emptyString,
    label: String,
    placeholder: String,
    listItems: List<String>,
    readOnly: Boolean,
    expandedIcon: Int,
    unexpandedIcon: Int,
    onClickAddButton: () -> Unit,
    getSelectedItem: (listItem: String) -> Unit
) {
    var item by remember {
        mutableStateOf(value)
    }
    var isExpanded by remember {
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
        val windowInfo = rememberWindowInfo()
        when(windowInfo.screenWidthInfo){
            WindowInfo.WindowType.Compact -> {
                Column(
                    modifier = Modifier.weight(5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .requiredHeight(LocalSpacing.current.textFieldHeight)
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                isExpanded = isFocused
                            },
                        readOnly = readOnly,
                        value = item,
                        onValueChange = { _item ->
                            item = _item
                            getSelectedItem(item)
                            isExpanded = true
                        },
                        placeholder = {
                            Text(
                                text = placeholder,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
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
                        label = {
                            Text(
                                text = label,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable { isExpanded = !isExpanded },
                                painter = painterResource(id = if (isExpanded) expandedIcon else unexpandedIcon),
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = LocalSpacing.current.extraSmall),
                        visible = isExpanded
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 150.dp),
                            ) {
                                if (item.isNotEmpty()) {
                                    items(
                                        if (readOnly) listItems else listItems.filter {
                                            it.lowercase()
                                                .contains(item.lowercase()) || it.lowercase()
                                                .contains("others")
                                        }.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                } else {
                                    items(
                                        if (readOnly) listItems else listItems.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(LocalSpacing.current.textFieldHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                    Box(
                        modifier = Modifier
                            .padding(LocalSpacing.current.small)
                            .requiredSize(LocalSpacing.current.large)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.medium
                            )
                            .clickable { onClickAddButton() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            WindowInfo.WindowType.Medium -> {
                Column(
                    modifier = Modifier.weight(11f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .requiredHeight(LocalSpacing.current.textFieldHeight)
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                isExpanded = isFocused
                            },
                        value = item,
                        onValueChange = { _item ->
                            item = _item
                            getSelectedItem(item)
                            isExpanded = true
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
                                modifier = Modifier.clickable { isExpanded = !isExpanded },
                                imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = LocalSpacing.current.extraSmall),
                        visible = isExpanded
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 150.dp),
                            ) {
                                if (item.isNotEmpty()) {
                                    items(
                                        if (readOnly) listItems else listItems.filter {
                                            it.lowercase()
                                                .contains(item.lowercase()) || it.lowercase()
                                                .contains("others")
                                        }.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                } else {
                                    items(
                                        if (readOnly) listItems else listItems.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(LocalSpacing.current.textFieldHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                    Box(
                        modifier = Modifier
                            .padding(LocalSpacing.current.small)
                            .requiredSize(LocalSpacing.current.large)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.medium
                            )
                            .clickable { onClickAddButton() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier.weight(14f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .requiredHeight(LocalSpacing.current.textFieldHeight)
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                isExpanded = isFocused
                            },
                        value = item,
                        onValueChange = { _item ->
                            item = _item
                            getSelectedItem(item)
                            isExpanded = true
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
                                modifier = Modifier.clickable { isExpanded = !isExpanded },
                                imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = LocalSpacing.current.extraSmall),
                        visible = isExpanded
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 150.dp),
                            ) {
                                if (item.isNotEmpty()) {
                                    items(
                                        if (readOnly) listItems else listItems.filter {
                                            it.lowercase()
                                                .contains(item.lowercase()) || it.lowercase()
                                                .contains("others")
                                        }.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                } else {
                                    items(
                                        if (readOnly) listItems else listItems.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(LocalSpacing.current.textFieldHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))
                    Box(
                        modifier = Modifier
                            .padding(LocalSpacing.current.small)
                            .requiredSize(LocalSpacing.current.large)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.medium
                            )
                            .clickable { onClickAddButton() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AutoCompleteWithAddButton1(
    value: String,
    label: String,
    placeholder: String,
    listItems: List<String>,
    readOnly: Boolean,
    expandedIcon: Int,
    unexpandedIcon: Int,
    onClickAddButton: () -> Unit,
    getSelectedItem: (listItem: String) -> Unit
) {
    var item by remember {
        mutableStateOf(value)
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        val windowInfo = rememberWindowInfo()
        when(windowInfo.screenWidthInfo){
            WindowInfo.WindowType.Compact -> {
                Column(
                    modifier = Modifier.weight(5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .requiredHeight(LocalSpacing.current.textFieldHeight)
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                isExpanded = isFocused
                            },
                        readOnly = readOnly,
                        value = item,
                        onValueChange = { _item ->
                            item = _item
                            getSelectedItem(item)
                            isExpanded = true
                        },
                        placeholder = {
                            Text(
                                text = placeholder,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
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
                        label = {
                            Text(
                                text = label,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable { isExpanded = !isExpanded },
                                painter = painterResource(id = if (isExpanded) expandedIcon else unexpandedIcon),
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = LocalSpacing.current.extraSmall),
                        visible = isExpanded
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 150.dp),
                            ) {
                                if (item.isNotEmpty()) {
                                    items(
                                        if (readOnly) listItems else listItems.filter {
                                            it.lowercase()
                                                .contains(item.lowercase()) || it.lowercase()
                                                .contains("others")
                                        }.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                } else {
                                    items(
                                        if (readOnly) listItems else listItems.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(LocalSpacing.current.textFieldHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                    Box(
                        modifier = Modifier
                            .padding(LocalSpacing.current.small)
                            .requiredSize(LocalSpacing.current.large)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.medium
                            )
                            .clickable { onClickAddButton() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            WindowInfo.WindowType.Medium -> {
                Column(
                    modifier = Modifier.weight(11f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .requiredHeight(LocalSpacing.current.textFieldHeight)
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                isExpanded = isFocused
                            },
                        value = item,
                        onValueChange = { _item ->
                            item = _item
                            getSelectedItem(item)
                            isExpanded = true
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
                                modifier = Modifier.clickable { isExpanded = !isExpanded },
                                imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = LocalSpacing.current.extraSmall),
                        visible = isExpanded
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 150.dp),
                            ) {
                                if (item.isNotEmpty()) {
                                    items(
                                        if (readOnly) listItems else listItems.filter {
                                            it.lowercase()
                                                .contains(item.lowercase()) || it.lowercase()
                                                .contains("others")
                                        }.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                } else {
                                    items(
                                        if (readOnly) listItems else listItems.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(LocalSpacing.current.textFieldHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                    Box(
                        modifier = Modifier
                            .padding(LocalSpacing.current.small)
                            .requiredSize(LocalSpacing.current.large)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.medium
                            )
                            .clickable { onClickAddButton() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier.weight(14f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .requiredHeight(LocalSpacing.current.textFieldHeight)
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                isExpanded = isFocused
                            },
                        value = item,
                        onValueChange = { _item ->
                            item = _item
                            getSelectedItem(item)
                            isExpanded = true
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
                                modifier = Modifier.clickable { isExpanded = !isExpanded },
                                imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = LocalSpacing.current.extraSmall),
                        visible = isExpanded
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = LocalSpacing.current.smallMedium),
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 150.dp),
                            ) {
                                if (item.isNotEmpty()) {
                                    items(
                                        if (readOnly) listItems else listItems.filter {
                                            it.lowercase()
                                                .contains(item.lowercase()) || it.lowercase()
                                                .contains("others")
                                        }.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                } else {
                                    items(
                                        if (readOnly) listItems else listItems.sorted()
                                    ) {
                                        DropDownItems(title = it) { title ->
                                            item = title
                                            getSelectedItem(title)
                                            isExpanded = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(LocalSpacing.current.textFieldHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))
                    Box(
                        modifier = Modifier
                            .padding(LocalSpacing.current.small)
                            .requiredSize(LocalSpacing.current.large)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.medium
                            )
                            .clickable { onClickAddButton() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}




