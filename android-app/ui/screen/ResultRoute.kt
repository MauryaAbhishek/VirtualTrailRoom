package com.virtualtrialroom.app.ui.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.ErrorState
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
    val context = LocalContext.current
    val imageUrl = viewModel.imageUrlFor(resultId)
    var retryKey by remember(resultId) { mutableIntStateOf(0) }
    var isImageLoading by remember(resultId, retryKey) { mutableStateOf(true) }
    var imageErrorMessage by remember(resultId, retryKey) { mutableStateOf<String?>(null) }
    val displayUrl = remember(imageUrl, retryKey) {
        "$imageUrl?reload=$retryKey"
    }

    AppScaffold(title = "Result") {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .clip(RoundedCornerShape(24.dp))
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
                    ResultLabel()
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
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryActionButton(
                    text = "Reload",
                    icon = Icons.Filled.Refresh,
                    modifier = Modifier.weight(1f),
                    onClick = { retryKey += 1 }
                )
                SecondaryActionButton(
                    text = "Share",
                    icon = Icons.Filled.Share,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, imageUrl)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share try-on")
                        context.startActivity(shareIntent)
                    }
                )
            }
            PrimaryActionButton(
                text = "Create Another Look",
                icon = Icons.Filled.Home,
                onClick = onBackToHomeClick
            )
        }
    }
}

@Composable
private fun ResultLabel() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.Image, contentDescription = null)
            Text(
                text = "Generated Look",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
