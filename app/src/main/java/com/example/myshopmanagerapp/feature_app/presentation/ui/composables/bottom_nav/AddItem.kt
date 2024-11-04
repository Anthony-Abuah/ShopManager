package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun RowScope.AddItem(
    screen: BottomNavScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val contentColor = MaterialTheme.colorScheme.primary

    Box(modifier = Modifier
        .fillMaxHeight()
        .weight(1f)
        .clickable {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        contentAlignment = Alignment.BottomCenter
    ){
        Row(modifier = Modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.semiLarge)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Icon(
                    painter = painterResource(id = if (isSelected) screen.focused_icon else screen.unfocused_icon ) ,
                    contentDescription = "",
                    tint = contentColor,
                    modifier = Modifier.height(if(isSelected) 30.dp else 25.dp)
                )

                Text(text = screen.name,
                    color = contentColor,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Light,
                    fontSize = if(isSelected) 14.sp else 12.sp
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .height(LocalSpacing.current.small)
                )

            }

        }


    }


}
