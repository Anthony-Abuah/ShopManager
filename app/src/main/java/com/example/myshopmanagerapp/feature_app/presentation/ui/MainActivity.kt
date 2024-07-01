package com.example.myshopmanagerapp.feature_app.presentation.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.myshopmanagerapp.core.Constants.CAMERAX_PERMISSIONS
import com.example.myshopmanagerapp.core.Functions.hasRequiredPermissions
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.start_app.StartAppNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.MyShopManagerAppTheme
import dagger.hilt.android.AndroidEntryPoint

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private fun foregroundPermissionApproved(context: Context): Boolean{
    val writePermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val readPermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context, Manifest.permission.READ_EXTERNAL_STORAGE
    )
    return writePermissionFlag && readPermissionFlag
}

private fun requestForegroundPermissions(context: Context){
    val provideRational = foregroundPermissionApproved(context)
    if (provideRational){
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
    }
    else{
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions(applicationContext)){
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0,
            )
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
                        StartAppNavGraph()
                    }

                }
            }
        }
    }

}

