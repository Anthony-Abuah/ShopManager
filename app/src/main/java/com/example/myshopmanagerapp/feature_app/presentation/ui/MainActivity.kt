package com.example.myshopmanagerapp.feature_app.presentation.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.myshopmanagerapp.core.Constants.CAMERAX_PERMISSIONS
import com.example.myshopmanagerapp.core.Functions.hasRequiredPermissions
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.BottomNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.MyShopManagerAppTheme
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import dagger.hilt.android.AndroidEntryPoint

//private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

/*
private fun foregroundPermissionApproved(context: Context): Boolean{
    val writePermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val readPermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context, Manifest.permission.READ_EXTERNAL_STORAGE
    )
    return writePermissionFlag && readPermissionFlag
}
*/

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val companyViewModel by viewModels<CompanyViewModel> ()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions(applicationContext)){
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0,
            )
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition{
                companyViewModel.isLoggedIn == null
            }
            /*
            setOnExitAnimationListener { screen ->
                try {
                    val zoomX = ObjectAnimator.ofFloat(
                        screen.iconView,
                        View.SCALE_X,
                        0.4f,
                        0.0f
                    )
                    zoomX.interpolator = OvershootInterpolator()
                    zoomX.duration = 500L
                    zoomX.doOnEnd { screen.remove() }

                    val zoomY = ObjectAnimator.ofFloat(
                        screen.iconView,
                        View.SCALE_Y,
                        0.4f,
                        0.0f
                    )
                    zoomY.interpolator = OvershootInterpolator()
                    zoomY.duration = 500L
                    zoomY.doOnEnd { screen.remove() }

                    zoomX.start()
                    zoomY.start()
                }catch (_: Exception){
                    val intent = Intent(MyShopManagerApp.applicationContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            }
            */
        }
        setContent {
            MyShopManagerAppTheme {
                //requestForegroundPermissions(applicationContext)
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        BottomNavGraph()
                    }

                }
            }
        }
    }
}

