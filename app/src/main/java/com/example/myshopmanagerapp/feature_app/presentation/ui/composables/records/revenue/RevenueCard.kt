package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.revenue

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.Personnel
import com.example.myshopmanagerapp.core.Constants.Sales
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*

@Composable
fun RevenueCard(
    revenue: RevenueEntity,
    personnel: String,
    number: String,
    currency: String,
    onDelete: () -> Unit,
    onOpenCard: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .border(
            width = LocalSpacing.current.divider,
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.onBackground
        )
        .padding(LocalSpacing.current.medium)
        .clickable { onOpenCard() }
    ) {
        val date = revenue.date.toDateString()
        val dayOfWeek = revenue.dayOfWeek?.lowercase(Locale.ROOT)
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } ?: NotAvailable
        val revenueType = revenue.revenueType  ?: Sales
        val isSales = revenueType.lowercase().contains(Sales.lowercase().take(4))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.shapes.large
                )
                .padding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = LocalSpacing.current.default,
                    vertical = LocalSpacing.current.extraSmall
                ),
                text = date,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        Row(modifier = Modifier) {
            Card (
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .size(40.dp)
                    .clip(CircleShape)
                    .padding(LocalSpacing.current.extraSmall),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(LocalSpacing.current.medium)
            ){
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.revenue),
                    contentDescription = emptyString
                )
            }

            Spacer(modifier = Modifier.width(LocalSpacing.current.small))

            Column(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    val hours = if(revenue.numberOfHours == null || revenue.numberOfHours == 1) Constants.Hour else Constants.Hours
                    val numberOfHoursOpened = if(revenue.numberOfHours == null || revenue.numberOfHours < 1) "Hours opened : $NotAvailable"
                    else "Opened for ${revenue.numberOfHours.toString().plus(" $hours")}"

                    Box(modifier = Modifier.weight(1f)) {
                        Text(
                            text = revenueType.plus("\n$numberOfHoursOpened"),
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

                    Box(modifier = Modifier
                        .wrapContentSize()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.shapes.large
                        )
                        .padding(),
                        contentAlignment = Alignment.Center){
                        Text(
                            modifier = Modifier.padding(
                                horizontal = LocalSpacing.current.default,
                                vertical = LocalSpacing.current.extraSmall
                            ),
                            text = dayOfWeek,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(LocalSpacing.current.default))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Box(modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "$currency ${revenue.revenueAmount}",
                                lineHeight = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        HorizontalDivider(thickness = LocalSpacing.current.divider, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = Personnel,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    modifier = Modifier
                        .padding(LocalSpacing.current.default)
                        .size(LocalSpacing.current.small),
                    imageVector = Icons.Default.Circle,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = personnel,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(modifier = Modifier.width(LocalSpacing.current.small))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.large
                    )
                    .clip(CircleShape)
                    .padding(LocalSpacing.current.extraSmall)
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = emptyString,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        /*
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = Personnel,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(modifier = Modifier
                .padding(LocalSpacing.current.default)
                .size(LocalSpacing.current.small),
                imageVector = Icons.Default.Circle,
                contentDescription = emptyString,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(text = personnel,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        */
    }

}

