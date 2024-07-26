package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ToggleSwitchCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun PersonnelCard(
    personnel: PersonnelEntity,
    number: String,
    cardHeight: Dp = 120.dp,
    cardShadowColor: Color = MaterialTheme.colorScheme.surface,
    cardContainerColor: Color = MaterialTheme.colorScheme.background,
    mainContentColor: Color = MaterialTheme.colorScheme.onSurface,
    secondaryContentColor: Color = MaterialTheme.colorScheme.onSurface,
    image: Int = R.drawable.personnel,
    imageWidth: Dp = 64.dp,
    imagePadding: Dp = LocalSpacing.current.small,
    onDelete: () -> Unit,
    onOpenPersonnel: () -> Unit
){
    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    var expandOptionsDropDown by remember {
        mutableStateOf(false)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    val dropDownOptions = listOf("Edit", "Delete")

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(cardHeight)
        .clickable { onOpenPersonnel() },
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
                    .padding(imagePadding)
                    .width(imageWidth)
                    .fillMaxHeight(0.9f),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = cardContainerColor
                ),
                elevation = CardDefaults.cardElevation(LocalSpacing.current.small)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = image),
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
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "${personnel.firstName} ${personnel.lastName} ${personnel.otherNames}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = mainContentColor
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Contact: ${personnel.contact}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = secondaryContentColor
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Role: ${personnel.role}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = secondaryContentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.extraSmall),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(LocalSpacing.current.noPadding),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        val adminRights =  if (personnel.hasAdminRights ==true) "YES" else "NO"
                        Text(
                            text = "Has admin rights: $adminRights",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = secondaryContentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Column(modifier = Modifier
                .width(LocalSpacing.current.medium)
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Column(modifier = Modifier
                    .weight(1f)
                    .onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
                    .pointerInput(true) {
                        detectTapGestures(
                            onPress = {
                                expandOptionsDropDown = true
                                pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            }
                        )
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_options),
                        contentDescription = emptyString,
                        tint = mainContentColor
                    )
                    DropdownMenu(modifier = Modifier
                        .width(150.dp),
                        expanded = expandOptionsDropDown,
                        onDismissRequest = { expandOptionsDropDown = false },
                        offset = pressOffset
                    ) {
                        dropDownOptions.forEachIndexed{ index, value->
                            Box(modifier = Modifier
                                .height(LocalSpacing.current.dropDownItem),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                DropdownMenuItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = {
                                        Row(modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = value,
                                                color = secondaryContentColor,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                    },
                                    onClick = {
                                        if (index == 0){ onOpenPersonnel() }else{ onDelete() }
                                        expandOptionsDropDown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(text = number,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        color = secondaryContentColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }


}

