package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first


@Composable
fun StartScreen(
    navigateToStartScreen: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(3000)
    )

    LaunchedEffect(Unit){
        val userPreferences = UserPreferences(context)
        val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
        startAnimation = true
        delay(3000)
        navigateToStartScreen(isLoggedIn)
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .width(200.dp),
                contentAlignment = Alignment.Center
            ){
                Image(
                    modifier = Modifier.fillMaxWidth().alpha(alpha = alphaAnim.value),
                    painter = painterResource(id = R.drawable.shop),
                    contentDescription = emptyString
                )
            }
        }
    }
}
