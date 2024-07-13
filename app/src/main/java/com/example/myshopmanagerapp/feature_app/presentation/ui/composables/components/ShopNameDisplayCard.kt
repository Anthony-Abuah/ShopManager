package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ShopNameDisplayCard(
    icon: Int,
    title: String,
    info: String
) {
    Row(modifier = Modifier.fillMaxWidth()
        .height(80.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .aspectRatio(1f)
            .padding(end = LocalSpacing.current.default),
            contentAlignment = Alignment.Center
        ){
            Image(
                modifier = Modifier
                    .padding(LocalSpacing.current.small)
                    .fillMaxSize(),
                painter = painterResource(id = icon),
                contentDescription = emptyString,
            )
        }
        Column(modifier = Modifier.weight(1f)
            .fillMaxHeight()
            .padding(start = LocalSpacing.current.default),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = info,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    }

}
