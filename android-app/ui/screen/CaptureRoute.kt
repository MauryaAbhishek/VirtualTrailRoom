package com.virtualtrialroom.app.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.virtualtrialroom.app.ui.components.InfoPanel
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.viewmodel.CameraViewModel
import java.io.File

@Composable
fun CaptureRoute(
    onBackClick: () -> Unit,
    onPhotoCaptured: (String) -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = viewModel::onPermissionResult
    )
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.importGalleryImage(uri)
            }
        }
    )

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.onPermissionResult(granted)
        if (!granted) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(uiState.capturedPhoto?.id) {
        val photo = uiState.capturedPhoto
        if (photo != null) {
            onPhotoCaptured(photo.id)
        }
    }

    if (uiState.hasCameraPermission) {
        CameraCaptureContent(
            isCapturing = uiState.isCapturing,
            isImporting = uiState.isImporting,
            errorMessage = uiState.errorMessage,
            onBackClick = onBackClick,
            onGalleryClick = { galleryLauncher.launch("image/*") },
            onCameraReady = viewModel::onCameraReady,
            onCameraUnavailable = viewModel::onCameraUnavailable,
            onPrepareCapture = viewModel::prepareCapture,
            onCaptureStarted = viewModel::onCaptureStarted,
            onCaptureSuccess = viewModel::onCaptureSuccess,
            onCaptureFailed = viewModel::onCaptureFailed
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            PermissionContent(
                onRequestPermission = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                errorMessage = uiState.errorMessage
            )
        }
    }
}

@Composable
private fun PermissionContent(
    onRequestPermission: () -> Unit,
    errorMessage: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoPanel(
            title = "Camera access",
            body = errorMessage ?: "Allow camera access to capture a try-on image.",
            icon = Icons.Filled.Lock
        )
        PrimaryActionButton(
            text = "Allow Camera",
            icon = Icons.Filled.CameraAlt,
            onClick = onRequestPermission
        )
    }
}

@Composable
private fun CameraCaptureContent(
    isCapturing: Boolean,
    isImporting: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraReady: () -> Unit,
    onCameraUnavailable: (String) -> Unit,
    onPrepareCapture: () -> Pair<File, ImageCapture.OutputFileOptions>,
    onCaptureStarted: () -> Unit,
    onCaptureSuccess: (File) -> Unit,
    onCaptureFailed: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember(context) {
        ContextCompat.getMainExecutor(context)
    }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            ProcessCameraProvider.getInstance(context).get().unbindAll()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { factoryContext ->
                PreviewView(factoryContext).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    previewView = this
                }
            }
        )

        CameraBinder(
            previewView = previewView,
            imageCapture = imageCapture,
            onCameraReady = onCameraReady,
            onCameraUnavailable = onCameraUnavailable
        )

        if (errorMessage != null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 72.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ) {
                androidx.compose.material3.Text(
                    modifier = Modifier.padding(14.dp),
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(16.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(16.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            IconButton(
                onClick = onGalleryClick,
                enabled = !isCapturing && !isImporting
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoLibrary,
                    contentDescription = "Import From Gallery"
                )
            }
        }

        if (isCapturing || isImporting) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(
            onClick = {
                if (!isCapturing && !isImporting) {
                    val (file, outputOptions) = onPrepareCapture()
                    onCaptureStarted()
                    imageCapture.takePicture(
                        outputOptions,
                        cameraExecutor,
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                onCaptureSuccess(file)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                onCaptureFailed(
                                    exception.message ?: "Unable to capture image."
                                )
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 28.dp)
                .size(84.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                    shape = CircleShape
                )
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .padding(8.dp),
            enabled = !isCapturing && !isImporting
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Capture Photo",
                modifier = Modifier.size(34.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CameraBinder(
    previewView: PreviewView?,
    imageCapture: ImageCapture,
    onCameraReady: () -> Unit,
    onCameraUnavailable: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(previewView, lifecycleOwner, imageCapture) {
        val view = previewView ?: return@LaunchedEffect
        val provider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(view.surfaceProvider)
        }

        try {
            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
            onCameraReady()
        } catch (exception: IllegalStateException) {
            onCameraUnavailable(exception.message ?: "Camera is not available.")
        } catch (exception: IllegalArgumentException) {
            onCameraUnavailable(exception.message ?: "Camera could not be configured.")
        }
    }
}
