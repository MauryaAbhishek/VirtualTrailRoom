package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.ui.components.InfoPanel
import com.virtualtrialroom.app.ui.components.LoadingState
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.viewmodel.PreviewViewModel

@Composable
fun PreviewRoute(
    photoId: String,
    clothingId: String,
    onBackClick: () -> Unit,
    onJobCreated: (String) -> Unit,
    viewModel: PreviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.jobId) {
        val jobId = uiState.jobId
        if (jobId != null) {
            onJobCreated(jobId)
            viewModel.consumeJobNavigation()
        }
    }

    AppScaffold(
        title = "Preview",
        onBackClick = onBackClick
    ) {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoPanel(
                title = "Selection",
                body = "Image: $photoId\nGarment: $clothingId",
                icon = Icons.Filled.ImageSearch
            )
            if (uiState.isLoading) {
                LoadingState(message = "Uploading images and creating try-on job.")
            }
            if (uiState.errorMessage != null) {
                ErrorState(
                    error = com.virtualtrialroom.app.util.AppError.Network(uiState.errorMessage ?: ""),
                    onRetryClick = { viewModel.submitTryOn(photoId, clothingId) }
                )
            }
            PrimaryActionButton(
                text = if (uiState.isLoading) "Starting" else "Generate Try-On",
                icon = Icons.Filled.AutoAwesome,
                enabled = !uiState.isLoading,
                onClick = { viewModel.submitTryOn(photoId, clothingId) }
            )
        }
    }
}
