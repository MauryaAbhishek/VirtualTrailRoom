package com.virtualtrialroom.app.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.virtualtrialroom.app.data.local.ClothingAssetManager
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
class WardrobeViewModel @Inject constructor(
    private val clothingAssetManager: ClothingAssetManager,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(TryOnFlowUiState())
    val uiState: StateFlow<TryOnFlowUiState> = _uiState.asStateFlow()

    fun importGarment(uri: Uri) {
        if (_uiState.value.isLoading) return
        _uiState.update {
            it.copy(
                isLoading = true,
                statusMessage = "Importing garment image...",
                errorMessage = null
            )
        }

        viewModelScope.launch {
            runCatching {
                withContext(dispatchers.io) {
                    clothingAssetManager.importClothingImage(uri)
                }
            }.onSuccess { clothingId ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        jobId = clothingId,
                        statusMessage = null,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        statusMessage = null,
                        errorMessage = throwable.message ?: "Unable to import garment image."
                    )
                }
            }
        }
    }

    fun consumeImportedGarment() {
        _uiState.update { it.copy(jobId = null) }
    }
}
