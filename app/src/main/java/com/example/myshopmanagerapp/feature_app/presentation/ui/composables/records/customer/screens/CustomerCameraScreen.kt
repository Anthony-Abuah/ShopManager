package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.screens

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.camera.InflateCameraPreview
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CameraScreenTopBar


@Composable
fun CustomerCameraScreen(
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var photoIsTaken by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            CameraScreenTopBar(
                photoIsTaken = photoIsTaken,
                setPhotoIsTakenToFalse = { photoIsTaken = false },
                topBarTitleText = if (!photoIsTaken) "Take Photo" else "Cancel"
            ) {
                navigateBack()
            }
        }
    ) {
        it
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            InflateCameraPreview(
                photoIsTaken = photoIsTaken,
                setPhotoIsTakenToTrue = { photoIsTaken = true },
                controller = remember {
                    LifecycleCameraController(context).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE
                        )
                    }
                }
            ) { isPhotoTaken, imageUri ->
                photoIsTaken = isPhotoTaken
                navigateBack()
            }
        }
    }
}
