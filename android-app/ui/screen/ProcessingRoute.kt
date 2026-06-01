package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.ui.components.LoadingState
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.util.AppError
import com.virtualtrialroom.app.viewmodel.ProcessingViewModel

@Composable
fun ProcessingRoute(
    requestId: String,
    onResultReady: (String) -> Unit,
    viewModel: ProcessingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(requestId) {
        viewModel.pollJob(requestId)
    }

    LaunchedEffect(uiState.outputImageId) {
        val outputImageId = uiState.outputImageId
        if (outputImageId != null) {
            onResultReady(outputImageId)
        }
    }

    AppScaffold(title = "Generating") {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.isLoading) {
                LoadingState(message = "Processing request $requestId")
            }
            if (uiState.errorMessage != null) {
                ErrorState(
                    error = AppError.AiProcessing(uiState.errorMessage ?: "Try-on generation failed."),
                    onRetryClick = { viewModel.pollJob(requestId) }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (uiState.outputImageId != null) {
                PrimaryActionButton(
                    text = "View Result",
                    icon = Icons.Filled.Image,
                    onClick = { onResultReady(uiState.outputImageId ?: return@PrimaryActionButton) }
                )
            }
        }
    }
}
