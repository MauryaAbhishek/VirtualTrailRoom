package com.virtualtrialroom.app.viewmodel

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.lifecycle.viewModelScope
import com.virtualtrialroom.app.data.local.PhotoFileManager
import com.virtualtrialroom.app.domain.model.ImageSource
import com.virtualtrialroom.app.util.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val photoFileManager: PhotoFileManager,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun onPermissionResult(granted: Boolean) {
        _uiState.update {
            it.copy(
                hasCameraPermission = granted,
                errorMessage = if (granted) null else "Camera permission is required to capture a try-on image."
            )
        }
    }

    fun onCameraReady() {
        _uiState.update {
            it.copy(isCameraReady = true, errorMessage = null)
        }
    }

    fun onCameraUnavailable(message: String) {
        _uiState.update {
            it.copy(isCameraReady = false, isCapturing = false, errorMessage = message)
        }
    }

    fun prepareCapture(): Pair<File, ImageCapture.OutputFileOptions> {
        val file = photoFileManager.createCaptureFile()
        return file to ImageCapture.OutputFileOptions.Builder(file).build()
    }

    fun onCaptureStarted() {
        _uiState.update {
            it.copy(isCapturing = true, errorMessage = null)
        }
    }

    fun onCaptureSuccess(file: File) {
        val photo = photoFileManager.toUserPhoto(
            file = file,
            source = ImageSource.CAMERA
        )
        _uiState.update {
            it.copy(
                isCapturing = false,
                capturedPhoto = photo,
                errorMessage = null
            )
        }
    }

    fun onCaptureFailed(message: String) {
        _uiState.update {
            it.copy(
                isCapturing = false,
                errorMessage = message
            )
        }
    }

    fun importGalleryImage(uri: Uri) {
        _uiState.update {
            it.copy(isImporting = true, errorMessage = null)
        }

        viewModelScope.launch {
            runCatching {
                withContext(dispatchers.io) {
                    photoFileManager.importGalleryImage(uri)
                }
            }.onSuccess { photo ->
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        capturedPhoto = photo,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        errorMessage = throwable.message ?: "Unable to import selected image."
                    )
                }
            }
        }
    }
}
