package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.domain.model.ListNumberDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.ProfileDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.scriptSemiBold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreenTopBar(
    topBarTitleText: String,
) {
    Card(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .height(LocalSpacing.current.bottomNavBarSize),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.default)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = topBarTitleText,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {}
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScreenTopBar(
    topBarTitleText: String,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    navigateBack: () -> Unit
) {

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = topBarTitleText,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        navigationIcon = {
            Box(modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
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
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewDebtScreenTopBar(
    topBarTitleText: String,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    deleteDebt: () -> Unit,
    navigateBack: () -> Unit,
) {

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = topBarTitleText,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier.clickable { deleteDebt() },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.error
                    )
                }

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        navigationIcon = {
            Box(modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
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
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerScreenTopBar(
    topBarTitleText: String,
    listDropDownItems: List<ListNumberDropDownItem>,
    onClickPDF: () -> Unit = {},
    onClickListItem: (ListNumberDropDownItem) -> Unit,
    listOfSortItems: List<ListNumberDropDownItem>,
    onSort: (ListNumberDropDownItem) -> Unit,
    navigateBack: () -> Unit
) {
    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var expandListNumberDropItems by remember {
        mutableStateOf(false)
    }
    var expandSortItems by remember {
        mutableStateOf(false)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    Card(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .height(LocalSpacing.current.bottomNavBarSize),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.default)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = topBarTitleText,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(modifier = Modifier
                                .width(LocalSpacing.current.topBarIcon)
                                .padding(horizontal = LocalSpacing.current.default)
                                .onSizeChanged {
                                    itemHeight = with(density) { it.height.toDp() }
                                }
                                .pointerInput(true) {
                                    detectTapGestures(
                                        onPress = {
                                            expandSortItems = true
                                            pressOffset = DpOffset(0.toDp(), it.y.toDp())
                                        }
                                    )
                                },
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sort),
                                    contentDescription = emptyString,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                DropdownMenu(
                                    modifier = Modifier
                                        .width(300.dp),
                                    expanded = expandSortItems,
                                    onDismissRequest = { expandSortItems = false },
                                    offset = pressOffset
                                ) {
                                    listOfSortItems.forEachIndexed { index, value ->
                                        Box(
                                            modifier = Modifier
                                                .height(LocalSpacing.current.dropDownItem),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            DropdownMenuItem(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        value.icon?.let {
                                                            Icon(
                                                                modifier = Modifier.padding(end = LocalSpacing.current.default),
                                                                painter = painterResource(id = it),
                                                                contentDescription = emptyString,
                                                                tint = MaterialTheme.colorScheme.onSurface
                                                            )
                                                        }

                                                        Text(
                                                            text = value.titleText,
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            fontWeight = FontWeight.Normal
                                                        )
                                                    }
                                                },
                                                onClick = {
                                                    onSort(value)
                                                    expandSortItems = false
                                                }
                                            )
                                        }
                                        if (index == 1 || index == 3) {
                                            HorizontalDivider()
                                        }
                                    }
                                }
                            }

                            Box(modifier = Modifier
                                .width(LocalSpacing.current.topBarIcon)
                                .padding(horizontal = LocalSpacing.current.default)
                                .onSizeChanged {
                                    itemHeight = with(density) { it.height.toDp() }
                                }
                                .pointerInput(true) {
                                    detectTapGestures(
                                        onPress = {
                                            expandListNumberDropItems = true
                                            pressOffset = DpOffset(0.toDp(), it.y.toDp())
                                        }
                                    )
                                },
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_list),
                                    contentDescription = emptyString,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                DropdownMenu(
                                    modifier = Modifier
                                        .width(150.dp),
                                    expanded = expandListNumberDropItems,
                                    onDismissRequest = { expandListNumberDropItems = false },
                                    offset = pressOffset
                                ) {
                                    listDropDownItems.forEachIndexed { index, value ->
                                        Box(
                                            modifier = Modifier
                                                .height(LocalSpacing.current.dropDownItem),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            DropdownMenuItem(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        value.icon?.let {
                                                            Icon(
                                                                modifier = Modifier.padding(end = LocalSpacing.current.default),
                                                                painter = painterResource(id = it),
                                                                contentDescription = emptyString,
                                                                tint = MaterialTheme.colorScheme.onSurface
                                                            )
                                                        }

                                                        Text(
                                                            text = value.titleText,
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            fontWeight = FontWeight.Normal
                                                        )
                                                    }
                                                },
                                                onClick = {
                                                    onClickListItem(value)
                                                    expandListNumberDropItems = false
                                                }
                                            )
                                        }
                                        if (index == 0 || index == 5) {
                                            HorizontalDivider()
                                        }
                                    }
                                }
                            }

                            Box(modifier = Modifier
                                .width(LocalSpacing.current.topBarIcon)
                                .clickable { onClickPDF() }
                                .padding(horizontal = LocalSpacing.current.default),
                                contentAlignment = Alignment.CenterEnd) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_pdf),
                                    contentDescription = emptyString,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
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
                    Box(modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
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
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreenTopBar(
    photoIsTaken: Boolean,
    setPhotoIsTakenToFalse: () -> Unit,
    topBarTitleText: String,
    navigateBack: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onBackground
    TopAppBar (
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .clickable {
                        if (photoIsTaken) setPhotoIsTakenToFalse() else navigateBack()
                    },
                contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = topBarTitleText,
                        color = contentColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor
        ),
        navigationIcon = {
            Box(modifier = Modifier) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = {
                        if (photoIsTaken) setPhotoIsTakenToFalse() else navigateBack()
                    }
                ) {
                    Icon(
                        imageVector = if (photoIsTaken) Icons.Default.Cancel else Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = contentColor
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenTopBar(
    topBarTitleText: String,
    navigateBack: () -> Unit
) {
    TopAppBar (
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = topBarTitleText,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    topBarTitleText: String,
    personnelUserName: String = emptyString,
    personnelIcon: Int = R.drawable.ic_logged_out_personnel,
    navigateToPersonnelNavGraph: () -> Unit,
    navigateBack: () -> Unit
) {
    Card(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .height(LocalSpacing.current.bottomNavBarSize),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.default)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TopAppBar(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = topBarTitleText,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(LocalSpacing.current.extraSmall)
                                .wrapContentWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(LocalSpacing.current.large)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(LocalSpacing.current.extraSmall)
                                        .fillMaxSize()
                                        .clickable { navigateToPersonnelNavGraph() },
                                    painter = painterResource(id = personnelIcon),
                                    contentDescription = emptyString,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            Text(
                                text = personnelUserName,
                                fontSize = 10.sp,
                                fontFamily = scriptSemiBold,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    Box(modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = navigateBack
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_nav_drawer),
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    placeholder: String,
    navigationIconColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onBackgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    goBack: () -> Unit = {},
    getSearchValue: (String) -> Unit
) {
    TopAppBar (
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SearchBar(
                    placeholder = placeholder,
                    onSearch = { getSearchValue(it) }
                )
            }
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .clickable { goBack() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = emptyString,
                tint = navigationIconColor
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = onBackgroundColor,
            navigationIconContentColor = onBackgroundColor,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockReportScreenTopBar(
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
    Card(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .height(LocalSpacing.current.bottomNavBarSize),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.default)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TopAppBar(
                modifier = Modifier,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(5f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = topBarTitleText,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
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
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            DropdownMenu(
                                modifier = Modifier
                                    .padding(end = LocalSpacing.current.small),
                                expanded = isContextMenuVisible,
                                onDismissRequest = { isContextMenuVisible = false },
                                offset = pressOffset
                            ) {
                                periodDropDownItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .height(LocalSpacing.current.dropDownItem),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = item.titleText,
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
                    Box(modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center) {
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
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTopBar(
    topBarTitleText: String,
    profileDropDownItems: List<ProfileDropDownItem>,
    onClickItem: (ProfileDropDownItem) -> Unit,
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
    Card(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .height(LocalSpacing.current.bottomNavBarSize),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.default)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TopAppBar(
                modifier = Modifier,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(5f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = topBarTitleText,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
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
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            DropdownMenu(
                                modifier = Modifier
                                    .padding(end = LocalSpacing.current.small),
                                expanded = isContextMenuVisible,
                                onDismissRequest = { isContextMenuVisible = false },
                                offset = pressOffset
                            ) {
                                profileDropDownItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .height(LocalSpacing.current.dropDownItem),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = item.titleText,
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
                    Box(modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center) {
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
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryReportScreenTopBar(
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
    val containerColor = MaterialTheme.colorScheme.background
    val contentColor = MaterialTheme.colorScheme.onBackground
    TopAppBar (
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor),
        title = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(containerColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(5f),
                    contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = topBarTitleText,
                        color = contentColor,
                        style = MaterialTheme.typography.titleLarge,
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
                        tint = contentColor
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
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor
        ),
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreenTopBar(
    topBarTitleText: String,
) {
    val containerColor = MaterialTheme.colorScheme.background
    val contentColor = MaterialTheme.colorScheme.onBackground
    TopAppBar (
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor),
        title = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(containerColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(5f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = topBarTitleText,
                        color = contentColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor
        ),
    )
}

