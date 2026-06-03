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
        _uiState.update {
            it.copy(
                isLoading = true,
                jobId = jobId,
                statusMessage = "Waiting for backend processing...",
                errorMessage = null
            )
        }
        viewModelScope.launch {
            repeat(MAX_ATTEMPTS) { attempt ->
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
                                    statusMessage = null,
                                    errorMessage = null
                                )
                            }
                            return@launch
                        }
                        "failed" -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    statusMessage = null,
                                    errorMessage = job.errorMessage.toUserFacingAiError()
                                )
                            }
                            return@launch
                        }
                    }
                }.onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            statusMessage = null,
                            errorMessage = throwable.message ?: "Unable to check try-on status."
                        )
                    }
                    return@launch
                }
                _uiState.update {
                    it.copy(statusMessage = "Generating try-on image... ${attempt + 1}/$MAX_ATTEMPTS")
                }
                delay(POLL_INTERVAL_MS)
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    statusMessage = null,
                    errorMessage = "Try-on generation timed out. Please retry."
                )
            }
        }
    }

    private companion object {
        const val MAX_ATTEMPTS = 60
        const val POLL_INTERVAL_MS = 1_000L
    }
}

private fun String?.toUserFacingAiError(): String {
    val rawMessage = this?.trim().orEmpty()
    if (rawMessage.isBlank()) {
        return "Try-on generation failed. Please retry."
    }
    val normalized = rawMessage.lowercase()
    return when {
        "402" in normalized || "payment required" in normalized -> {
            "AI generation credits are not available. Add credits or switch the backend AI provider, then retry."
        }
        "runpod" in normalized && ("credit" in normalized || "billing" in normalized) -> {
            "RunPod billing is not ready. Add RunPod credits or switch to the FASHN provider, then retry."
        }
        "api.runpod.ai" in normalized -> {
            "The AI provider is unavailable right now. Please check backend provider settings and retry."
        }
        "timeout" in normalized || "timed out" in normalized -> {
            "AI generation is taking too long. Please retry with a clearer person and garment image."
        }
        else -> rawMessage.take(MAX_ERROR_LENGTH)
    }
}

private const val MAX_ERROR_LENGTH = 160
