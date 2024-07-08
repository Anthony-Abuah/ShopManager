package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.domain.model.ListNumberDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListTopBar(
    topBarTitleText: String,
    periodDropDownItems: List<PeriodDropDownItem>,
    onClickPeriodItem: (PeriodDropDownItem) -> Unit,
    listDropDownItems: List<ListNumberDropDownItem>,
    onClickListItem: (ListNumberDropDownItem) -> Unit,
    listOfSortItems: List<ListNumberDropDownItem>,
    onSort: (ListNumberDropDownItem) -> Unit,
    print: () -> Unit,
    navigateBack: () -> Unit
) {
    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var expandPeriodDropItems by remember {
        mutableStateOf(false)
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

    TopAppBar (
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = topBarTitleText,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(modifier = Modifier
                        .width(LocalSpacing.current.topBarIcon)
                        .clickable { print() }
                        .padding(horizontal = LocalSpacing.current.default)
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pdf),
                            contentDescription = emptyString,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Box(modifier = Modifier
                        .width(LocalSpacing.current.topBarIcon)
                        .padding(horizontal = LocalSpacing.current.default)
                        .onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
                        .pointerInput(true) {
                            detectTapGestures(
                                onPress = {
                                    expandListNumberDropItems = true
                                    pressOffset = DpOffset(0.toDp(), it.y.toDp())
                                }
                            )
                        },
                        contentAlignment = Alignment.CenterEnd) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_list),
                            contentDescription = emptyString,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        DropdownMenu(modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .width(150.dp),
                            expanded = expandListNumberDropItems,
                            onDismissRequest = { expandListNumberDropItems = false },
                            offset = pressOffset
                        ) {
                            listDropDownItems.forEachIndexed{ index, value->
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
                                if (index == 0 || index == 5){
                                    HorizontalDivider()
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier
                        .width(LocalSpacing.current.topBarIcon)
                        .padding(horizontal = LocalSpacing.current.default)
                        .onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
                        .pointerInput(true) {
                            detectTapGestures(
                                onPress = {
                                    expandSortItems = true
                                    pressOffset = DpOffset(0.toDp(), it.y.toDp())
                                }
                            )
                        },
                        contentAlignment = Alignment.CenterEnd) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort),
                            contentDescription = emptyString,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        DropdownMenu(modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .width(250.dp),
                            expanded = expandSortItems,
                            onDismissRequest = { expandSortItems = false },
                            offset = pressOffset
                        ) {
                            listOfSortItems.forEachIndexed{ index, value->
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                    //.height(LocalSpacing.current.dropDownItem),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                        text = {
                                            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
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
                                                    maxLines = 2,
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
                                if (index == 1 || index == 3 || index == 5){
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier
                        .width(LocalSpacing.current.topBarIcon)
                        .padding(horizontal = LocalSpacing.current.default)
                        .onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
                        .pointerInput(true) {
                            detectTapGestures(
                                onPress = {
                                    expandPeriodDropItems = true
                                    pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                                }
                            )
                        },
                        contentAlignment = Alignment.CenterEnd) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_date),
                            contentDescription = emptyString,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        DropdownMenu(modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .width(150.dp)
                            .padding(end = LocalSpacing.current.small),
                            expanded = expandPeriodDropItems,
                            onDismissRequest = { expandPeriodDropItems = false },
                            offset = pressOffset
                        ) {
                            periodDropDownItems.forEach{ item->
                                Box(modifier = Modifier
                                    .height(LocalSpacing.current.dropDownItem),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                        text = {
                                            Text(
                                                text = item.titleText,
                                                maxLines = 2,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            onClickPeriodItem(item)
                                            expandPeriodDropItems = false
                                        }
                                    )
                                }
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
