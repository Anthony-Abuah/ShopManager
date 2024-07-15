package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun HomeCard(
    title: String,
    description: String,
    imageWidth: Dp = 40.dp,
    icon: Int,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    descriptionColor: Color,
    cardContainerColor: Color,
    cardShadowColor: Color = MaterialTheme.colorScheme.background,
    onOpenCard: () -> Unit
){
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .clickable { onOpenCard() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = cardShadowColor
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.extraSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .background(cardContainerColor, MaterialTheme.shapes.medium),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(LocalSpacing.current.smallMedium)
                    .width(imageWidth)
                    .aspectRatio(1f),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = cardContainerColor
                ),
                elevation = CardDefaults.cardElevation(LocalSpacing.current.extraSmall)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = icon),
                        contentDescription = emptyString,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
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
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = titleColor
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            top = LocalSpacing.current.small,
                            start = LocalSpacing.current.small,
                            bottom = LocalSpacing.current.small,
                            end = LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = descriptionColor,

                    )
                }

            }
        }
    }
}

