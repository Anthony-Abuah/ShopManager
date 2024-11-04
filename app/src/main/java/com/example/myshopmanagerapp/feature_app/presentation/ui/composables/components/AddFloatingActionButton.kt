package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun AddFloatingActionButton(
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onAdd: () -> Unit
){
    Card(modifier = Modifier
        .clickable { onAdd() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.noElevation)
    ) {

        Row(modifier = Modifier.padding(
            vertical = LocalSpacing.current.smallMedium,
            horizontal = LocalSpacing.current.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.AddCircle,
                contentDescription = emptyString,
                tint = contentColor
            )

            Spacer(modifier = Modifier.width(LocalSpacing.current.small))

            Text(
                text = "ADD NEW",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = contentColor
            )

        }
    }
}

