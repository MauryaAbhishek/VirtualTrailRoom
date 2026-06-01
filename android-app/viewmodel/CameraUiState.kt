package com.virtualtrialroom.app.viewmodel

import com.virtualtrialroom.app.domain.model.UserPhoto

data class CameraUiState(
    val hasCameraPermission: Boolean = false,
    val isCameraReady: Boolean = false,
    val isCapturing: Boolean = false,
    val isImporting: Boolean = false,
    val capturedPhoto: UserPhoto? = null,
    val errorMessage: String? = null
)
