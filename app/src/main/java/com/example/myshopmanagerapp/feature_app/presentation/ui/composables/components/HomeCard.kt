package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun HomeCard(
    title: String,
    semiTitle: String,
    description: String,
    icon: Int,
    contentColor: Color,
    cardContainerColor: Color,
    onOpenCard: () -> Unit
){
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .clickable { onOpenCard() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.noElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(LocalSpacing.current.smallMedium)
                    .weight(1f)
                    .aspectRatio(1f),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = cardContainerColor
                ),
                elevation = CardDefaults.cardElevation(LocalSpacing.current.noElevation)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(LocalSpacing.current.large),
                        painter = painterResource(id = icon),
                        contentDescription = emptyString,
                        tint = contentColor
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = semiTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor,

                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal,
                        color = contentColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

