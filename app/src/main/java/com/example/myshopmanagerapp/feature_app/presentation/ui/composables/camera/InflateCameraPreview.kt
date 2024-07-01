package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.camera

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CustomizeButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.io.File

@Composable
fun InflateCameraPreview(
    photoIsTaken: Boolean,
    setPhotoIsTakenToTrue: () -> Unit,
    controller: LifecycleCameraController,
    onGoBack: (Boolean, Uri?) -> Unit,
    //onPhotoTaken: (Bitmap) -> Unit,
    //captureImage: (LifecycleCameraController, Context) -> Unit
) {
    val context = LocalContext.current


    var isImageSelected by remember {
        mutableStateOf(false)
    }
    var imageBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var selectedUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            selectedUri = uri
            val readFlag = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            //val writeFlag = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            val resolver = context.contentResolver
            if (uri != null) {
                resolver.takePersistableUriPermission(uri, readFlag)
            }
            setPhotoIsTakenToTrue()
            isImageSelected = true
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier.weight(8f),
            contentAlignment = Alignment.Center
        ) {
            if (/* isImageSelected || */ photoIsTaken){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = emptyString,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            /*
            else if (photoIsTaken) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    imageBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = emptyString
                        )
                    }
                }
            }*/

            else {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    controller = controller
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*
            if (isImageSelected) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    CustomizeButton(
                        buttonName = "Save",
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                        buttonHeight = LocalSpacing.current.buttonHeight
                    ) {
                        onGoBack(true, selectedUri)
                    }
                }
            }
            else
            */
            if (photoIsTaken) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    CustomizeButton(
                        buttonName = "Save",
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                        buttonHeight = LocalSpacing.current.buttonHeight
                    ) {
                        setPhotoIsTakenToTrue()
                        onGoBack(true, selectedUri)
                    }
                }
            }
            else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSpacing.current.small),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .size(40.dp)
                        .padding(LocalSpacing.current.small)
                    ) {
                        IconButton(onClick = {
                            singlePhotoLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_photo_gallery),
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .padding(0.dp)
                    ) {
                        IconButton(onClick = {
                            controller.takePicture(
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageCapturedCallback() {
                                    override fun onCaptureSuccess(image: ImageProxy) {
                                        super.onCaptureSuccess(image)

                                        val matrix = Matrix().apply {
                                            postRotate(image.imageInfo.rotationDegrees.toFloat())
                                        }
                                        val rotatedBitmap = Bitmap.createBitmap(
                                            image.toBitmap(),
                                            0,
                                            0,
                                            image.width,
                                            image.height,
                                            matrix,
                                            true
                                        )
                                        //onPhotoTaken(rotatedBitmap)
                                        imageBitmap = rotatedBitmap
                                        selectedUri = context.saveImageAsUri(imageBitmap!!)
                                        setPhotoIsTakenToTrue()
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        super.onError(exception)
                                        Log.e("Camera", "Couldn't take photo: ", exception)
                                    }
                                }
                            )
                        }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(
                                        LocalSpacing.current.extraSmall,
                                        MaterialTheme.colorScheme.onBackground,
                                        CircleShape
                                    )
                                    .padding(LocalSpacing.current.noPadding),
                                painter = painterResource(id = R.drawable.ic_circle),
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(LocalSpacing.current.small)
                    ) {
                        IconButton(onClick = {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else CameraSelector.DEFAULT_BACK_CAMERA
                        }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera_rotate),
                                contentDescription = emptyString,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }

    }
}



private fun Context.saveImageAsUri(bitmap: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        val fileName = System.nanoTime().toString() + ".png"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            } else {
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                val file = File(directory, fileName)
                put(MediaStore.MediaColumns.DATA, file.absolutePath)
            }
        }

        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            contentResolver.openOutputStream(it).use { output ->
                if (output != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.apply {
                    clear()
                    put(MediaStore.Audio.Media.IS_PENDING, 0)
                }
                contentResolver.update(uri, values, null, null)
            }
        }
        return uri
    }catch (e: java.lang.Exception) {
        if (uri != null) {
            // Don't leave an orphan entry in the MediaStore
            contentResolver.delete(uri, null, null)
        }
        throw e
    }
}


