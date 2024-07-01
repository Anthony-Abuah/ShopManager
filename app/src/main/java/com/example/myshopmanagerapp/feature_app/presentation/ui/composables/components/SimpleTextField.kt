package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun SimpleTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (value:String)-> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    readOnly: Boolean,
    textStyle: TextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
) {
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        singleLine = singleLine,
        onValueChange = { onValueChange(it) },
        readOnly = readOnly,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        decorationBox = {innerTextField ->
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(value.isEmpty()){
                    Text(
                        modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = textStyle
                    )
                }
            }
            innerTextField()
        },

    )

}
