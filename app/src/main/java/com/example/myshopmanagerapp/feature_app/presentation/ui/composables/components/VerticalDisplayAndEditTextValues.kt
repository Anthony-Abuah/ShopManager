package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.Functions.textIsInvalid
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.time.LocalDate


@Composable
fun VerticalDisplayAndEditTextValues(
    modifier: Modifier = Modifier,
    innerHorizontalPadding: Dp = 4.dp,
    innerVerticalPadding: Dp = 2.dp,
    leadingIcon: Int? = R.drawable.personnel,
    leadingIconWidth: Dp = 32.dp,
    trailingIcon: Int? = null,
    trailingIconWidth: Dp = 24.dp,
    firstText: String,
    firstTextSize: TextUnit = 16.sp,
    firstTextColor: Color = MaterialTheme.colorScheme.onBackground,
    firstTextFontWeight: FontWeight = FontWeight.SemiBold,
    secondText: String,
    secondTextSize: TextUnit = 14.sp,
    secondTextColor: Color = MaterialTheme.colorScheme.onBackground,
    secondTextFontWeight: FontWeight = FontWeight.Normal,
    shape: Shape = MaterialTheme.shapes.small,
    elevation: Dp = LocalSpacing.current.noElevation,
    backgroundColor: Color = Color.Transparent,
    onBackgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    value: String = emptyString,
    placeholder: String = emptyString,
    readOnly: Boolean = false,
    isError: Boolean = false,
    label: String = emptyString,
    textFieldIcon: Int = R.drawable.ic_person_filled,
    keyboardType: KeyboardType = KeyboardType.Text,
    isAutoCompleteTextField: Boolean = false,
    isDate: Boolean = false,
    addNewItem: ()-> Unit = {},
    listItems: List<String> = emptyList(),
    expandedIcon: Int = R.drawable.ic_person_filled,
    unexpandedIcon: Int = R.drawable.ic_person_outline,
    selectOnlyList: Boolean = false,
    getUpdatedValue: (String) ->Unit = {},
) {
    val context = LocalContext.current
    var expandRow by remember {
        mutableStateOf(false)
    }
    var textValueIsInvalid by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        elevation = elevation,
        backgroundColor = backgroundColor
    ) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = innerHorizontalPadding, vertical = innerVerticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (!readOnly) expandRow = !expandRow },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null){
                    Box(
                        modifier = Modifier
                            .size(leadingIconWidth)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            modifier = Modifier
                                .size(leadingIconWidth)
                                .aspectRatio(1f),
                            painter = painterResource(id = leadingIcon),
                            contentDescription = emptyString
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            text = firstText,
                            fontSize = firstTextSize,
                            fontWeight = firstTextFontWeight,
                            overflow = TextOverflow.Ellipsis,
                            color = firstTextColor
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = secondText,
                            textAlign = TextAlign.Start,
                            fontSize = secondTextSize,
                            fontWeight = secondTextFontWeight,
                            overflow = TextOverflow.Ellipsis,
                            color = secondTextColor
                        )
                    }
                }
                if (trailingIcon != null){
                    Box(
                        modifier = Modifier
                            .size(trailingIconWidth)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(trailingIconWidth)
                                .aspectRatio(1f),
                            painter = painterResource(id = trailingIcon),
                            tint = onBackgroundColor,
                            contentDescription = emptyString
                        )
                    }
                }

            }


            if (isAutoCompleteTextField){
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
                        var updatedValue by remember { mutableStateOf(value) }
                        Box(modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            AutoCompleteWithAddButton(
                                value = value,
                                label = label,
                                listItems = listItems,
                                placeholder = placeholder,
                                readOnly = readOnly,
                                expandedIcon = expandedIcon,
                                unexpandedIcon = unexpandedIcon,
                                onClickAddButton = { addNewItem() },
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
            else if (isDate){
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
                        var updatedDate by remember { mutableStateOf(value) }
                        Box(modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            DatePickerTextField(
                                defaultDate = value.ifBlank { LocalDate.now().toString() },
                                context = context,
                                onValueChange = {
                                    updatedDate = it
                                    getUpdatedValue(it)
                                },
                                label = FormRelatedString.SelectDate
                            )
                        }
                        Box(modifier = Modifier
                            .width(LocalSpacing.current.topBarIcon)
                            .padding(LocalSpacing.current.small)
                            .clickable {
                                getUpdatedValue(updatedDate)
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
            else {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth()
                        .background(backgroundColor),
                    visible = expandRow
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var updatedValue by remember { mutableStateOf(value) }
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicTextFieldWithTrailingIconError(
                                value = updatedValue,
                                onValueChange = {
                                    updatedValue = it
                                    textValueIsInvalid = textIsInvalid(it)
                                },
                                isError = isError,
                                readOnly = selectOnlyList,
                                placeholder = placeholder,
                                label = label,
                                icon = textFieldIcon,
                                keyboardType = keyboardType
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(LocalSpacing.current.topBarIcon)
                                .padding(LocalSpacing.current.small)
                                .clickable {
                                        getUpdatedValue(updatedValue)
                                        expandRow = false
                                },
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = emptyString,
                                tint = onBackgroundColor
                            )
                        }
                    }
                }
            }

        }

    }
}

