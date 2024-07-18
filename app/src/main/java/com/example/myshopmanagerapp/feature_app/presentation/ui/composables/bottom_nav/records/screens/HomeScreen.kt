package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UIEvent
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.BottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.HomeContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens.HomeNavDrawerItems.homeNavDrawerItems
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.HomeScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.WindowInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.rememberWindowInfo
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import com.example.myshopmanagerapp.feature_app.presentation.view_models.BackupViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun HomeScreen(
    backupViewModel: BackupViewModel = hiltViewModel(),
    navController: NavHostController,
    navHostController: NavHostController,
    navigateToPersonnelNavGraph: (Boolean) -> Unit,
    navigateToRevenueListScreen: () -> Unit,
    navigateToExpenseListScreen: () -> Unit,
    navigateToInventoryListScreen: () -> Unit,
    navigateToStockListScreen: () -> Unit,
    navigateToDebtListScreen: () -> Unit,
    navigateToDebtRepaymentListScreen: () -> Unit,
    navigateToWithdrawalListScreen: () -> Unit,
    navigateToSavingsListScreen: () -> Unit
) {

    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.collectAsState(initial = false).value ?: false
    val personnelJson = userPreferences.getPersonnelInfo.collectAsState(initial = emptyString).value ?: emptyString
    val personnel = personnelJson.toPersonnelEntity()
    val shopInfoJson = userPreferences.getShopInfo.collectAsState(initial = emptyString).value
    val shopInfo = shopInfoJson.toCompanyEntity()

    val windowType = rememberWindowInfo().screenWidthInfo


    Surface(modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val coroutineScope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var selectedItemIndex by remember {
            mutableStateOf(0)
        }

        ModalNavigationDrawer(
            modifier = Modifier.fillMaxWidth(),
            drawerContent = {
                when(windowType){
                    WindowInfo.WindowType.Compact->{
                        ModalDrawerSheet(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            windowInsets = WindowInsets.safeDrawing
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(LocalSpacing.current.default)
                                        .requiredHeight(120.dp),
                                    painter = painterResource(id = R.drawable.shop),
                                    contentDescription = emptyString
                                )
                                Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val companyName = shopInfo?.companyName?.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                        else it.toString()
                                    }
                                    Text(
                                        modifier = Modifier.padding(LocalSpacing.current.medium),
                                        text = companyName ?: "Shop Manager",
                                        style = MaterialTheme.typography.titleLarge,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }
                            HorizontalDivider()

                            Column(
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .fillMaxWidth()
                                    .fillMaxHeight(1f)
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

                                homeNavDrawerItems.forEachIndexed { index, navDrawerItem ->
                                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                                    NavigationDrawerItem(
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                        label = { Text(text = navDrawerItem.title) },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            selectedItemIndex = index
                                            coroutineScope.launch {
                                                drawerState.close()
                                                if (index < 3) {
                                                    navDrawerItem.route?.let {
                                                        navHostController.navigate(
                                                            it
                                                        )
                                                    }
                                                } else {
                                                    navDrawerItem.route?.let {
                                                        navController.navigate(
                                                            it
                                                        )
                                                    }
                                                }

                                                if (navDrawerItem.title == "Back up") {
                                                    backupViewModel.absoluteRemoteBackup()
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(
                                                    id = if (index == selectedItemIndex) navDrawerItem.selectedIcon
                                                    else navDrawerItem.unselectedIcon
                                                ),
                                                contentDescription = navDrawerItem.title
                                            )
                                        },
                                        badge = {
                                            navDrawerItem.badgeCount?.let { Text(text = it) }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                                    if (index == 2 || index == 9) {
                                        HorizontalDivider()
                                        Spacer(modifier = Modifier.height(LocalSpacing.current.small))
                                    }
                                }
                            }
                        }
                    }
                    else->{
                        ModalDrawerSheet(
                            modifier = Modifier,
                            windowInsets = WindowInsets.safeDrawing
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(LocalSpacing.current.default)
                                        .requiredHeight(120.dp),
                                    painter = painterResource(id = R.drawable.shop),
                                    contentDescription = emptyString
                                )
                                Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val companyName = shopInfo?.companyName?.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                        else it.toString()
                                    }
                                    Text(
                                        modifier = Modifier.padding(LocalSpacing.current.medium),
                                        text = companyName ?: "Shop Manager",
                                        style = MaterialTheme.typography.titleLarge,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }
                            HorizontalDivider()

                            Column(
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .fillMaxWidth()
                                    .fillMaxHeight(1f)
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

                                homeNavDrawerItems.forEachIndexed { index, navDrawerItem ->
                                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                                    NavigationDrawerItem(
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                        label = { Text(text = navDrawerItem.title) },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            selectedItemIndex = index
                                            coroutineScope.launch {
                                                drawerState.close()
                                                if (index < 3) {
                                                    navDrawerItem.route?.let {
                                                        navHostController.navigate(
                                                            it
                                                        )
                                                    }
                                                } else {
                                                    navDrawerItem.route?.let {
                                                        navController.navigate(
                                                            it
                                                        )
                                                    }
                                                }

                                                if (navDrawerItem.title == "Back up") {
                                                    backupViewModel.absoluteRemoteBackup()
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(
                                                    id = if (index == selectedItemIndex) navDrawerItem.selectedIcon
                                                    else navDrawerItem.unselectedIcon
                                                ),
                                                contentDescription = navDrawerItem.title
                                            )
                                        },
                                        badge = {
                                            navDrawerItem.badgeCount?.let { Text(text = it) }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

                                    if (index == 2 || index == 9) {
                                        HorizontalDivider()
                                        Spacer(modifier = Modifier.height(LocalSpacing.current.small))
                                    }
                                }
                            }
                        }
                    }
                }
            },
            drawerState = drawerState
        ) {
            val scaffoldState = rememberScaffoldState()

            LaunchedEffect(key1 = true) {
                backupViewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is UIEvent.ShowSnackBar -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = event.message
                            )
                        }
                    }
                }
            }

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    HomeScreenTopBar(
                        topBarTitleText = "Records",
                        personnelUserName = personnel?.userName.toNotNull().ifBlank { "Tap to login" },
                        personnelIcon = if (personnelIsLoggedIn) R.drawable.ic_person_filled else R.drawable.ic_logged_out_personnel,
                        navigateToPersonnelNavGraph = {
                            navigateToPersonnelNavGraph(
                                personnelIsLoggedIn
                            )
                        },
                    ) {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                },
                bottomBar = { BottomBar(navHostController) }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HomeContent(
                        navigateToRevenueListScreen = navigateToRevenueListScreen,
                        navigateToExpenseListScreen = navigateToExpenseListScreen,
                        navigateToInventoryListScreen = navigateToInventoryListScreen,
                        navigateToDebtListScreen = navigateToDebtListScreen,
                        navigateToDebtRepaymentListScreen = navigateToDebtRepaymentListScreen,
                        navigateToSavingsListScreen = navigateToSavingsListScreen,
                        navigateToWithdrawalListScreen = navigateToWithdrawalListScreen,
                        navigateToStockListScreen = navigateToStockListScreen,
                    )
                }
            }


        }
    }
}
