package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralReportScreenTopBar(
    topBarTitleText: String,
    periodDropDownItems: List<PeriodDropDownItem>,
    onClickItem: (PeriodDropDownItem) -> Unit,
    navigateBack: () -> Unit
) {
    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var isContextMenuVisible by remember {
        mutableStateOf(false)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    TopAppBar (
        modifier = Modifier,
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(5f),
                    contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = topBarTitleText,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .onSizeChanged {
                        itemHeight = with(density) { it.height.toDp() }
                    }
                    .pointerInput(true) {
                        detectTapGestures(
                            onPress = {
                                isContextMenuVisible = true
                                pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            }
                        )
                    },
                    contentAlignment = Alignment.CenterEnd) {
                    Icon(imageVector = Icons.Default.Tune,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    DropdownMenu(modifier = Modifier
                        .padding(end = LocalSpacing.current.small),
                        expanded = isContextMenuVisible,
                        onDismissRequest = { isContextMenuVisible = false },
                        offset = pressOffset
                    ) {
                        periodDropDownItems.forEach{ item->
                            Box(modifier = Modifier
                                .height(LocalSpacing.current.dropDownItem),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = item.titleText,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        onClickItem(item)
                                        isContextMenuVisible = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}

