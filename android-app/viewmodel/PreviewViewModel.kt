package com.virtualtrialroom.app.viewmodel

import androidx.lifecycle.viewModelScope
import com.virtualtrialroom.app.data.local.ClothingAssetManager
import com.virtualtrialroom.app.data.local.PhotoFileManager
import com.virtualtrialroom.app.data.remote.RemoteImageDataSource
import com.virtualtrialroom.app.data.remote.RemoteImageKind
import com.virtualtrialroom.app.util.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val photoFileManager: PhotoFileManager,
    private val clothingAssetManager: ClothingAssetManager,
    private val remoteImageDataSource: RemoteImageDataSource,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(TryOnFlowUiState())
    val uiState: StateFlow<TryOnFlowUiState> = _uiState.asStateFlow()

    fun submitTryOn(photoId: String, clothingId: String) {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                isLoading = true,
                statusMessage = "Preparing images...",
                errorMessage = null
            )
        }
        viewModelScope.launch {
            runCatching {
                withTimeout(SUBMIT_TIMEOUT_MS) {
                    withContext(dispatchers.io) {
                        _uiState.update { it.copy(statusMessage = "Reading captured photo...") }
                        val userPhoto = requireNotNull(photoFileManager.findPhotoById(photoId)) {
                            "Captured image was not found. Please capture or import again."
                        }
                        _uiState.update { it.copy(statusMessage = "Preparing garment image...") }
                        val clothingUri = clothingAssetManager.getOrCreateClothingUri(clothingId)
                        _uiState.update { it.copy(statusMessage = "Uploading captured photo...") }
                        val userUpload = remoteImageDataSource.uploadLocalImage(
                            localUri = userPhoto.localUri,
                            kind = RemoteImageKind.USER
                        )
                        _uiState.update { it.copy(statusMessage = "Uploading garment image...") }
                        val clothingUpload = remoteImageDataSource.uploadLocalImage(
                            localUri = clothingUri,
                            kind = RemoteImageKind.CLOTHING
                        )
                        _uiState.update { it.copy(statusMessage = "Starting try-on generation...") }
                        remoteImageDataSource.createTryOnJob(
                            userImageId = userUpload.id,
                            clothingImageId = clothingUpload.id
                        )
                    }
                }
            }.onSuccess { job ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        jobId = job.id,
                        statusMessage = null,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        statusMessage = null,
                        errorMessage = throwable.message ?: "Unable to start try-on generation."
                    )
                }
            }
        }
    }

    fun consumeJobNavigation() {
        _uiState.update { it.copy(jobId = null) }
    }

    private companion object {
        const val SUBMIT_TIMEOUT_MS = 120_000L
    }
}
