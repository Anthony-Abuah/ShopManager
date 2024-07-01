package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.company

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*
import com.example.myshopmanagerapp.R

@Composable
fun CompanyCard(
    companyName: String,
    products: String,
    companyLocation: String,
    onOpenCompany: () -> Unit,
    onDelete: () -> Unit
){
    val contentColor = MaterialTheme.colorScheme.onSecondary
    val cardContainerColor = MaterialTheme.colorScheme.secondary
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .clickable { onOpenCompany() },
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
            // Icon/photo
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
                        painter = painterResource(id = R.drawable.ic_company),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = LocalSpacing.current.borderStroke,
                            horizontal = LocalSpacing.current.small,
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = companyName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        color = contentColor
                    )
                }

                // Location
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = LocalSpacing.current.borderStroke,
                            horizontal = LocalSpacing.current.small,
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Location: $companyLocation",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor,

                    )
                }

                // Product/ Services
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = LocalSpacing.current.borderStroke,
                            horizontal = LocalSpacing.current.small,
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Products/Services: $products",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Box(modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ){
                IconButton(onClick = {
                    onDelete()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
        }
    }
}

