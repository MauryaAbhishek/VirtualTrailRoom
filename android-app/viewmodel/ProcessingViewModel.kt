package com.virtualtrialroom.app.viewmodel

import androidx.lifecycle.viewModelScope
import com.virtualtrialroom.app.data.remote.RemoteImageDataSource
import com.virtualtrialroom.app.util.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProcessingViewModel @Inject constructor(
    private val remoteImageDataSource: RemoteImageDataSource,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(TryOnFlowUiState(isLoading = true))
    val uiState: StateFlow<TryOnFlowUiState> = _uiState.asStateFlow()

    fun pollJob(jobId: String) {
        if (_uiState.value.jobId == jobId && _uiState.value.outputImageId != null) return
        _uiState.update { it.copy(isLoading = true, jobId = jobId, errorMessage = null) }
        viewModelScope.launch {
            repeat(MAX_ATTEMPTS) {
                val result = runCatching {
                    withContext(dispatchers.io) {
                        remoteImageDataSource.getTryOnJob(jobId)
                    }
                }
                result.onSuccess { job ->
                    when (job.status) {
                        "completed" -> {
                            val outputId = requireNotNull(job.outputImageId) {
                                "Try-on completed without output image."
                            }
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    outputImageId = outputId,
                                    outputImageUrl = remoteImageDataSource.imageContentUrl(outputId),
                                    errorMessage = null
                                )
                            }
                            return@launch
                        }
                        "failed" -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = job.errorMessage ?: "Try-on generation failed."
                                )
                            }
                            return@launch
                        }
                    }
                }.onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to check try-on status."
                        )
                    }
                    return@launch
                }
                delay(POLL_INTERVAL_MS)
            }
            _uiState.update {
                it.copy(isLoading = false, errorMessage = "Try-on generation timed out.")
            }
        }
    }

    private companion object {
        const val MAX_ATTEMPTS = 60
        const val POLL_INTERVAL_MS = 1_000L
    }
}
