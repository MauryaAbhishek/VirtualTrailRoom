package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.ui.components.InfoPanel
import com.virtualtrialroom.app.ui.components.LoadingState
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.ui.components.SecondaryActionButton
import com.virtualtrialroom.app.util.AppError
import com.virtualtrialroom.app.viewmodel.ResultViewModel

@Composable
fun ResultRoute(
    resultId: String,
    onBackToHomeClick: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel()
) {
    val imageUrl = viewModel.imageUrlFor(resultId)
    var retryKey by remember(resultId) { mutableIntStateOf(0) }
    var isImageLoading by remember(resultId, retryKey) { mutableStateOf(true) }
    var imageErrorMessage by remember(resultId, retryKey) { mutableStateOf<String?>(null) }
    val displayUrl = remember(imageUrl, retryKey) {
        "$imageUrl?reload=$retryKey"
    }

    AppScaffold(title = "Result") {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = displayUrl,
                    contentDescription = "Generated try-on image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    onLoading = {
                        isImageLoading = true
                        imageErrorMessage = null
                    },
                    onSuccess = {
                        isImageLoading = false
                        imageErrorMessage = null
                    },
                    onError = { state ->
                        isImageLoading = false
                        val reason = state.result.throwable.message
                            ?: state.result.throwable::class.simpleName
                            ?: "Unknown image loading error"
                        imageErrorMessage = "Generated image could not be loaded. $reason"
                    }
                )
                if (isImageLoading) {
                    LoadingState(message = "Loading generated image")
                }
            }
            if (imageErrorMessage != null) {
                ErrorState(
                    error = AppError.Network(imageErrorMessage ?: "Generated image could not be loaded."),
                    onRetryClick = {
                        retryKey += 1
                    }
                )
            }
            InfoPanel(
                title = "Generated image",
                body = "Result ID: $resultId\nImage URL: $imageUrl",
                icon = Icons.Filled.Image
            )
            SecondaryActionButton(
                text = "Reload Image",
                icon = Icons.Filled.Refresh,
                onClick = { retryKey += 1 }
            )
            PrimaryActionButton(
                text = "Back To Home",
                icon = Icons.Filled.Home,
                onClick = onBackToHomeClick
            )
        }
    }
}
