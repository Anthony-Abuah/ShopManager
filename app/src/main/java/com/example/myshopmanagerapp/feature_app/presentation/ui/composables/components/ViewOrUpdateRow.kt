package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.Zero
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.SelectDate
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.nameIsValid
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ViewOrUpdateTextValueRow(
    viewTitle: String,
    viewValue: String,
    placeholder: String,
    label: String,
    icon: Int,
    getUpdatedValue: (String) -> Unit,
) {
    var expandRow by remember {
        mutableStateOf(false)
    }
    var nameIsInvalid by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Values
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(LocalSpacing.current.viewOrUpdateRow),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = LocalSpacing.current.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = viewTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = LocalSpacing.current.small),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = viewValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(modifier = Modifier
                .width(LocalSpacing.current.topBarIcon)
                .padding(LocalSpacing.current.small)
                .clickable { expandRow = !expandRow },
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = expandRow
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var updatedValue by remember { mutableStateOf(viewValue) }
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextFieldWithTrailingIconError(
                        value = updatedValue,
                        onValueChange = {
                            updatedValue = it
                            nameIsInvalid = nameIsValid(it)
                        },
                        isError = nameIsInvalid,
                        readOnly = false,
                        placeholder = placeholder,
                        label = label,
                        icon = icon,
                        keyboardType = KeyboardType.Text
                    )
                }
                Box(modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .padding(LocalSpacing.current.small)
                    .clickable {
                        getUpdatedValue(updatedValue)
                        expandRow = nameIsInvalid
                    },
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun ViewOrUpdateAutoCompleteValueRow(
    viewTitle: String,
    viewValue: String,
    placeholder: String,
    label: String,
    expandedIcon: Int,
    unexpandedIcon: Int,
    readOnly: Boolean = true,
    listItems: List<String>,
    onClickAddButton: () -> Unit,
    getUpdatedValue: (String) -> Unit,
) {
    var expandRow by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Values
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(LocalSpacing.current.viewOrUpdateRow),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = LocalSpacing.current.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = viewTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = LocalSpacing.current.small),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = viewValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(modifier = Modifier
                .width(LocalSpacing.current.topBarIcon)
                .padding(LocalSpacing.current.small)
                .clickable { expandRow = !expandRow },
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = expandRow
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var updatedValue by remember { mutableStateOf(viewValue) }
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    AutoCompleteWithAddButton(
                        label = label,
                        listItems = listItems,
                        placeholder = placeholder,
                        readOnly = readOnly,
                        expandedIcon = expandedIcon,
                        unexpandedIcon = unexpandedIcon,
                        onClickAddButton = { onClickAddButton() },
                        getSelectedItem = {
                            updatedValue = it
                            getUpdatedValue(it)
                        }
                    )
                }
                Box(modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .padding(LocalSpacing.current.small)
                    .clickable {
                        getUpdatedValue(updatedValue)
                        expandRow = !expandRow
                    },
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun ViewOrUpdateDateValueRow(
    viewTitle: String,
    viewDateString: String,
    getUpdatedDate: (String) -> Unit,
) {
    val context = LocalContext.current
    var expandRow by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Values
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(LocalSpacing.current.viewOrUpdateRow),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = LocalSpacing.current.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = viewTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = LocalSpacing.current.small),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = viewDateString,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(modifier = Modifier
                .width(LocalSpacing.current.topBarIcon)
                .padding(LocalSpacing.current.small)
                .clickable { expandRow = !expandRow },
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = expandRow
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var updatedDate by remember { mutableStateOf(viewDateString) }
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    DatePickerTextField(defaultDate = viewDateString,
                        context = context,
                        onValueChange = {
                            updatedDate = it
                            getUpdatedDate(it)
                        },
                        label = SelectDate
                    )
                }
                Box(modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .padding(LocalSpacing.current.small)
                    .clickable {
                        getUpdatedDate(updatedDate)
                        expandRow = !expandRow
                    },
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun ViewOrUpdateDescriptionValueRow(
    viewTitle: String,
    viewValue: String,
    placeholder: String,
    label: String,
    icon: Int,
    getUpdatedValue: (String) -> Unit,
) {
    var expandRow by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Values
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = LocalSpacing.current.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = viewTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(bottom = LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = viewValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(modifier = Modifier
                .width(LocalSpacing.current.topBarIcon)
                .padding(LocalSpacing.current.small)
                .clickable { expandRow = !expandRow },
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = expandRow
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var updatedValue by remember { mutableStateOf(viewValue) }
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    DescriptionTextFieldWithTrailingIcon(
                        value = updatedValue,
                        onValueChange = {
                            updatedValue = it
                        },
                        placeholder = placeholder,
                        label = label,
                        icon = icon,
                        keyboardType = KeyboardType.Text
                    )
                }
                Box(modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .padding(LocalSpacing.current.small)
                    .clickable {
                        getUpdatedValue(updatedValue)
                        expandRow = !expandRow
                    },
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
fun ViewOrUpdateNumberValueRow(
    viewTitle: String,
    viewValue: String,
    insideValue: String = Zero,
    placeholder: String,
    label: String,
    icon: Int,
    getUpdatedValue: (String) -> Unit,
) {
    var expandRow by remember {
        mutableStateOf(false)
    }
    var numberIsValid by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Values
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(LocalSpacing.current.viewOrUpdateRow),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = LocalSpacing.current.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = viewTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = LocalSpacing.current.small),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = viewValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(modifier = Modifier
                .width(LocalSpacing.current.topBarIcon)
                .padding(LocalSpacing.current.small)
                .clickable { expandRow = !expandRow },
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = expandRow
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var updatedValue by remember { mutableStateOf(insideValue) }
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextFieldWithTrailingIconError(
                        value = updatedValue,
                        onValueChange = {
                            updatedValue = it
                            numberIsValid = amountIsNotValid(it)
                        },
                        isError = numberIsValid,
                        readOnly = false,
                        placeholder = placeholder,
                        label = label,
                        icon = icon,
                        keyboardType = KeyboardType.Number
                    )
                }
                Box(modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .padding(LocalSpacing.current.small)
                    .clickable {
                        getUpdatedValue(updatedValue)
                        expandRow = numberIsValid
                    },
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
fun ViewTextValueRow(
    viewTitle: String,
    viewValue: String,
    icon: ImageVector = Icons.Default.Info,
    showInfo: Boolean = false,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(LocalSpacing.current.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Values
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalSpacing.current.viewOrUpdateRow),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = LocalSpacing.current.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = viewTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = LocalSpacing.current.small),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = viewValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .clickable { onClick() }
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = emptyString,
                    tint = if (showInfo) MaterialTheme.colorScheme.onBackground else Color.Transparent
                )
            }
        }
    }
}

@Composable
fun ViewTextValueWithExtraValueRow(
    viewTitle: String,
    viewValue: String,
    extraValue: String,
    onClick: () -> Unit = {},
) {
    // Values
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(LocalSpacing.current.small),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalSpacing.current.viewOrUpdateRow),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = LocalSpacing.current.small),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = viewTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(LocalSpacing.current.small))

            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = LocalSpacing.current.small),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier.weight(3f),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = viewValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
                Box(
                    modifier = Modifier.weight(2f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        text = extraValue,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }

}

@Composable
fun ViewPhoto(icon: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.default),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(125.dp)
                .padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LocalSpacing.current.small),
                painter = painterResource(id = icon),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = emptyString
            )
        }
    }
}

@Composable
fun ViewInfo(info: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = LocalSpacing.current.default,
                horizontal = LocalSpacing.current.small
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = info,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold
        )
    }
}
