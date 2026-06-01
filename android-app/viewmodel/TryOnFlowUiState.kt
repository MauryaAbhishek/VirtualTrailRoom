package com.virtualtrialroom.app.viewmodel

data class TryOnFlowUiState(
    val isLoading: Boolean = false,
    val jobId: String? = null,
    val outputImageId: String? = null,
    val outputImageUrl: String? = null,
    val statusMessage: String? = null,
    val errorMessage: String? = null
)
