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

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            runCatching {
                withContext(dispatchers.io) {
                    val userPhoto = requireNotNull(photoFileManager.findPhotoById(photoId)) {
                        "Captured image was not found. Please capture or import again."
                    }
                    val clothingUri = clothingAssetManager.getOrCreateClothingUri(clothingId)
                    val userUpload = remoteImageDataSource.uploadLocalImage(
                        localUri = userPhoto.localUri,
                        kind = RemoteImageKind.USER
                    )
                    val clothingUpload = remoteImageDataSource.uploadLocalImage(
                        localUri = clothingUri,
                        kind = RemoteImageKind.CLOTHING
                    )
                    remoteImageDataSource.createTryOnJob(
                        userImageId = userUpload.id,
                        clothingImageId = clothingUpload.id
                    )
                }
            }.onSuccess { job ->
                _uiState.update {
                    it.copy(isLoading = false, jobId = job.id, errorMessage = null)
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to start try-on generation."
                    )
                }
            }
        }
    }

    fun consumeJobNavigation() {
        _uiState.update { it.copy(jobId = null) }
    }
}

