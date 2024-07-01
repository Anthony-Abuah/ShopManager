package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.SearchPlaceholder
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchBar(
    placeholder: String,
    onSearch: (String) -> Unit,
){
    val coroutineScope = rememberCoroutineScope()
    var value by remember {
        mutableStateOf(emptyString)
    }

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BlueGrey10 else BlueGrey90
    val contentColor = if (isDarkTheme) BlueGrey90 else BlueGrey10

    Row(
        modifier = Modifier
            .background(backgroundColor, MaterialTheme.shapes.extraLarge)
            .fillMaxWidth()
            .height(LocalSpacing.current.topAppBarSize),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(horizontal = LocalSpacing.current.default)
                .size(LocalSpacing.current.semiLarge),
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = emptyString,
            tint = MaterialTheme.colorScheme.primary
        )
        SimpleTextField(
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent)
                .padding(
                    horizontal = LocalSpacing.current.default,
                    vertical = LocalSpacing.current.smallMedium,
                ),
            value = value,
            onValueChange = {
                value = it
                coroutineScope.launch {
                    delay(2000L)
                    onSearch(value)
                }
            },
            placeholder = placeholder,
            readOnly = false,
            textStyle = TextStyle(
                color = contentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            )
        )
        if (value.isNotBlank()) {
            Icon(
                modifier = Modifier
                    .padding(horizontal = LocalSpacing.current.small)
                    .size(LocalSpacing.current.semiLarge)
                    .clickable { value = emptyString },
                painter = painterResource(id = R.drawable.ic_cancel_circle),
                contentDescription = emptyString,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

